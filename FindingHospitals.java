   package hospital;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import freemarker.log.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
 
public class FindingHospitals extends DriverSetup {
    public static ExtentReports extent;
    public static ExtentTest test;
 
    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("PractoTestReport.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Practo Automation Report");
        spark.config().setReportName("Practo Hospital & Medicine Test");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Varsha");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
    }
 
    @Test
    public void practoAutomationTest() {
        test = extent.createTest("Practo Automation Test");
        try {
            String browser = "chrome";
            launchBrowser(browser);
            ScreenshotUtil.capture(driver, "Browser Launched", test);
            driver.get("https://www.practo.com");
            ScreenshotUtil.capture(driver, "Practo Home Page", test);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,1000)");
            ScreenshotUtil.capture(driver, "Scrolled Down", test);
            WebElement searchforhospital = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/footer/div/div[1]/div[2]/div[2]/a[3]/span"));
            js.executeScript("arguments[0].scrollIntoView(true);", searchforhospital);
            Thread.sleep(2000);
            searchforhospital.click();
            ScreenshotUtil.capture(driver, "Clicked on Hospital Search", test);
            String[] cities = {"Bangalore", "Chennai", "Hyderabad", "Mumbai", "Delhi"};
            List<String[]> topHospitals = new ArrayList<>();
            for (String city : cities) {
                setLocation(driver, city);
                
                Thread.sleep(3000);
                String[] hospitalData = getTopHospital(driver);
                if (hospitalData != null) {
                	System.out.println("City: " + city);
                    System.out.println("Hospital: " + hospitalData[0]);
                    System.out.println("Timing: " + hospitalData[1]);
                    System.out.println("Rating: " + hospitalData[2]);
                    System.out.println("-----------------------------");
                    topHospitals.add(new String[]{city, hospitalData[0], hospitalData[1], hospitalData[2]});
                    test.log(Status.INFO, "Hospital found in " + city + ": " + hospitalData[0]);
                }
                else {
                	System.out.println("No hospital data found for " + city);
                }
            }
            js.executeScript("window.scrollBy(0,1000)");
            ScreenshotUtil.capture(driver, "Scrolled to Practo Plus", test);
            WebElement practo = driver.findElement(By.linkText("Practo Plus"));
            js.executeScript("arguments[0].scrollIntoView(true);", practo);
            Thread.sleep(2000);
            practo.click();
            ScreenshotUtil.capture(driver, "Clicked on Practo Plus", test);
            String originalWindow = driver.getWindowHandle();
            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
            driver.navigate().to("https://www.practo.com/order?utm_source=practonavbar&utm_medium=referral&utm_campaign=practonavbarredirect");
            Thread.sleep(3000);
            ScreenshotUtil.capture(driver, "Navigated to Practo Order Page", test);
            
            JavascriptExecutor ja = (JavascriptExecutor) driver;
 
         // Explicitly scroll down to make the popular medicines visible
         ja.executeScript("window.scrollBy(0,1000)");
 
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
 
            // Wait for at least one popular medicine title to be present
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("m-product__title")));
 
            List<String> popularMedicines = new ArrayList<>();
            List<WebElement> Names = driver.findElements(By.className("m-product__title"));
 
            ScreenshotUtil.capture(driver, "Popular Medicines Displayed", test);
 
            for (int i = 0; i < Math.min(5, Names.size()); i++) {
            	String name = Names.get(i).getText();
    			System.out.println((i + 1) + ". " + name);
                popularMedicines.add(Names.get(i).getText());
                test.log(Status.INFO, "Popular Medicine: " + Names.get(i).getText());
            }
            driver.get("https://www.practo.com/company/contact");
            ScreenshotUtil.capture(driver, "Navigated to Contact Page", test);
            Select select = new Select(driver.findElement(By.xpath("//*[@id=\"contactus-form\"]/div[1]/div/div/div[1]/select")));
            select.selectByVisibleText("Free trial for a software to manage my clinic");
            ScreenshotUtil.capture(driver, "Dropdown Selected", test);
            driver.findElement(By.xpath("//*[@id=\"contactus-form\"]/div[1]/div/div/div[2]/div[2]/input")).sendKeys("Rajagomathi");
            driver.findElement(By.xpath("//*[@id=\"contactus-form\"]/div[1]/div/div/div[2]/div[3]/div/input")).sendKeys("8736387463874");
            ScreenshotUtil.capture(driver, "Entered Name and Phone", test);
            driver.findElement(By.xpath("//*[@id=\"contactus-form\"]/div[1]/div/div/div[2]/div[7]/div[1]/span[1]/label/span")).click();
            ScreenshotUtil.capture(driver, "Checkbox Selected", test);
            driver.findElement(By.xpath("//*[@id=\"contactus-form\"]/div[2]/input")).click();
            ScreenshotUtil.capture(driver, "Form Submitted", test);
            WebElement iframe = driver.findElement(By.id("login-iframe-form"));
            driver.switchTo().frame(iframe);
            WebElement popupElement = driver.findElement(By.xpath("//*[@id='otpSentMsg']"));
            String popupText = popupElement.getText();
            test.log(Status.INFO, "Popup Text: " + popupText);
            ScreenshotUtil.capture(driver, "Popup Captured", test);
            System.out.println("---------------------------------");
	        System.out.println("");
	        System.out.println("Popup Text: " + popupText);
            
            
            writeToExcel(topHospitals, popularMedicines, "TopHospitals.xlsx");
            test.log(Status.INFO, "Excel Written");
            
            test.log(Status.PASS, "Test completed successfully.");
        } catch (Exception e) {
            ScreenshotUtil.capture(driver, "Error", test);
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
 
    @AfterMethod
    public void tearDownMethod() {
        if (driver != null) {
            driver.quit();
        }
    }
 
    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
    
    // Helper Methods from the previous code
    public static void setLocation(WebDriver driver, String city) throws InterruptedException {
        WebElement location = driver.findElement(
                By.xpath("//*[@id=\"c-omni-container\"]/div/div[1]/div/input"));
        location.click();
        driver.findElement(By.className("icon-ic_cross_solid")).click();
        location.sendKeys(city);
        location.sendKeys(Keys.ARROW_DOWN);
        Thread.sleep(2000);
        location.sendKeys(Keys.ARROW_DOWN);
        location.sendKeys(Keys.ENTER);
    }
    
    public static String[] getTopHospital(WebDriver driver) {
        try {
            WebElement hospitalCard = driver.findElement(By.className("inner"));
            String name = hospitalCard.findElement(By.tagName("h2")).getText();
            String timing = hospitalCard.findElement(By.className("pd-right-2px-text-green")).getText();
            String ratingText = hospitalCard.findElement(By.className("c-feedback")).getText();
            String rating = ratingText.split(" ")[0];
            return new String[]{name, timing, rating};
        } catch (Exception e) {
            return null;
        }
    }
 
    public static void writeToExcel(List<String[]> data, List<String> medicineData, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Top Hospitals");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("City");
        header.createCell(1).setCellValue("Hospital Name");
        header.createCell(2).setCellValue("Timing");
        header.createCell(3).setCellValue("Rating"); 
        int rowCount = 1;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 0; i < rowData.length; i++) {
                row.createCell(i).setCellValue(rowData[i]);
            }
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        
        
        Sheet medicineSheet = workbook.createSheet("Popular Medicines");
        Row medHeader = medicineSheet.createRow(0);
        medHeader.createCell(0).setCellValue("Medicine Name");
        medHeader.createCell(1).setCellValue("Quantity");
        medHeader.createCell(2).setCellValue("Unit");
    
        for (int i = 0; i < medicineData.size(); i++) {
            String fullName = medicineData.get(i);
            String nameOnly = fullName;
            String quantity = "";
            String unit = "";
    
            // Regex to extract quantity and unit (e.g., "250ML", "400GM")
            Pattern pattern = Pattern.compile("(\\d+)\\s*(ML|GM)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(fullName);
            if (matcher.find()) {
                quantity = matcher.group(1);
                unit = matcher.group(2).toUpperCase();
                nameOnly = fullName.replace(matcher.group(0), "").trim();
            }
    
            Row row = medicineSheet.createRow(i + 1);
            row.createCell(0).setCellValue(nameOnly);
            row.createCell(1).setCellValue(quantity);
            row.createCell(2).setCellValue(unit);
        }
        medicineSheet.autoSizeColumn(0);
        medicineSheet.autoSizeColumn(1);
        medicineSheet.autoSizeColumn(2);
        
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            workbook.close();
            System.out.println("Sucess : Excel printed sucessfully "+ fileName) ;
        } catch (Exception e) {
            e.printStackTrace();
            test.log(Status.FAIL, "Failed to write to Excel: " + e.getMessage());
        }
    
        
}
    
}
 