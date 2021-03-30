package com.wp.imsiauthsdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.umcrash.UMCrash;
import com.wp.util.AesUtils;
import com.wp.util.CipherUtil;
import com.wp.util.HttpUtils;
import com.wp.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AccessCodeRequestUtil {

    private static int NETWORK_TYPE;

    private static final int PERMISSION_REQ_ID = 22;

    /**
     * @author Aloha
     * @date 2021/2/3 20:25
     * @explain 低版本api 避免多次请求标志位
     */
    private static boolean returnSign;

    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.CHANGE_WIFI_STATE};

    public static String getNetState(final Context context, final String mobileNetwork, final AccessCodeListener listener) throws Exception {
        /**
         * @author Aloha
         * @date 2021/1/30 23:08
         * @explain 支持android5.0 以上系统
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isNetworkConnected(context)) {
                returnSign = true;
                UMConfigure.init(context, "60157579f1eb4f3f9b7db568", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
                UMConfigure.setLogEnabled(true);

                if (getMobileDataState(context)) {
                    LogUtil.log( "数据流量已开启");
                    final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkRequest.Builder builder = new NetworkRequest.Builder();
                    /**
                     * @author Aloha
                     * @date 2021/1/30 18:14
                     * @explain 强制使用蜂窝数据网络-移动数据
                     */
                    builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                    builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
                    NetworkRequest request = builder.build();

                    ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onAvailable(Network network) {
                            super.onAvailable(network);
                            if(returnSign){
                                if (checkSelfPermission(context,REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(context,REQUESTED_PERMISSIONS[1],PERMISSION_REQ_ID) &&
                                        checkSelfPermission(context,REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(context,REQUESTED_PERMISSIONS[3], PERMISSION_REQ_ID) &&
                                        checkSelfPermission(context,REQUESTED_PERMISSIONS[4], PERMISSION_REQ_ID)) {

                                    final Map<String, String> resultMap = new HashMap<String, String>();
                                    if ("CT".equals(mobileNetwork)) {
                                        parseResult(preCTCCAuth(network), mobileNetwork, resultMap);
                                    } else if ("CU".equals(mobileNetwork)) {
                                        parseResult(preCUCCAuth(network), mobileNetwork, resultMap);
                                    } else if ("CM".equals(mobileNetwork)) {
                                        parseResult(preCMCCAuth(network), mobileNetwork, resultMap);
                                    } else {
                                        resultMap.put("code", "00011");
                                        resultMap.put("accessCode", "");
                                        resultMap.put("msg", "未知运营商");
                                    }
                                    returnSign = false;
                                    connectivityManager.unregisterNetworkCallback(this);
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onRequestAccessCodeComplete(resultMap);
                                        }
                                    });
                                } else {
                                    LogUtil.log( "无网络权限");
                                    Toast.makeText(context, "请允许网络权限", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                            super.onCapabilitiesChanged(network, networkCapabilities);
                            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                    LogUtil.log( "WIFI已连接");
                                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                    LogUtil.log( "GPRS已连接");
                                } else {
                                    LogUtil.log( "其他网络");
                                }
                            }
                        }
                    };
                    connectivityManager.registerNetworkCallback(request, callback);
                    connectivityManager.requestNetwork(request, callback);
                } else {
                    LogUtil.log( "数据流量未开启");
                    Toast.makeText(context, "请开启数据流量", Toast.LENGTH_SHORT).show();
                }
            } else {
                LogUtil.log( "无网络连接");
                Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    /**
     * @author Aloha
     * @date 2021/2/3 21:59
     * @explain 权限检测
     */
    public static boolean checkSelfPermission(Context context, String permission, int requestCode) {
        LogUtil.log( "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,REQUESTED_PERMISSIONS,requestCode);
            return false;
        }
        return true;
    }

    /**
     * @author Aloha
     * @date 2021/1/31 22:38
     * @explain 判断是否有网络连接,并赋值网络类型
     * ConnectivityManager.TYPE_NONE -1
     * ConnectivityManager.TYPE_MOBILE 0
     * ConnectivityManager.TYPE_WIFI 1
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                NETWORK_TYPE = mNetworkInfo.getType();
                return mNetworkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * @author Aloha
     * @date 2021/1/30 18:07
     * @explain 判断手机数据流量是否开启
     * true 开启 false 未开启
     */
    public static boolean getMobileDataState(Context context) throws Exception{
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /**
         * @author Aloha
         * @date 2021/1/30 17:42
         * @explain 检测流量是否开启
         */
        if (mConnectivityManager != null) {
            if (NETWORK_TYPE == ConnectivityManager.TYPE_MOBILE){
                return true;
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Class mClass = mConnectivityManager.getClass();
                Method method = mClass.getMethod("getMobileDataEnabled");
                return (boolean) method.invoke(mConnectivityManager);
                /*try {
                    Class mClass = mConnectivityManager.getClass();
                    Method method = mClass.getMethod("getMobileDataEnabled");
                    return (boolean) method.invoke(mConnectivityManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    return false;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return false;
                }*/
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 开启手机移动数据
     @param context
     @return
     **/
    public static void openMobileData(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    /*Method setMobileDataEnabledMethod = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
                    if (null != setMobileDataEnabledMethod) {
                        setMobileDataEnabledMethod.invoke(telephonyManager, true);
                    }*/
                    //telephonyManager.setDataEnabled(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @author Aloha
     * @date 2021/2/3 21:59
     * @explain 中国电信预验证
     */
    private static String preCTCCAuth(Network network) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String sign = AesUtils.encryptAesNew(Constants.CT_APP_ID + timeStamp, Constants.CT_APP_KEY.substring(3,19));
        String url = Constants.CT_PREAUTH_URL + "?appId=" + Constants.CT_APP_ID + "&timeStamp=" + timeStamp + "&sign=" + sign;
        String result = HttpUtils.preCTCCAuth(url, network,8000,5000);
        return result;
    }

    /**
     * @author Aloha
     * @date 2021/2/3 22:00
     * @explain 中国联通预验证
     */
    private static String preCUCCAuth(Network network) {
        StringBuffer input = new StringBuffer();
        long now = System.currentTimeMillis();
        input.append("client_id=").append(Constants.CU_APP_ID);
        input.append("&client_type=30010");
        input.append("&format=jsonp");
        input.append("&timeStamp=").append(now);
        input.append("&version=2.0");
        try {
            String ency = CipherUtil.encryptMd5(Constants.CU_APP_ID+"30010jsonp"+now+"2.0");
            input.append("&sign=").append(CipherUtil.encryptMd5(Constants.CU_APP_ID+"30010jsonp"+now+"2.0"));
        } catch (Exception e) {
            e.printStackTrace();
            UMCrash.generateCustomLog(e, "UmengException");
        }
        final StringBuilder result = new StringBuilder();
        BufferedReader br = null;InputStreamReader isr = null;OutputStream osm = null;
        HttpsURLConnection conn = null;
        try {
            URL geturl = new URL(Constants.CU_PREAUTH_URL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                conn = (HttpsURLConnection) network.openConnection(geturl);
            }
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;chartset=UTF-8");
            conn.connect();
            osm = conn.getOutputStream();
            LogUtil.log("input : " + input.toString());
            osm.write(input.toString().getBytes());
            osm.flush();
            isr = new InputStreamReader(conn.getInputStream());
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            LogUtil.log("result : " + result.toString());
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            UMCrash.generateCustomLog(e, "UmengException");
            //return e.toString();
            return null;
        } finally {//执行流的关闭
            try {
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if(osm != null)
                    osm.close();
            } catch (IOException e) {
                e.printStackTrace();
                UMCrash.generateCustomLog(e, "UmengException");
                return null;
            }
            if(conn != null)
                conn.disconnect();
        }
    }

    /**
     * @author Aloha
     * @date 2021/2/3 22:00
     * @explain 中国移动预验证
     */
    private static String preCMCCAuth(Network network) {
        StringBuffer input = new StringBuffer();
        input.append("{\"custId\"").append(":").append("\"").append(Constants.CM_CUSTID).append("\"}");
        final StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        InputStreamReader isr = null;
        OutputStream osm = null;
        HttpURLConnection conn = null;
        try {
            URL geturl = new URL(Constants.CM_PREAUTH_URL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                conn = (HttpURLConnection) network.openConnection(geturl);
            }
            conn.setRequestMethod("POST");
            // 设置是否向HttpURLConnection输出
            conn.setDoOutput(true);
            // 设置是否从httpUrlConnection读入
            conn.setDoInput(true);
            conn.setRequestProperty("Content-type", "application/json;chartset=UTF-8");
            conn.setRequestProperty("seqId", UUID.randomUUID().toString().replace("-",""));
            conn.setRequestProperty("appId", Constants.CM_APP_ID);
            conn.setRequestProperty("Authorization", CipherUtil.generateAuthorization(Constants.CM_APP_ID,Constants.CM_APP_KEY));
            conn.connect();//get连接
            osm = conn.getOutputStream();
            LogUtil.log("input : " + input.toString());
            osm.write(input.toString().getBytes());
            osm.flush();
            isr = new InputStreamReader(conn.getInputStream());//输入流
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);//获取输入流数据
            }
            LogUtil.log("result : " + result.toString());
            return result.toString();
        }catch (Exception e) {
            e.printStackTrace();
            UMCrash.generateCustomLog(e, "UmengException");
            //return e.toString();
            return null;
        } finally {//执行流的关闭
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if(osm != null){
                    osm.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                UMCrash.generateCustomLog(e, "UmengException");
                return null;
            }
            if(conn != null){
                conn.disconnect();
            }
        }
    }

    /**
     * @author Aloha
     * @date 2021/2/3 22:00
     * @explain 结果解析
     */
    private static void parseResult(String result,String mobileNetwork,Map<String,String> resultMap){
        try{
            LogUtil.log( "result: " + result );
            if(result ==null){
                LogUtil.log( "解析错误");
                return;
            }
            JSONObject json = new JSONObject(result);
            if(json != null) {
                if ("CU".equals(mobileNetwork)) {
                    //联通
                    LogUtil.log("json--code" + json.getString("code"));
                    if ("0".equals(json.getString("code"))){
                        resultMap.put("code",ResultCode.matchCUCCCode(json.getString("code")));
                        resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                        JSONObject codeJson = new JSONObject(json.getString("data"));
                        resultMap.put("accessCode",codeJson.getString("accessCode"));
                    }else{
                        resultMap.put("code",ResultCode.matchCUCCCode(json.getString("code")));
                        resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                        resultMap.put("accessCode","");
                    }
                } else if ("CT".equals(mobileNetwork)){
                    //电信
                    if (json.getInt("result")==0 || json.getInt("result")==10000){
                        resultMap.put("code",ResultCode.matchCTCCCode("0"));
                        resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                        resultMap.put("accessCode",json.getString("accessCode"));
                    }else{
                        resultMap.put("code",ResultCode.matchCTCCCode(new Integer(json.getInt("result")).toString()));
                        resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                        resultMap.put("accessCode","");
                    }
                }else if ("CM".equals(mobileNetwork)){
                    //移动
                    if (json.getBoolean("success")){
                        resultMap.put("code",ResultCode.matchCMCCCode("0"));
                        JSONObject codeJson = new JSONObject(json.getString("data"));
                        if(codeJson !=null){
                            resultMap.put("accessCode",codeJson.getString("sessionId"));
                            resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                        } else{
                            resultMap.put("accessCode","");
                            resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                        }
                    }else{
                        resultMap.put("code",ResultCode.matchCMCCCode(json.getString("code")));
                        resultMap.put("accessCode","");
                        resultMap.put("msg",ResultCode.matchMsg(resultMap.get("code")));
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            UMCrash.generateCustomLog(e, "UmengException");
        }
    }
}
