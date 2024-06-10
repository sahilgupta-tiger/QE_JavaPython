package com.ta.api.framework.mobile.pages;

import com.ta.api.framework.mobile.wrappers.MobileGenericWrapper;


public class BacktoOrderSectionPage extends MobileGenericWrapper {



    public BacktoOrderSectionPage backtoorder() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("BacktoOrderSectionPage.backhome.Xpath"));
        return this;
    }


}
