package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.GenericWrapper;

public class LoginPage extends GenericWrapper {


    public LoginPage verifyLogin(){
        getTextByXpath(prop.getProperty("LoginPage.VerifyLogin.Xpath"));
        return this;
    }
    public LoginPage enterUsername(String data) {
        enterByXpath(prop.getProperty("LoginPage.username.Xpath"), data);
        return this;
    }

    public LoginPage enterPsssword(String data) {
        enterByXpath(prop.getProperty("LoginPage.password.Xpath"), data);
        return this;
    }
    public LoginPage clickLogin() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("LoginPage.login.Xpath"));
        return this;
    }

}
