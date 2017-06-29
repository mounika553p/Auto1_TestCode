package testScenarios;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import junit.framework.Assert;


/* CarFilter_Auto1 class verifies if Auto1 website lists only BMW cars on filter. Output will be logged using logger.
 * If any different merchant car is displayed junit test will fail.
 * ChromeDriver, SeleniumStandalone and Logger jars should be configured in build path
 * List of variables : 
 * carDetailsList - Parameters like mileage,fuel type that will be displayed for every car
 * 
 * This class validates the cars displayed in 1st page. Can be extended for all the pages.
 *  */


public class CarFilter_Auto1 {

	private final static Logger log = Logger.getLogger(CarFilter_Auto1.class.getName());
	ConsoleAppender console = new ConsoleAppender();
	WebDriver driver ;


	@Test
	public void verifyCarFilterFeature(){
		
		String PATTERN = "%d [%p|%c|%C{1}] %m%n";
		String carName = "";
		String[] carDetailsList= {"Mileage:","Stock number:","Horsepower:","Fuel type:","First registration:","Gear box:"};
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.INFO);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);

		System.setProperty("webdriver.chrome.driver", "C:\\Wallet\\Eclipse\\TestScenario\\lib\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://auto1.com/en/our-cars");		
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.linkText("Log in")).click();
		driver.findElement(By.id("login-email")).click();
		driver.findElement(By.id("login-email")).clear();
		driver.findElement(By.id("login-email")).sendKeys("mpalla553@gmail.com");
		driver.findElement(By.id("login-password")).clear();
		driver.findElement(By.id("login-password")).sendKeys("test123");
		driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();

		driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys("BMW");
		driver.findElement(By.xpath("//div[3]/div[6]/div/label")).click();
		if(driver.findElement(By.xpath("//div[2]/div[2]/span[2]")).getText().equalsIgnoreCase("BMW"))
			log.info("BMW is selected as filter option");
		else 
			logAndFail("BMW is not selected as filter option");

		List<WebElement> carList = driver.findElements(By.xpath(".//*[contains(@class,'car-list car-list-secured')]//child::li"));
		List<WebElement> carDetails = null;
		log.info("No.of cars displayed in the page is :: " + carList.size());

		for(int carNo=1;carNo<=carList.size();carNo++){
			if(driver.findElement(By.xpath("//*[contains(@class,'car-list car-list-secured')]//child::li["+carNo+"]//child::img")).isEnabled())
				log.info("Car Image is displayed for Car "+carNo);
			else
				logAndFail("Car Image is not displayed for Car "+carNo);
			carName = driver.findElement(By.xpath("//*[contains(@class,'car-list car-list-secured')]//child::li["+carNo+"]//child::div[contains(@class,'car-title')]/a")).getAttribute("text");
			log.info("Car name of Car "+carNo+ " is "+ carName);
			if(!carName.contains("BMW")||!carName.contains("BMW"))
				logAndFail("BMW car is not displayed in the page after applying filter");

			carDetails = driver.findElements(By.xpath("//*[contains(@class,'car-list car-list-secured')]//child::li["+carNo+"]//child::table//child::tr"));
			int count = 0;
			for(int carDetail=1;carDetail<=carDetails.size();carDetail++){
				log.info("Value of "+ driver.findElement(By.xpath("//*[contains(@class,'car-list car-list-secured')]//child::li["+carNo+"]//child::table//child::tr["+carDetail+"]/td[1]")).getText() 
						+" is :: "+driver.findElement(By.xpath("//*[contains(@class,'car-list car-list-secured')]//child::li["+carNo+"]//child::table//child::tr["+carDetail+"]/td[2]")).getText());
				if(containsItemFromList(driver.findElement(By.xpath("//*[contains(@class,'car-list car-list-secured')]//child::li["+carNo+"]//child::table//child::tr["+carDetail+"]/td[1]")).getText(),carDetailsList))
						count++;
			}
			if(count==6)
				log.info("All the details are displayed for Car "+carNo);
			else
				logAndFail("Some car details are not displayed for car "+carNo);
		}


		//driver.quit();

	}

	public void logAndFail(String message)
	{
		log.info(message);
		Assert.assertTrue(message, false);
	}

	public static boolean containsItemFromList(String inputStr, String[] details) {
		for(int i = 0; i < details.length; i++) {
			if(inputStr.contains(details[i]))
				return true;
		}
		return false;
	}

}
