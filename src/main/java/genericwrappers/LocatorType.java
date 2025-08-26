package genericwrappers;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public enum LocatorType {
    RESOURCEID {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.androidUIAutomator(String.format("new UiSelector().resourceId(\"%s\")", locatorValue));
        }
    },
    TEXT {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.androidUIAutomator(String.format("new UiSelector().text(\"%s\")", locatorValue));
        }
    },
    CONTENT_DESC {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.androidUIAutomator(String.format("new UiSelector().description(\"%s\")", locatorValue));
        }
    },
    ACCESSIBILITY_ID {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.accessibilityId(locatorValue);
        }
    },
    XPATH {
        @Override
        By getBy(String locatorValue) {
            return By.xpath(locatorValue);
        }
    };

    abstract By getBy(String locatorValue);
}
