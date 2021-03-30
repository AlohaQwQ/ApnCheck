package com.wp.imsiauthsdk;


public class Constants {

	public static final String TELETE_COM_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5se07mkN71qsSJHjZ2Z0+Z+4L" +
			"lLvf2sz7Md38VAa3EmAOvI7vZp3hbAxicL724ylcmisTPtZQhT/9C+25AELqy9PN" +
			"9JmzKpwoVTUoJvxG4BoyT49+gGVl6s6zo1byNoHUzTfkmRfmC9MC53HvG8GwKP5x" +
			"tcdptFjAIcgIR7oAWQIDAQAB";

	/**
	 * 应用秘钥A：AES的key，16位，合作方自行生成（生成方法在AESUtil中的getAesKey（）方法）
	 */
	public static final String APP_AES_KEY = "123456";
	/**
	 * 应用ID
	 */
	public static final String APP_ID = "";

	/**
	 * 应用秘钥(用于签名)
	 */
	public static final String APP_SECRET = "";

	public static final int HTTP_RESTART = 302;

	public  static String CM_PREAUTH_URL = "http://wz.app.sdbmcc.com:9015/bmcc-fass-ws-api/api/mobile/consistency/request";
	public  static String CM_APP_ID = "aaa";
	public  static String CM_APP_KEY = "aaa";
	public  static String CM_CUSTID = "aaa";

	public  static String CU_PREAUTH_URL = "https://opencloud.wostore.cn/openapi/netauth/precheck/wp";
	public  static String CU_APP_ID = "aaa";
	public  static String CU_APP_KEY = "aaa";

	public  static String CT_PREAUTH_URL = "https://id6.me/gw/auth/simpreauth.do";
	public  static String CT_APP_ID = "aaa";
	public  static String CT_APP_KEY = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

	/**
	 * 设备类型
	 */
	public static final String CLIENT_TYPE = "3";

	/**
	 * json格式
	 */
	public static final String FORMAT = "json";

	/**
	 * 版本号
	 */
	public static final String VERSION = "v1.5";
	/**
	 * 密钥
	 */
	public static final String KEY = "e1c3d0de067d4666";

	/**
	 * 公钥
	 */
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5se07mkN71qsSJHjZ2Z0+Z+4LlLvf2sz7Md38VAa3EmAOvI7vZp3hbAxicL724ylcmisTPtZQhT/9C+25AELqy9PN9JmzKpwoVTUoJvxG4BoyT49+gGVl6s6zo1byNoHUzTfkmRfmC9MC53HvG8GwKP5xtcdptFjAIcgIR7oAWQIDAQAB";
}
