package com.ta.api.framework.mobile.pages;

import com.ta.api.framework.mobile.wrappers.MobileGenericWrapper;

public class OverviewPage extends MobileGenericWrapper {


    public OverviewPage orderconfimation() throws InterruptedException {
        Thread.sleep(2000);
        clickByXpath(prop.getProperty("Overviewpage.finish.Xpath"));
        return this;
    }
}
