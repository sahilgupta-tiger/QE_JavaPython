package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.SelfHealGenericWrapper;

public class HealeniumPage extends SelfHealGenericWrapper {


    public HealeniumPage Verify(){
        getTextByXpath("//*[@id='change_id']");
        return this;
    }

    public HealeniumPage fieldOne(String data) {
        System.out.println(data);
        //enterById("change_id", data);
        enterByXpath("//*[@id='change_id']",data);
        return this;
    }
    public HealeniumPage fieldTwo(String data) {
        enterByXpath("//*[@class='test_class']", data);
        return this;
    }


    public HealeniumPage Changelocator() throws InterruptedException {
        Thread.sleep(1000);
        PageRefresh();
        clickByXpath("//*[@id=\"Submit\"]/span/i");
        return this;


        //clearfiled("//*[@id='change_id']");
        // clearfiled("//*[@class='test_class']");
    }

}
