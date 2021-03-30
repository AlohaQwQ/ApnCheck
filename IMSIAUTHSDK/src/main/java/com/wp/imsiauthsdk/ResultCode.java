package com.wp.imsiauthsdk;

import java.util.HashMap;

public class ResultCode {
	static HashMap<String,String> ctccCodeMap = new HashMap<String,String>();
	static{
		ctccCodeMap.put("0","0");
		ctccCodeMap.put("-10000","00006");
		ctccCodeMap.put("-10001","00006");
		ctccCodeMap.put("-10002","00001");
		ctccCodeMap.put("-10003","00004");
		ctccCodeMap.put("-10004","00007");
		ctccCodeMap.put("-10005","99999");
		ctccCodeMap.put("-10006","00005");
		ctccCodeMap.put("-10007","99999");
		ctccCodeMap.put("-10008","00008");
		ctccCodeMap.put("-10009","00009");
		ctccCodeMap.put("-10010","99999");
		ctccCodeMap.put("-10011","99999");
		ctccCodeMap.put("-10012","99999");
		ctccCodeMap.put("-10013","99999");
		ctccCodeMap.put("-10014","00001");
		ctccCodeMap.put("-10015","00006");
		ctccCodeMap.put("-10016","00005");
		ctccCodeMap.put("-10017","99999");
		ctccCodeMap.put("-20005","00001");
		ctccCodeMap.put("-20006","00002");
		ctccCodeMap.put("-20007","00001");
		ctccCodeMap.put("-20100","00001");
		ctccCodeMap.put("-20102","00001");
		ctccCodeMap.put("-30001","00001");
		ctccCodeMap.put("-30003","99999");
		ctccCodeMap.put("-99999","99999");
		ctccCodeMap.put("51002","00001");
		ctccCodeMap.put("51114","00006");
		ctccCodeMap.put("51207","00005");
		ctccCodeMap.put("51208","99999");
	}
	public static String matchCTCCCode(String sourceCode){
		return ctccCodeMap.get(sourceCode);
	}
	
	static HashMap<String,String> cuccCodeMap = new HashMap<String,String>();
	static{
		cuccCodeMap.put("0","0");
		cuccCodeMap.put("100","00005");
		cuccCodeMap.put("101","00001");
		cuccCodeMap.put("102","00005");
		cuccCodeMap.put("103","00007");
		cuccCodeMap.put("104","00008");
		cuccCodeMap.put("105","99999");
		cuccCodeMap.put("106","00005");
		cuccCodeMap.put("107","00005");
		cuccCodeMap.put("108","00008");
		cuccCodeMap.put("300","00005");
		cuccCodeMap.put("301","00005");
		cuccCodeMap.put("303","00008");
		cuccCodeMap.put("400","00001");
		cuccCodeMap.put("401","00001");
		cuccCodeMap.put("402","00001");
		cuccCodeMap.put("600","00001");
		cuccCodeMap.put("1000","00001");
		cuccCodeMap.put("1001","00001");
		cuccCodeMap.put("1002","00001");
		cuccCodeMap.put("1004","00001");
		cuccCodeMap.put("1005","00001");
		cuccCodeMap.put("1010","99999");
		cuccCodeMap.put("1011","99999");
		cuccCodeMap.put("3010","00006");
		cuccCodeMap.put("3011","00007");
		cuccCodeMap.put("3012","00006");
		cuccCodeMap.put("3013","00006");
		cuccCodeMap.put("3014","00006");
		cuccCodeMap.put("3015","99999");
		cuccCodeMap.put("3016","00006");
		cuccCodeMap.put("3017","00006");
		cuccCodeMap.put("3018","00006");
		cuccCodeMap.put("3032","00002");
		cuccCodeMap.put("3050","99999");
		cuccCodeMap.put("3051","00007");
		cuccCodeMap.put("3052","00007");
		cuccCodeMap.put("3053","00007");
		cuccCodeMap.put("3054","00007");
		cuccCodeMap.put("3055","00007");
		cuccCodeMap.put("3056","00006");
		cuccCodeMap.put("3057","00006");
		cuccCodeMap.put("3058","00006");
		cuccCodeMap.put("3059","00006");
		cuccCodeMap.put("3060","00006");
		cuccCodeMap.put("3061","00008");
		cuccCodeMap.put("3062","00007");
		cuccCodeMap.put("3063","00010");
		cuccCodeMap.put("3064","00001");
		cuccCodeMap.put("3065","00005");
		cuccCodeMap.put("3066","99999");
	}
	
	public static String matchCUCCCode(String sourceCode){
		return cuccCodeMap.get(sourceCode);
	}

	static HashMap<String,String> cmccCodeMap = new HashMap<String,String>();
	static{
		cmccCodeMap.put("0","0");
		cmccCodeMap.put("CE999001","00001");
		cmccCodeMap.put("CE999002","00002");
		cmccCodeMap.put("CE999003","00003");
		cmccCodeMap.put("CE999004","00004");
		cmccCodeMap.put("CE999005","00005");
		cmccCodeMap.put("CE999999","99999");
	}

	public static String matchCMCCCode(String sourceCode){
		return cmccCodeMap.get(sourceCode);
	}

	static HashMap<String,String> msgMap = new HashMap<String,String>();
	static{
		msgMap.put("0","成功");
		msgMap.put("00001","参数错误");
		msgMap.put("00002","APPID不存在");
		msgMap.put("00003","APPID被禁用");
		msgMap.put("00004","解密失败");
		msgMap.put("00005","权限验证失败");
		msgMap.put("00006","会话标识获取失败");
		msgMap.put("00007","无效IP");
		msgMap.put("00008","超过预设阀值");
		msgMap.put("00009","会话标识超期");
		msgMap.put("00010","网关并发连接数受限");
		msgMap.put("00011","未知运营商");
		msgMap.put("99999","未知异常");
	}

	public static String matchMsg(String sourceCode){
		return msgMap.get(sourceCode);
	}

}
