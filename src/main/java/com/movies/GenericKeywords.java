package com.movies;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.util.Constants;

public class GenericKeywords {

	public WebDriver driver;
	public Properties prop;
	public String object;
	public String keyword;
	public String key;
	public String data;
	public String proceedOnFail;
	public Hashtable<String, String> testData;
	ExtentTest test;
	WebDriverWait wait;
	public String testCase;
	boolean openBrowser = false;
	boolean isWindows = true;
	String storedKeyword;

	int waitTimeUpperLimitInSeconds = 60;
	int counterTocheckError = 0;
	int maxLimit_counterTocheckError = 5; // To set max limit of exceptions before close browser.
	ArrayList<Long> averageLoadTime = new ArrayList<Long>();

	public GenericKeywords(ExtentTest test, String testCase) {
		System.out.println("Generic Start");
		this.test = test;
		this.testCase = testCase;

		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.Project_Properties);
			prop.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.ERROR, ExceptionUtils.getStackTrace(e));
		}

	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setkeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setTestData(Hashtable<String, String> testData) {
		this.testData = testData;
	}

	public String getStoredkeyword() {
		return storedKeyword;
	}

	public String getProceedOnFail() {
		return proceedOnFail;
	}

	public void setProceedOnFail(String proceedOnFail) {
		this.proceedOnFail = proceedOnFail;
	}

	public String getkeyword() {
		return keyword;
	}

	public String getObject() {
		return object;
	}

	public String getKey() {
		return key;
	}

	public String getData() {
		return data;
	}

	public Hashtable<String, String> getTestData() {
		return testData;
	}

	public void setDataFromSheet() {
		getData();
	}

	public void pause(String timeout) {
		try {
			Thread.sleep(Integer.parseInt(timeout));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean openBrowser() throws InterruptedException {
		try {
			openBrowser = true;
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				isWindows = true;
			} else {
				isWindows = false;
			}
			boolean headlessSession = Boolean.parseBoolean(prop.getProperty("enable_headless-session"));

			if (osName.contains("windows server")) // Bypassing headless flag for remote machine
				headlessSession = true;

			System.out.println("Opening Browser - " + data);
			test.log(LogStatus.INFO, "Opening Browser - " + data);

			if (data.contains("Mozilla")) {
				driver = openMozilla(osName, headlessSession);
			} else if (data.contains("Chrome"))
				driver = openChrome(osName, headlessSession);

			driver.manage().window().maximize();
			return true;
		} catch (Exception e) {
			System.err.println("Not able to open browser - " + e.getMessage());
			test.log(LogStatus.FAIL, "Not able to open browser - " + e.getMessage());
			test.log(LogStatus.ERROR, ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	public WebDriver openMozilla(String osName, boolean headless) {
		try {
			WebDriver localDriver;
			FirefoxOptions options = new FirefoxOptions();
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			options.addPreference("browser.tabs.remote.autostart", false);
			options.addPreference("dom.ipc.processCount", 1);
			options.addPreference("dom.disable_beforeunload", true);

			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("dom.webnotifications.enabled", false);
			profile.setPreference("browser.download.folderList", 2);
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			if (isWindows) {
				profile.setPreference("browser.download.dir", System.getProperty("user.dir") + "\\Downloads");
			} else {
				profile.setPreference("browser.download.dir", System.getProperty("user.dir") + "//Downloads");
			}
			profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"text/csv,application/x-msexcel,application/excel,application/octet-stream,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			profile.setPreference("browser.download.manager.alertOnEXEOpen", false);

			if (headless)
				options.setHeadless(true);

			if (osName.contains("windows")) {
				System.setProperty("webdriver.gecko.driver", Constants.geckodriver_Window);
			} else {
				System.setProperty("webdriver.gecko.driver", Constants.geckodriver);
			}

			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "null");
			options.setProfile(profile);
			localDriver = new FirefoxDriver(options);
			return localDriver;
		} catch (Exception e) {
			System.err.println("Not able to open browser - " + e.getMessage());
			test.log(LogStatus.FAIL, "Not able to open browser - " + e.getMessage());
			return null;
		}
	}

	public WebDriver openChrome(String osName, boolean headless) {
		try {
			WebDriver localDriver;
			ChromeOptions Chrome_Options = new ChromeOptions();
			Chrome_Options.addArguments("start-maximized");
			Chrome_Options.addArguments("--no-sandbox");
			Chrome_Options.addArguments("'--disable-web-security'");
			Chrome_Options.addArguments("--allow-running-insecure-content");

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("profile.default_content_setting_values.notifications", 2);
			if (isWindows) {
				chromePrefs.put("download.default_directory", System.getProperty("user.dir") + "\\Downloads");
			} else {
				chromePrefs.put("download.default_directory", System.getProperty("user.dir") + "//Downloads");
			}
			chromePrefs.put("credentials_enable_service", false);
			Chrome_Options.setExperimentalOption("prefs", chromePrefs);
			Chrome_Options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

			if (headless)
				Chrome_Options.addArguments("--headless", "--window-size=1920,1200");

			if (osName.contains("windows")) {
				System.setProperty("webdriver.chrome.driver", Constants.chromedriver_Window);
			} else {
				System.setProperty("webdriver.chrome.driver", Constants.chromedriver);
			}

			localDriver = new ChromeDriver(Chrome_Options);
			return localDriver;
		} catch (Exception e) {
			System.err.println("Not able to open browser - " + e.getMessage());
			test.log(LogStatus.FAIL, "Not able to open browser - " + e.getMessage());
			return null;
		}
	}

	public void closeBrowser() {
		if (openBrowser == true) {
			try {
				System.out.println("Closing Browser!");
				driver.quit();
				pause("200");
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER); // Additional Check - In case any dialog box is present.
				openBrowser = false;
			} catch (Exception e) {
				System.err.println("Not able to close browser - " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void takeScreenShot() {
		try {
			// decide name - time stamp
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
			String screenshotFile = formatter.format(d).toString().replace(":", "_").replace(" ", "_") + ".png";
			String path = Constants.SCREENSHOT_PATH_1 + screenshotFile;
			// take screenshot
			File srcFile = null;

			srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			try {
				FileUtils.copyFile(srcFile, new File(path));
			} catch (IOException e) {
				e.printStackTrace();
				test.log(LogStatus.ERROR, ExceptionUtils.getStackTrace(e));
			}

			// embed
			test.log(LogStatus.INFO, test.addScreenCapture(Constants.SCREENSHOT_PATH_2 + screenshotFile));
			System.out.println("Screenshot - " + Constants.SCREENSHOT_PATH_2 + screenshotFile);
		} catch (Throwable e) {
			System.err.println("Issue in taking screenshot\n" + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeCurrentSession() {
		if (counterTocheckError >= maxLimit_counterTocheckError) {
			System.out.println("Closing browser as exception max limit is reached : " + counterTocheckError);
			test.log(LogStatus.FAIL, "Closing browser as exception max limit is reached.");
			closeBrowser();
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert().accept();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void reportFailure(String failureMsg, Throwable e) {
		if (e.getMessage().contains("No last expected state to find old element in!")) {
			System.err.println("Master copy is not present !");
		} else {
			System.err.println("########### App Keyword: Failure\n" + failureMsg + " : " + e.getMessage() + "\n \n");
			e.printStackTrace();
			test.log(LogStatus.FAIL, failureMsg + "    >>>>    " + e.getMessage());
			test.log(LogStatus.FAIL, ExceptionUtils.getStackTrace(e));
			test.log(LogStatus.INFO, "Current URL : " + driver.getCurrentUrl());
			takeScreenShot();
		}
		if (isAlertPresent()) {
			driver.switchTo().alert().accept();
		}
		if (ExceptionUtils.getStackTrace(e).contains("TimeoutException")
				|| ExceptionUtils.getStackTrace(e).contains("NullPointerException")
				|| ExceptionUtils.getStackTrace(e).contains("NoSuchElementException")
				|| ExceptionUtils.getStackTrace(e).contains("ElementNotInteractableException")) {
			counterTocheckError++;
			System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> counterTocheckError : "
					+ counterTocheckError);
			closeCurrentSession();
		}
	}

	public void waitForPageLoad() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			int i = 0;
			pause("200");
			while (i < 600) {
				String state = (String) js.executeScript("return document.readyState;");

				if (state.equals("complete"))
					break;
				else
					pause("200");

				i++;
			}
			// check for jquery status
			i = 0;
			while (i < 600) {
				Long d = (Long) js.executeScript("return jQuery.active;");
				if (d.longValue() == 0)
					break;
				else
					pause("200");
				i++;

			}
		} catch (Throwable e) {

		}

	}

	public void assertTrue(boolean condition, String failureMsg) {
		Assert.assertTrue(condition, failureMsg);
	}

	public void assertFalse(boolean condition, String failureMsg) {
		Assert.assertFalse(condition, failureMsg);
	}

	public void assertEquals(Object actualResult, Object expectedResult, String failureMsg) {
		Assert.assertEquals(actualResult, expectedResult, failureMsg);
	}

	public void assertNotEquals(Object actualResult, Object expectedResult, String failureMsg) {
		Assert.assertNotEquals(actualResult, expectedResult, failureMsg);
	}

	public void navigate(String urlKey) {
		try {
			System.out.println("Navigating to " + prop.getProperty(urlKey));
			driver.get(prop.getProperty(urlKey));
		} catch (Throwable e) {
			reportFailure("Not able to navigate : " + urlKey, e);
		}
	}

	public WebElement getElement(String locatorKey) {
		WebElement element = null;
		try {
			wait = new WebDriverWait(driver, waitTimeUpperLimitInSeconds, 100);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(locatorKey))));
			element = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		} catch (Exception e) {
			reportFailure("Not able to find element : " + locatorKey, e);
		}
		return element;
	}

	public void input(String locatorKey, String data) {
		try {
			System.out.println("Entering: " + data + " into: " + locatorKey);
			WebElement e = getElement(locatorKey);
			e.sendKeys(data);
		} catch (Exception e) {
			System.err.println("Not able to input : " + locatorKey);
			e.printStackTrace();
		}
	}

	public void waitforElementToBeClickable(String locatorKey) {
		try {
			wait = new WebDriverWait(driver, waitTimeUpperLimitInSeconds, 100);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(locatorKey))));
		} catch (Exception e) {
			reportFailure("Wait times up !! - Waiting for Clickable element " + locatorKey, e);
		}
	}

	public void click(String locatorKey) {
		try {
			WebElement e = getElement(locatorKey);
			waitforElementToBeClickable(locatorKey);
			e.click();
		} catch (Exception e) {
			System.err.println("Not able to click : " + object);
			e.printStackTrace();
		}
	}

	public void clickAndInput(String locatorKey, String data) {
		try {
			System.out.println("Clicking on:" + locatorKey + " and entering data: " + data);
			WebElement e = getElement(locatorKey);
			click(locatorKey);
			e.sendKeys(data);
		} catch (Exception e) {
			System.err.println("Not able to click and input : ");
			e.printStackTrace();
		}
	}

	public void wait(String locatorKey) {
		try {
			wait = new WebDriverWait(driver, waitTimeUpperLimitInSeconds, 160);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(locatorKey))));
		} catch (Exception e) {
			reportFailure("Wait timeout! Element: " + locatorKey + " is not present. ", e);
		}
	}

	public void scrollToMid(String Locator) {
		try {
			System.out.println("Scrolling " + Locator + " to middle of the screen.");
			((JavascriptExecutor) driver)
					.executeScript("window.scrollTo(0," + (getElement(Locator).getLocation().y - 120) + ")");
		} catch (Throwable e) {
			System.err.println("Issue with scrolling - " + Locator);
			e.printStackTrace();
		}
	}
	
	public String returnCorrectDateFormat(String imdbDate) throws ParseException {
		
		DateFormat originalFormat = new SimpleDateFormat("MMMMM dd,yyyy", Locale.ENGLISH);
		DateFormat formattedDate = new SimpleDateFormat("dd MMMMM yyyy");
		Date date = originalFormat.parse(imdbDate.split("\\(")[0].trim());
		String locImdbDateCorrected = formattedDate.format(date);
		System.out.println(locImdbDateCorrected);
		return locImdbDateCorrected;
		
		
	}

}
