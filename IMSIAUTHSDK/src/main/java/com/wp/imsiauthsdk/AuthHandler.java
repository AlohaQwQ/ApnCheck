package com.wp.imsiauthsdk;

public class AuthHandler {

    private static AuthHandler authHandler = null;
    private android.content.Context _context = null;

    private AuthHandler(android.content.Context context){
        _context = context;
    }

    public static AuthHandler getInstance(android.content.Context context){
        if(authHandler==null){
            authHandler = new AuthHandler(context);
        }
        return authHandler;
    }

    public void registerAppId(String appId,String appKey,String custId,String mobileNetwork){
        if("CM".equals(mobileNetwork)){
            Constants.CM_APP_ID = appId;
            Constants.CM_APP_KEY = appKey;
            Constants.CM_CUSTID = custId;
        }
        if("CU".equals(mobileNetwork)){
            Constants.CU_APP_ID = appId;
            Constants.CU_APP_KEY = appKey;
        }
        if("CT".equals(mobileNetwork)){
            Constants.CT_APP_ID = appId;
            Constants.CT_APP_KEY = appKey;
        }
    }
    public void requestAccessCode(String mobileNetwork,AccessCodeListener listener) throws Exception{
        AccessCodeRequestUtil.getNetState(_context,mobileNetwork,listener);
    }
}
