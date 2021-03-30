package com.wp.imsiauthsdk;

import java.util.Map;

public abstract class AccessCodeListener {
    public abstract void onRequestAccessCodeComplete(Map<String,String> resultMap);
}
