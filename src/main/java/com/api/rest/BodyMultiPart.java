package com.api.rest;

import java.util.ArrayList;
import java.util.List;

public class BodyMultiPart {

    private List<MultiPartInfo> multiPartInfos = new ArrayList();

    public BodyMultiPart() {
    }

    public void add(String key, String value, MultiPartType type) {
        MultiPartInfo multiPartInfo = new MultiPartInfo();
        multiPartInfo.setKey(key);
        multiPartInfo.setValue(value);
        multiPartInfo.setType(type);
        this.multiPartInfos.add(multiPartInfo);
    }

    public List<MultiPartInfo> getMultiPartInfos() {
        return this.multiPartInfos;
    }
}
