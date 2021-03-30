package com.wp.util;

import android.net.Network;
import android.os.Build;
import android.util.Log;

import com.wp.imsiauthsdk.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtils {
    /**
     * 数据流
     * @param inputStream
     * @return
     */
    private static String dealResponseResult(InputStream inputStream) {
        String resultData;ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] data = new byte[1024];int len;
        try {
            while ((len = inputStream.read(data)) != -1)
                bos.write(data, 0, len);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(bos.toByteArray());
        return resultData;
    }


    /**
     * url获取
     *
     * @param urlString
     * @return
     */
    private static URL getValidateURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (Exception e) {

        }
        return null;
    }

    public static String preCTCCAuth(String string, Network network, int connectTimeOut,int readTimeOut) {
        URL url = HttpUtils.getValidateURL(string);
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            if (network!=null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                httpsURLConnection = (HttpsURLConnection) network.openConnection(url);
            }
            httpsURLConnection.setConnectTimeout(connectTimeOut);
            httpsURLConnection.setReadTimeout(readTimeOut);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.connect();
            int responseCode = httpsURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream intStream = httpsURLConnection.getInputStream();
                return HttpUtils.dealResponseResult(intStream);
            }
            String location = "";
            if(responseCode == Constants.HTTP_RESTART){
                location = httpsURLConnection.getHeaderField("Location");
            }
            int repeatNum = 0;
            while(responseCode == Constants.HTTP_RESTART && repeatNum<=2){
                repeatNum++;
                URL aURL = new URL(location);
                if ("https".equalsIgnoreCase(aURL.getProtocol())) {
                    HttpsURLConnection httpsConn = (HttpsURLConnection) aURL.openConnection();
                    httpsConn.setConnectTimeout(connectTimeOut);
                    httpsConn.setReadTimeout(readTimeOut);
                    responseCode = httpsConn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream intStream = httpsConn.getInputStream();
                        return HttpUtils.dealResponseResult(intStream);
                    } else {
                        location = httpsConn.getHeaderField("Location");
                        LogUtil.log( "url:" + location + ", responseCode:" + responseCode);
                    }
                }else{
                    HttpURLConnection httpConn = (HttpURLConnection) new URL(location).openConnection();
                    httpConn.setConnectTimeout(connectTimeOut);
                    httpConn.setReadTimeout(readTimeOut);
                    responseCode = httpConn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream intStream = httpConn.getInputStream();
                        return HttpUtils.dealResponseResult(intStream);
                    } else {
                        location = httpConn.getHeaderField("Location");
                        LogUtil.log( "url:" + location + ", responseCode:" + responseCode);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.log( "url:" + string + ", error:" + e.toString());
            return "url:" + string + ", error:" + e.toString();
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return null;
    }
}
