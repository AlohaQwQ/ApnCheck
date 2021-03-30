package com.wp.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
* @author: Chenzhenyong
* @description: 字符串处理工具类
* @date: Created in 10:27 2018/8/23
*/
public class StringUtil {

	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		int strLen;
		if (str != null && (strLen = str.length()) != 0) {
			for(int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}



	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);

		for(int i = 0; i < bArray.length; ++i) {
			String sTemp = Integer.toHexString(255 & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}

			sb.append(sTemp.toUpperCase());
		}

		return sb.toString();
	}
	/**
	 * 32位MD5加密
	 * @param content -- 待加密内容
	 * @return
	 */
	public static String md5Decode32(String content) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("NoSuchAlgorithmException",e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException", e);
		}
		//对生成的16字节数组进行补零操作
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10){
				hex.append("0");
			}
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static String analysisCUCCSessionId(String string) {
		try {
			JSONObject jsonObject = new JSONObject(string);
			String data = jsonObject.getString("data");
			if(data !=null){
				JSONObject jsonObj = new JSONObject(data);
				return jsonObj.getString("accessCode");
			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String analysisCTCCSessionId(String string) {
		try {
			JSONObject jsonObject = new JSONObject(string);
			String data = jsonObject.getString("accessCode");
			if(data !=null){
				return data;
			}else{
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

    public static String analysisSessionId(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            String data = jsonObject.getString("data");
            if(data !=null){
                JSONObject dataObject = new JSONObject(data);
                return dataObject.getString("sessionId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 将byte数组转换为十六进制文本
	 */
	public static String toHex(byte[] buf) {
		if (buf == null || buf.length == 0) {
			return "";
		}
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < buf.length; i++) {
			out.append(HEX[(buf[i] >> 4) & 0x0f]).append(HEX[buf[i] & 0x0f]);
		}
		return out.toString();
	}

	/**
	 * 将十六进制文本转换为byte数组
	 */
	public static byte[] hexToBytes(String str) {
		if (str == null) {
			return null;
		}
		char[] hex = str.toCharArray();
		int length = hex.length / 2;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++) {
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			int value = (high << 4) | low;
			if (value > 127) {
				value -= 256;
			}
			raw[i] = (byte) value;
		}
		return raw;
	}

	private final static char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 *
	 * @param data
	 * @return
	 */
	public static byte[] getBytes(String data){
		byte [] bytes = new byte[]{};
		try {
			bytes = data.getBytes(DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return  bytes;
	}

	public static void main(String[] args) {
		//Log.e(com.wp.util.Constants.TAG,md5Decode32("13842389678"));
        LogUtil.log(analysisSessionId("{\"success\":true,\"data\":{\"sessionId\":\"934d5316207e47b7bf3c87645ecf89ab\"}}"));
	}
}
