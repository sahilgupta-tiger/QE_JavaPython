package com.ta.api.framework.mobile.wrappers;

public interface Wrapper {
    public void invokeApp(String browser, String url, boolean remote);

    public void enterById(String idValue, String data);

    public void enterByName(String nameValue, String data);

    public void enterByXpath(String xpathValue, String data);

    public void selectVisibileTextById(String id, String value);

    public void selectVisibileTextByName(String name, String value);

    public void selectVisibileTextByXPath(String xpathVal, String value);

    public void selectIndexById(String id, int value);

    public void selectIndexByName(String name, int value);

    public void selectIndexByXPath(String xpathVal, int value);

    public void selectValueById(String id, int value);

    public void selectValueByName(String name, int value);

    public void selectValueByXPath(String xpathVal, int value);

    public void closeBrowser();

    public void closeAllBrowsers();

    public void clickById(String id);

    public void clickByClassName(String classVal);

    public void clickByName(String name);

    public void clickByLink(String name);

    public void clickByLinkNoSnap(String name);

    public void clickByXpath(String xpathVal);

    public void clickByXpathNoSnap(String xpathVal);

    public String getTextById(String idVal);

    public String getTextByName(String name);

    public String getTextByClassName(String name);

    public String getTextByXpath(String xpathVal);

    public void acceptAlert();

    public void enterAlert(String text);

    public void dismissAlert();

    public String getAlertText();

    public void switchToParentWindow();

    public void switchToLastWindow();

    public void switchToWindowWithConditions(String text);

    public void verifyTextById(String id, String text);

    public void verifyTextByName(String name, String text);

    public void verifyTextByXpath(String xpath, String text);

    public void verifyTextContainsByXpath(String xpath, String text);

    public void verifyTitle(String title);

    public void clickOnTabById(String id);

    public void clickOnTabByXpath(String xpathVal);

    public void pagedown();

    public void webDriverWaitForAlertIsPresent();

    public void webdriverWaitPresenceOfElementByXpath(String xpathval);

    public void webdriverWaitElementToBeClickable(String xpathval);

    public void webdriverWaitSwitchToFrame(String xpathval);

    public void scrolldownByPixcel();

    public void scrolldownByElementId(String element, String xpathval);

    public void scrolldownTillEnd();

    public void fileUpload(String xpathval, String filepath);

    public void mouseHover(String xpathval);

    public void selectDayOfMonth(String xpathval);
}

