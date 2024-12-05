package image.tools.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDescriptorBase;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifImageDirectory;
import com.drew.metadata.exif.GpsDirectory;
import image.tools.entity.FileDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileMetaDataHandler {
    @Value("${config.source}")
    private String sourcePath;
    @Value("${config.target}")
    private String targetPath;
    @Value("${config.temp}")
    private String tempPath;
    private Logger logger = LoggerFactory.getLogger(FileMetaDataHandler.class);
    private List<FileDto> fileDtos = new ArrayList<>();

    DateTimeFormatter targetDir = DateTimeFormatter.ofPattern("YYYYMMW");
    DateTimeFormatter targetName =  DateTimeFormatter.ofPattern("YYYYMMddHHmmssS");
    /**
     * 比较 temp 和 target 目录中的文件
     * 如果 hash 值相同，则删除 temp 中的文件
     */

    public void deleteTempPath()throws IOException , NoSuchAlgorithmException{
        printFilesAndMeta(tempPath);
        ArrayList<Path> deletePaths = new ArrayList<>();
        for(FileDto fileDto: fileDtos){
            String targetFileString = fileDto.getFileName().replace(tempPath, targetPath);
            Path path = Paths.get(targetFileString);
            if(Files.exists(path)){
                String tempFileHex = bytesToHex(calculateSHA256(fileDto.getFileName()));
                logger.info("hax:{},file:{}",tempFileHex,fileDto.getFileName());
                String targetFileHex = bytesToHex(calculateSHA256(targetFileString));
                logger.info("hax:{},file:{}",targetFileHex,targetFileString);
                if(tempFileHex.equals(targetFileHex)){
                    deletePaths.add(Paths.get(fileDto.getFileName()));
                    logger.info("delete temp file:{}",fileDto.getFileName());
                }
            }
        }
        for(Path path: deletePaths){
            Files.delete(path);
            logger.info("delete temp file:{}",path.toString());
        }
    }

    /**
     * 计算 SHA256 哈希值
     * @param filePath 文件路径
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */

    private byte[] calculateSHA256(String filePath) throws IOException , NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final FileInputStream fileInputStream = new FileInputStream(filePath);
        FileChannel channel = fileInputStream.getChannel();
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        ByteBuffer allocate = ByteBuffer.allocate(8192);
        while(channel.read(allocate) != -1){
            allocate.flip();
            digest.update(allocate);
            allocate.clear();
        }
        digestInputStream.close();
        return digest.digest();
    }

    /**
     * 将 字节数组 转化为 String 类型哈希值
     * @param bytes
     * @return
     */
    private String bytesToHex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b: bytes){
            stringBuilder.append(String.format("%02x",b));
        }
        return stringBuilder.toString();
    }

    /**
     * 处理 sourcePath 目录中的文件
     * 将文件根据 createTime,size 移动到 target 目录中。
     * 如果移动失败，则移动到 temp 目录中。
     */
    public void renameFileNames(){
        printFilesAndMeta(sourcePath);
        for(FileDto fileDto: fileDtos){
            String subDir = fileDto.getCreateTime().format(targetDir);
            String nameWithTime = fileDto.getCreateTime().format(targetName);
            Path path = Paths.get(targetPath + subDir);
            if(!Files.exists(path)){
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String subDirWithFileName = subDir +"\\"+ nameWithTime + "_" + fileDto.getSize() +  "." + fileDto.getType();
            try {
                Files.move(Paths.get(fileDto.getFileName()),Paths.get(targetPath + subDirWithFileName));
            } catch (IOException e) {
                /**
                 * 文件移动到 target 目录失败，则移动到 temp 目录中
                 */
                Path temp = Paths.get(tempPath + subDir);
                if(!Files.exists(temp)){
                    try {
                        Files.createDirectory(temp);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                try {
                    Files.move(Paths.get(fileDto.getFileName()),Paths.get(tempPath + subDirWithFileName));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                logger.warn("move file:{} to target:{} error",fileDto.getFileName(),targetPath + subDirWithFileName);
            }
        }
    }



    /**
     * 遍历目录，将目录中的文件存储到 fileDtos 列表中
     * @param filePath
     */
    private void printFilesAndMeta(String filePath){
        logger.info("the filePath:{}",filePath);
        Path path = Paths.get(filePath);
        if(Files.isDirectory(path)){
            try {
                final Stream<Path> list = Files.list(path);
                list.forEach(item->{
                    if(Files.isDirectory(item)){
                        printFilesAndMeta(item.toString());
                    }else{
                        FileDto fileDto = new FileDto();
                        try {
                            int i = item.toString().lastIndexOf('.');
                            fileDto.setType(item.toString().substring(i+1));
                            fileDto.setCreateTime(LocalDateTime.ofInstant(Files.getLastModifiedTime(item).toInstant(),
                                    ZoneId.systemDefault()));
                            fileDto.setFileName(item.toString());
                            fileDto.setSize(Files.size(item));
                            fileDtos.add(fileDto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void printFile(){
        for(FileDto fileDto: fileDtos){
            logger.info(fileDto.toString());
        }
    }





    public void getMetaDatas(String filepath){
        File file = new File(filepath);
        try{
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            Collection<GpsDirectory> gpsDirectories = metadata
                    .getDirectoriesOfType(GpsDirectory.class);
            for (GpsDirectory gpsDirectory : gpsDirectories) {
                // Try to read out the location, making sure it's non-zero
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                if (geoLocation != null && !geoLocation.isZero()) {
                    logger.info("the gps:{},{}",geoLocation.getLatitude(),geoLocation.getLongitude());
                    break;
                }
            }
            for(Directory directory: metadata.getDirectories()){
                logger.info("name:{}",directory.getName());

                if(directory instanceof ExifIFD0Directory){
                    logger.info("Exif TIME:{}",directory.getDate(ExifDirectoryBase.TAG_DATETIME));
                }

                for(Tag tag: directory.getTags()){
                    logger.info("directory:{},tag:{},desc:{]",directory.getName(),tag.getTagName(),tag.getDescription());
                }
            }
        }catch (Exception e){
            logger.error("the exception msg:{}",e.getMessage());
        }
    }
}
