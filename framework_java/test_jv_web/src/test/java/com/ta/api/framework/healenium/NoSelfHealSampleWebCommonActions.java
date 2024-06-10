package com.ta.api.framework.healenium;

import com.ta.api.framework.apicoreutils.APICommonActions;
import com.ta.api.framework.apicoreutils.ApiCore;
import com.ta.api.framework.helperfunctions.CommonUtils;
import com.ta.api.framework.pojo.CommonPojo;
import com.ta.api.framework.webutils.ObjectRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NoSelfHealSampleWebCommonActions extends ApiCore {

    private static final Logger logger = LogManager.getLogger(NoSelfHealSampleWebCommonActions.class);
    protected static APICommonActions api;
    protected CommonPojo commonPojo;
    protected static CommonUtils commonUtils;
    public ObjectRepository objectRepository;

    public NoSelfHealSampleWebCommonActions(CommonPojo commonPojo) {
        this.commonPojo = commonPojo;
        api = new APICommonActions(commonPojo);
        commonUtils = new CommonUtils();
        objectRepository = new ObjectRepository();
    }

    public void HealeniumApplicationchecksOldLocator(Map<String, Object> map) throws InterruptedException {
        System.out.println(map.get("Fieldone").toString());
        objectRepository.healeniumPageNoHealage().fieldOne(map.get("Fieldone").toString());
        objectRepository.healeniumPageNoHealage().fieldTwo(map.get("Fieldtwo").toString());
        objectRepository.healeniumPageNoHealage().Changelocator();
    }

    public void HealeniumApplicationchecksNewLocator(Map<String, Object> map) throws InterruptedException {
        objectRepository.healeniumPageNoHealage().fieldOne(map.get("FieldoneNew").toString());
        objectRepository.healeniumPageNoHealage().fieldTwo(map.get("FieldtwoNew").toString());

    }

    public void verify() {
        String a = String.valueOf(objectRepository.healeniumPageNoHealage().Verify());
        System.out.println("*******a = " +a.toString());
        List<String> sr = Collections.singletonList("FAIL");
        commonPojo.setTestStatus(sr);
    }

}
