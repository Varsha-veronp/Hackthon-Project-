package hospital;
 
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
 
import java.time.Duration;
 
public class DriverSetup {
    public static WebDriver driver;
 
    // Method to launch either Chrome or Edge based on input
    public static void launchBrowser(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
        	  //set path for ChromeDriver
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\2425304\\Downloads\\chromedriver\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
        	  //set path for EdgeDriver
            System.setProperty("webdriver.edge.driver", "\"C:\\Users\\2425304\\Downloads\\edgedriver_win64\\msedgedriver.exe\"");
            driver = new EdgeDriver();
        } else {
            System.out.println("Unsupported browser");
            return;
        }
      //maximize window and set implicit wait
        driver.manage().window().maximize();
        
    }
 
    }
 
 