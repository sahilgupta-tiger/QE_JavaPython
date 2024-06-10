package com.ta.api.framework.web.pages;

import com.ta.api.framework.web.wrappers.GenericWrapper;

public class ProductSelectionPage extends GenericWrapper {


    public ProductSelectionPage addtoCart() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("ProductSelectionPage.addtocart.Xpath"));
        return this;
    }

    public ProductSelectionPage shippingcart() throws InterruptedException {
        Thread.sleep(1000);
        clickByXpath(prop.getProperty("ProductSelectionPage.shippingcart.Xpath"));
        return this;
    }
    public ProductSelectionPage checkout() throws InterruptedException {
        Thread.sleep(2000);
        clickByXpath(prop.getProperty("ProductSelectionPage.basket.Xpath"));
        return this;
    }

}
