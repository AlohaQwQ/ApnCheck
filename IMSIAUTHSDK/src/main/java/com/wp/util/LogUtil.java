package com.wp.util;

import android.util.Log;

public class LogUtil {

    /**
     * @author Aloha
     * @date 2021/2/3 13:57
     * @explain 开启log
     */
    public static boolean DEBUG = true;

    public static final String TAG = "imsiauthsdk";

    public static void log(String log){
        if(DEBUG)
            Log.e(TAG,log);
    }
}
