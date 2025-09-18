package hospital;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
 
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
 
public class ScreenshotUtil {
    public static String takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + screenshotName.replace(" ", "_") + ".png";
            File dest = new File(path);
            dest.getParentFile().mkdirs(); // Ensure directory exists
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    public static void capture(WebDriver driver, String stepName, ExtentTest test) {
        String path = takeScreenshot(driver, stepName);
        if (path != null) {
            test.log(com.aventstack.extentreports.Status.INFO, stepName,
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } else {
            test.log(com.aventstack.extentreports.Status.WARNING, "Screenshot failed for: " + stepName);
        }
    }
}
 