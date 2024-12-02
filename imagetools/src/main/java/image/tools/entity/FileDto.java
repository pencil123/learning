package image.tools.entity;


import java.time.LocalDateTime;
import lombok.Data;

@Data
public class FileDto {
    private String fileName;
    private Long size;
    private LocalDateTime createTime;
    private String type;

}
