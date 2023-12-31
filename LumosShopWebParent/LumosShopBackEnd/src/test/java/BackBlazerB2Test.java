import com.dgpad.admin.util.B2_Util;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class BackBlazerB2Test {

    @Test
    public void testListFolder() {
        String dir = "user-photos";
        List<String> keys = B2_Util.listDir(dir);
        keys.forEach(System.out::println);
    }

    @Test
    public void testUploadImage() throws FileNotFoundException {
        String folderName = "test-u";
        String fileName = "Energy_Confirm.jpeg";
        String filePath = "C:\\Users\\ali\\Desktop\\ProjectGit\\LumosShop\\LumosShopWebParent\\LumosShopFrontEnd\\src\\main\\resources\\static\\images\\" + fileName;

        InputStream inputStream = new FileInputStream(filePath);
        B2_Util.uploadFile(folderName, fileName, inputStream);

    }

    @Test
    public void testRemoveDir() {
        String dirName = "test-u";
        B2_Util.removeFolder(dirName);

    }
}
