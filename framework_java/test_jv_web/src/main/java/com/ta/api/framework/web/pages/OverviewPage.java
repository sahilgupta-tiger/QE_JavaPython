package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.GenericWrapper;

public class OverviewPage extends GenericWrapper {


    public OverviewPage orderconfimation() throws InterruptedException {
        Thread.sleep(2000);
        clickByXpath(prop.getProperty("Overviewpage.finish.Xpath"));
        return this;
    }
}
