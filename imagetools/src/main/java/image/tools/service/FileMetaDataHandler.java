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
    @Value("config.source")
    private String sourcePath;
    @Value("config.target")
    private String targetPath;
    @Value("config.temp")
    private String tempPath;
    private Logger logger = LoggerFactory.getLogger(FileMetaDataHandler.class);
    private List<FileDto> fileDtos = new ArrayList<>();

    /**
     * 比较 temp 和 target 目录中的文件
     * 如果 hash 值相同，则删除 temp 中的文件
     */

    public void deleteTempPath(){
        printFilesAndMeta(tempPath);
        for(FileDto fileDto: fileDtos){

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
            String week = fileDto.getCreateTime().format(DateTimeFormatter.ofPattern("YYYYMMW"));
            String minseconds = fileDto.getCreateTime().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssS"));
            Path path = Paths.get(targetPath + week);
            if(!Files.exists(path)){
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Files.move(Paths.get(fileDto.getFileName()),Paths.get(targetPath + week +"\\"+ minseconds + "." + fileDto.getType()));
            } catch (IOException e) {
                /**
                 * 文件移动到 target 目录失败，则移动到 temp 目录中
                 */
                Path temp = Paths.get(tempPath + week);
                if(!Files.exists(temp)){
                    try {
                        Files.createDirectory(path);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                try {
                    Files.move(Paths.get(fileDto.getFileName()),Paths.get(tempPath + week +"\\"+ minseconds + "." + fileDto.getType()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                logger.warn("move file:{} to target:{} error",fileDto.getFileName(),targetPath + week +"\\"+ minseconds + "." + fileDto.getType());
            }
        }
    }



    /**
     * 遍历目录，将目录中的文件存储到 fileDtos 列表中
     * @param filePath
     */
    private void printFilesAndMeta(String filePath){
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
