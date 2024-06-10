package com.ta.api.framework.mobile;

import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.mobileutils.ObjectRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SampleMobileCommonActions extends ApiCore {

    private static final Logger logger = LogManager.getLogger(SampleMobileCommonActions.class);
    protected static APICommonActions api;
    protected CommonPojo commonPojo;
    protected static CommonUtils commonUtils;
    protected ObjectRepository objectRepository;

    public SampleMobileCommonActions(CommonPojo commonPojo) {
        this.commonPojo = commonPojo;
        api = new APICommonActions(commonPojo);
        commonUtils = new CommonUtils();
        objectRepository = new ObjectRepository();
    }

    public void LogintoApplication(Map<String,Object> map) throws InterruptedException {
        Thread.sleep(7000);
        objectRepository.loginPage().enterUsername(map.get("username").toString());
        objectRepository.loginPage().enterPassword(map.get("password").toString());
        objectRepository.loginPage().clickLogin();
    }

    public void SelectTheProduct(Map<String,Object> map) throws InterruptedException {
        objectRepository.productselectionpage().addtoCart();
        objectRepository.productselectionpage().shippingcart();


    }
    public void Checkout(Map<String,Object> map) throws InterruptedException {
        objectRepository.productselectionpage().checkout();

    }
    public void ProvideShippingDetails(Map<String,Object> map) throws InterruptedException {
        objectRepository.informationpage().enterfirstname(map.get("firstname").toString());
        objectRepository.informationpage().enterlastname(map.get("lastname").toString());
        objectRepository.informationpage().enterzipcode(map.get("zipcode").toString());
        objectRepository.informationpage().clickContinue();

    }
    public void OverviewofOrder(Map<String,Object> map) throws InterruptedException {
        objectRepository.overviewpage().orderconfimation();
    }

    public void MovebacktoHomeScreen(Map<String,Object> map) throws InterruptedException {
        objectRepository.backtoorderectionpage().backtoorder();

    }
    public void verify() {
        String a = String.valueOf(objectRepository.loginPage().verifyLogin());
        System.out.println("*******a = " +a.toString());
        List<String> sr = Collections.singletonList("PASS");
        commonPojo.setTestStatus(sr);
    }


}
