package com.yourcompany.Tests;

import com.yourcompany.Pages.*;
import org.junit.Test;
import org.openqa.selenium.InvalidElementStateException;

import static org.junit.Assert.*;

public class FollowLinkTest extends TestBase {

    public FollowLinkTest(String platformName, String platformVersion, String deviceName) {
            super(platformName, platformVersion, deviceName);
    }

    /**
     * Runs a simple test verifying link can be followed.
     * @throws InvalidElementStateException
     */
    @Test
    public void verifyLinkTest() throws InvalidElementStateException {
        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        page.followLink();

        assertFalse(page.isOnPage());
    }
    @Test
    public void verifyLinkTest2() throws InvalidElementStateException {
        GuineaPigPage page = GuineaPigPage.visitPage(driver);

        page.followLink();

        assertFalse(page.isOnPage());
    }
}