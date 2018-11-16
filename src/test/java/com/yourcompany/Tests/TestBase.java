package com.yourcompany.Tests;

import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.rules.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.yourcompany.util.*;
import com.saucelabs.junit.ConcurrentParameterized;

import java.net.URL;
import java.util.LinkedList;


@Ignore
@RunWith(ConcurrentParameterized.class)
public class TestBase {

    public static String seleniumURI;
    
    @Rule
    public TestName name = new TestName() {
        public String getMethodName() {
            return String.format("%s", super.getMethodName());
        }
    };

    protected String platformName;
    protected String platformVersion;
    protected String deviceName;
    protected WebDriver driver;
    protected String sessionId;
    private ResultReporter reporter;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce Device.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param platformName
     * @param platformVersion
     * @param deviceName
     */

    public TestBase(String platformName, String platformVersion, String deviceName) {
        super();
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.deviceName = deviceName;
    }

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();

        // Dynamic Allocation
        browsers.add(new String[]{"Android", "8.0", ""});
        browsers.add(new String[]{"iOS", "12.1", ""});
        
        //Static Allocation
        browsers.add(new String[]{"Android", "", "Google_Pixel_2_real"});
        browsers.add(new String[]{"iOS", "", "iPad_2_16GB_real"});
		
		//The above is 24 different browser configs, so 96 tests will run
		        
        return browsers;
    }

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("testobject_api_key", System.getenv("TESTOBJECT_API_KEY"));
        if(platformVersion != "")
            capabilities.setCapability("platformVersion", platformVersion);
        if(platformName != "")
            capabilities.setCapability("platformName", platformName);
        if(deviceName != "")
            capabilities.setCapability("deviceName", deviceName);

        String methodName = name.getMethodName();
        capabilities.setCapability("name", methodName);

        //Getting the build name.
        //Using the Jenkins ENV var. You can use your own. If it is not set test will run without a build id.
        this.driver = new RemoteWebDriver(
                new URL("https://" + seleniumURI +"/wd/hub"),
                capabilities);

        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
    }

    // Reports Pass/Fail status and ends the session
    @Rule
    public TestWatcher watchman= new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
        	reporter = new ResultReporter();
            String sessionId = getSessionId();

            reporter.saveTestStatus(sessionId, false);
            driver.quit();
        }

        @Override
        protected void succeeded(Description description) {
        	reporter = new ResultReporter();
        	String sessionId = getSessionId();

            reporter.saveTestStatus(sessionId, true);
            driver.quit();
           }
       };

    /**
     * @return the value of the Sauce Job id.
     */
    public String getSessionId() {
        return sessionId;
    }

    @BeforeClass
    public static void setupClass() {
        //get the uri to send the commands to.
        seleniumURI = "us1.appium.testobject.com:443";
        //You can set this manually on manual runs.
        }
    }

