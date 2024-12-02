package image.tools;

import image.tools.service.FileMetaDataHandler;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureTest {
    @Autowired
    private FileMetaDataHandler fileMetaDataHandler;


    @Test
    public void execute() throws Exception {
        fileMetaDataHandler.printFilesAndMeta("F:\\test");
        fileMetaDataHandler.renameFileNames();
        fileMetaDataHandler.printFile();
    }
}
