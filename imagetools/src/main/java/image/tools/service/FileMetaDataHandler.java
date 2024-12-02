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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileMetaDataHandler {

    private String TARGET_PATH= "F:\\targets\\";
    private Logger logger = LoggerFactory.getLogger(FileMetaDataHandler.class);
    private List<FileDto> fileDtos = new ArrayList<>();

    public void printFilesAndMeta(String filePath){
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

    public void renameFileNames(){
        for(FileDto fileDto: fileDtos){
            String week = fileDto.getCreateTime().format(DateTimeFormatter.ofPattern("YYYYMMW"));
            String minseconds = fileDto.getCreateTime().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmssS"));
            Path path = Paths.get(TARGET_PATH + week);
            if(!Files.exists(path)){
                try {
                    Files.createDirectory(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Files.move(Paths.get(fileDto.getFileName()),Paths.get(TARGET_PATH + week +"\\"+ minseconds + "." + fileDto.getType()));
            } catch (IOException e) {
                logger.warn("move file:{} to target:{} error",fileDto.getFileName(),TARGET_PATH + week +"\\"+ minseconds + "." + fileDto.getType());
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
