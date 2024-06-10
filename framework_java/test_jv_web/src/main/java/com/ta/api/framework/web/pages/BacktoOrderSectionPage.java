package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.GenericWrapper;

public class BacktoOrderSectionPage extends GenericWrapper {



    public BacktoOrderSectionPage backtoorder() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("BacktoOrderSectionPage.backhome.Xpath"));
        return this;
    }


}
