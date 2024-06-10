package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.GenericWrapper;

public class Informationpage extends GenericWrapper {



    public Informationpage enterfirstname(String data){
        enterByXpath(prop.getProperty("Informationpage.firstname.Xpath"),data);
        return this;
    }

    public Informationpage enterlastname(String data) throws InterruptedException {
        Thread.sleep(1000);
        enterByXpath(prop.getProperty("Informationpage.lastname.Xpath"),data);
        return this;
    }

    public Informationpage enterzipcode(String data) throws InterruptedException {
        Thread.sleep(1000);
        enterByXpath(prop.getProperty("Informationpage.zipcode.Xpath"),data);
        return this;
    }

    public Informationpage clickContinue() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("Informationpage.continue.Xpath"));
        return this;
    }

}
