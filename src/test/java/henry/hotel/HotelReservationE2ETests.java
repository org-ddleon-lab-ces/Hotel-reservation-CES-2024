package henry.hotel;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HotelReservationE2ETests {
	private static final String SING_IN_TITLE_EXPECT = "Sign In";
	private static final String SING_IN_TITLE = ".sign-in-container #regForm h1";
	private static final String SING_IN_NAME = ".sign-in-container #regForm input[name=username]";
	private static final String SING_IN_PASS = ".sign-in-container #regForm input[name=password]";
	private static final String SING_IN_SUBMIT = ".sign-in-container #regForm #button";
	private static final String SING_UP_TITLE_EXPECT = "Sign Up";
	private static final String SING_UP_GO = ".overlay-panel #signUp";
	private static final String SING_UP_TITLE = ".sign-up-container #regForm h1";
	private static final String SING_UP_NAME = ".sign-up-container #regForm #email";
	private static final String SING_UP_MAIL = ".sign-up-container #regForm input[name=username]";
	private static final String SING_UP_PASS = ".sign-up-container #regForm input[name=password]";
	private static final String SING_UP_MATCHING_PASS = ".sign-up-container #regForm input[name=matchingPassword]";
	private static final String SING_UP_SUBMIT = ".sign-up-container #regForm #button";
	private static final String DASHBOARD_ABOUT_ME = ".main-footer .colum1 h1";
	private static final String DASHBOARD_ABOUT_ME_EXPECT = "About Me";
	
	private static final String ACCOUNT_NAME = "user@server.com";
	private static final String ACCOUNT_MAIL = "username";
	private static final String ACCOUNT_PASS = "gwNm46owaj%gC#wAgA&";

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll	
	static void setupClass() {
		WebDriverManager.chromedriver().setup();		
	}

	@BeforeEach
	void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu"); 
        options.addArguments("--window-size=1920,1080");
		driver = new ChromeDriver(options);
	}

	@AfterEach
	void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	void test() {
		driver.get(getBaseURL());

		/**
		 * Sign Up
		 */
		assertText(SING_IN_TITLE, SING_IN_TITLE_EXPECT);
		click(SING_UP_GO);
		assertText(SING_UP_TITLE, SING_UP_TITLE_EXPECT);
		setValue(SING_UP_NAME, ACCOUNT_NAME);
		setValue(SING_UP_MAIL, ACCOUNT_MAIL);
		setValue(SING_UP_PASS, ACCOUNT_PASS);
		setValue(SING_UP_MATCHING_PASS, ACCOUNT_PASS);
		click(SING_UP_SUBMIT);
		
		/**
		 * Sign In
		 */
		assertText(SING_IN_TITLE, SING_IN_TITLE_EXPECT);
		setValue(SING_IN_NAME, ACCOUNT_NAME);
		setValue(SING_IN_PASS, ACCOUNT_PASS);
		click(SING_IN_SUBMIT);

		/**
		 * Dashboard
		 */
		assertText(DASHBOARD_ABOUT_ME, DASHBOARD_ABOUT_ME_EXPECT);

	}

	String getBaseURL() {
		return "http://localhost:" + this.port + "/";
	}
	
	WebElement find(String selector) {
		
		int maxAttempts = 5; 
		int waitTimeMs = 1000;

		int attempts = 0;
        WebElement element = null;

        while (attempts < maxAttempts) {
            try {
                element = driver.findElement(By.cssSelector(selector));
                return element; 
            } catch (NoSuchElementException e) {
                attempts++;
                System.out.println("Intento " + attempts + " de " + maxAttempts + " fallido. Reintentando en " + waitTimeMs + " milisegundos...");
                try {
                    Thread.sleep(waitTimeMs); 
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        
        return element = driver.findElement(By.cssSelector(selector));
	}
	
	String getText(String selector) {
		return find(selector).getAttribute("innerText");
	}
	
	void setValue(String selector, String value) {
		find(selector).sendKeys(value);
	}
	
	void click(String selector) {
		find(selector).click();
	}

	void assertText(String selector, String expected) {
		assertThat(getText(selector)).contains(expected);
	}
	
	void waitUntil(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
