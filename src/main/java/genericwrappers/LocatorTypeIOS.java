package genericwrappers;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public enum LocatorTypeIOS {
    NAME("name") {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.iOSNsPredicateString(String.format("name == \"%s\"", locatorValue));
        }
    },
    VALUE("value") {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.iOSNsPredicateString(String.format("value == \"%s\"", locatorValue));
        }
    },
    LABEL("label") {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.iOSNsPredicateString(String.format("label == \"%s\"", locatorValue));
        }
    },
    CONTENT_DESC("accessibility id") {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.accessibilityId(locatorValue);
        }
    },
    ACCESSIBILITY_ID("accessibility id") {
        @Override
        By getBy(String locatorValue) {
            return AppiumBy.accessibilityId(locatorValue);
        }
    },
    XPATH("xpath") {
        @Override
        By getBy(String locatorValue) {
            return By.xpath(locatorValue);
        }
    };

    // Field to store the scroll strategy
    private final String scrollStrategy;

    // Constructor to initialize scroll strategy
    LocatorTypeIOS(String scrollStrategy) {
        this.scrollStrategy = scrollStrategy;
    }

    // Abstract method to be implemented by each enum constant
    abstract By getBy(String locatorValue);

    // Utility method to retrieve the locator
    public static By getLocator(LocatorTypeIOS type, String locatorValue) {
        return type.getBy(locatorValue);
    }

    // Getter for the scroll strategy
    public String getScrollStrategy() {
        return scrollStrategy;
    }
}