package com.wp.util;

import android.os.Build;
import android.util.Log;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <h1>加密工具类</h1>
 * @Date 2018年12月13日
 */
public class CipherUtil {

    /**
     * md5加密
     * @param target 待加密字符串
     * @return 加密后的MD5字符串
     * @throws Exception 异常
     */
    public static String encryptLowerMd5(String target) throws Exception {
        if (target == null || target.length() <= 0) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return byte2Lowerhex(md5.digest(target.getBytes("utf-8")));
    }

    public static String encryptMd5(String target) throws Exception {
        if (target == null || target.length() <= 0) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return byte2hex(md5.digest(target.getBytes("utf-8")));
    }


    /**
     * 加密
     * @param target 加密对象
     * @param sKey   加密密钥
     * @return 加密后的字符串
     * @throws Exception 异常
     */
    public static String encryptAes(String target, String sKey) throws Exception {
        if (target == null || target.length() <= 0) {
            return null;
        }
        // 判断Key是否正确
        if (sKey == null || sKey.length() == 0) {
            throw new IllegalArgumentException("illegal sKey");
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new IllegalArgumentException("illegal sKey");
        }
        byte[] raw = sKey.getBytes("UTF-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(target.getBytes());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LogUtil.log("hex : " + Base64.getEncoder().encodeToString(encrypted));
        }
        return byte2hex(encrypted);
    }

    /**
     * 生产Authorization
     * @param appId  解密对象
     * @param appKey 加密密钥
     * @return 解密后的原数据
     * @throws Exception 异常
     */
    public static String generateAuthorization(String appId, String appKey) throws Exception {
        if (appId == null || appId.length() == 0) {
            throw new IllegalArgumentException("illegal appId");
        }
        if (appKey == null || appKey.length() == 0) {
            throw new IllegalArgumentException("illegal appKey");
        }
        // 生产AES密钥
        String aesKey = encryptLowerMd5(appKey).substring(3, 19);
        LogUtil.log("aesKey: " + aesKey);
        // 获取当前时间300秒后失效
        long currTime = System.currentTimeMillis();
        LogUtil.log("timeSp: " + currTime);
        // AES最终的加密
        LogUtil.log("tempAppId : " + appId + currTime);
        return encryptAes(appId + currTime, aesKey);

    }
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString();
    }
    // 二行制转字符串
    private static String byte2Lowerhex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * @param args 参数
     * @throws Exception 异常
     */
    /*public static void main(String[] args) throws Exception {
        // 校验值
        // cc4ca20205d56ea2e408881b688da2bb
        Log.e(com.wp.util.Constants.TAG,encryptLowerMd5("18812345678"));
        // 校验值
        String appId = "aaa";
        String appKey = "aaa";
        // 由于时间实时变化无校验值Authorization有效期时间为300秒
        Log.e(com.wp.util.Constants.TAG,generateAuthorization(appId, appKey));
    }*/
}
