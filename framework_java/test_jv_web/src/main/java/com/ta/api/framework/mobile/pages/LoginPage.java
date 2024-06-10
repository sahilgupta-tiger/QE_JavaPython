package com.ta.api.framework.mobile.pages;

import com.ta.api.framework.mobile.wrappers.MobileGenericWrapper;


public class LoginPage extends MobileGenericWrapper {


    public LoginPage verifyLogin(){
        getTextByXpath(prop.getProperty("LoginPage.VerifyLogin.Xpath"));
        return this;
    }
    public LoginPage enterUsername(String data) {
        enterByXpath(prop.getProperty("LoginPage.username.Xpath"), data);
        return this;
    }

    public LoginPage enterPassword(String data) {
        enterByXpath(prop.getProperty("LoginPage.password.Xpath"), data);
        return this;
    }
    public LoginPage clickLogin() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("LoginPage.login.Xpath"));
        return this;
    }

}
