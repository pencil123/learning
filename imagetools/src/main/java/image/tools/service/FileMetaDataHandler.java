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
import java.io.File;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileMetaDataHandler {

    private Logger logger = LoggerFactory.getLogger(FileMetaDataHandler.class);

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
