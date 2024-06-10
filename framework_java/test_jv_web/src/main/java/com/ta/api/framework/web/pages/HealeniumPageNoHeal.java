package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.GenericWrapper;

public class HealeniumPageNoHeal extends GenericWrapper {


    public HealeniumPageNoHeal Verify(){
        getTextByXpath("//*[@id='change_id']");
        return this;
    }

    public HealeniumPageNoHeal fieldOne(String data) {
        System.out.println(data);
        //enterById("change_id", data);
        enterByXpath("//*[@id='change_id']",data);
        return this;
    }
    public HealeniumPageNoHeal fieldTwo(String data) {
        enterByXpath("//*[@class='test_class']", data);
        return this;
    }

    public HealeniumPageNoHeal Changelocator() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath("//*[@id=\"Submit\"]/span/i");

        return this;
    }

}
