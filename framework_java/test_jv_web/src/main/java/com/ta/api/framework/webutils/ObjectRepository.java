package com.ta.api.framework.webutils;

import com.ta.api.framework.web.pages.*;
import com.ta.api.framework.web.wrappers.GenericWrapper;
import com.ta.api.framework.web.wrappers.SelfHealGenericWrapper;

public class ObjectRepository  {


    public LoginPage loginPage() {
        return new LoginPage();
    }
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

    public HealeniumPage healeniumPage(){
        return new HealeniumPage();
    }

    public HealeniumPageNoHeal healeniumPageNoHealage(){
        return new HealeniumPageNoHeal();
    }


    public GenericWrapper genericWrapper(){
        return new GenericWrapper();
    }

    public SelfHealGenericWrapper selfHealGenericWrapper(){
        return  new SelfHealGenericWrapper();
    }


}
