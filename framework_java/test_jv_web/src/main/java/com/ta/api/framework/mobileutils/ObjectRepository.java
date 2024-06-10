package com.ta.api.framework.mobileutils;

import com.ta.api.framework.mobile.wrappers.MobileGenericWrapper;
import com.ta.api.framework.mobile.pages.*;

public class ObjectRepository {

    public LoginPage loginPage() {  return new LoginPage(); }
    public ProductSelectionPage productselectionpage(){
        return new ProductSelectionPage();
    }

    public Informationpage informationpage(){
        return new Informationpage();
    }

    public OverviewPage overviewpage(){
        return new OverviewPage();
    }


    public BacktoOrderSectionPage backtoorderectionpage(){
        return new BacktoOrderSectionPage();
    }


    public MobileGenericWrapper mobileGenericWrapper(){
        return new MobileGenericWrapper();
    }


}
