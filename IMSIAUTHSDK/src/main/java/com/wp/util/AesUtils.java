package com.wp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {

    private static final byte[] IV = "0000000000000000".getBytes();

    private static final char[] SEC_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密码
     * @return 密文
     */
    public static String encryptAesNew(String content, String password) {
        byte[] bytes = encryptAesGop(content, password);
        if (bytes != null) {
            return StringUtil.toHex(bytes);
        } else {
            return null;
        }
    }

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密码
     * @return 密文
     */
    public static byte[] encryptAesGop(String content, String password) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);// 初始化为加密模式的密码器
            return cipher.doFinal(byteContent);// 加密
        } catch (Exception e) {
            //异常消除
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     *
     * @param content  AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     */
    public static String decryptAesNew(String content, String password) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(IV);
            SecretKeySpec secretKey = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);// 初始化为解密模式的密码器
            byte[] contentByte = StringUtil.hexToBytes(content);
            byte[] result = cipher.doFinal(contentByte);
            return new String(result); // 明文
        } catch (Exception e) {
            //异常消除
        }
        return null;
    }

    public static String encrypt(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] result = cipher.doFinal(content.getBytes("UTF-8"));
            return enCode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String enCode(byte[] paramArrayOfByte) {
        int i = 0;
        int j = paramArrayOfByte.length;
        StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length * 3 / 2);

        int k = j - 3;
        int m = i;
        int n = 0;
        int i1;
        while (m <= k) {
            i1 = (paramArrayOfByte[m] & 0xFF) << 16 | (paramArrayOfByte[(m + 1)] & 0xFF) << 8
                    | paramArrayOfByte[(m + 2)] & 0xFF;

            localStringBuffer.append(SEC_CODE[(i1 >> 18 & 0x3F)]);
            localStringBuffer.append(SEC_CODE[(i1 >> 12 & 0x3F)]);
            localStringBuffer.append(SEC_CODE[(i1 >> 6 & 0x3F)]);
            localStringBuffer.append(SEC_CODE[(i1 & 0x3F)]);

            m += 3;

            if (n++ >= 14) {
                n = 0;
                localStringBuffer.append(" ");
            }
        }

        if (m == i + j - 2) {
            i1 = (paramArrayOfByte[m] & 0xFF) << 16 | (paramArrayOfByte[(m + 1)] & 0xFF) << 8;

            localStringBuffer.append(SEC_CODE[(i1 >> 18 & 0x3F)]);
            localStringBuffer.append(SEC_CODE[(i1 >> 12 & 0x3F)]);
            localStringBuffer.append(SEC_CODE[(i1 >> 6 & 0x3F)]);
            localStringBuffer.append("=");
        } else if (m == i + j - 1) {
            i1 = (paramArrayOfByte[m] & 0xFF) << 16;

            localStringBuffer.append(SEC_CODE[(i1 >> 18 & 0x3F)]);
            localStringBuffer.append(SEC_CODE[(i1 >> 12 & 0x3F)]);
            localStringBuffer.append("==");
        }

        return localStringBuffer.toString();
    }

    public static String decrypt(String content, String password) {
        try {
            byte[] byteContent = toByte(content);
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] result = cipher.doFinal(byteContent);
            return new String(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static int getCode(char paramChar) {
        if ((paramChar >= 'A') && (paramChar <= 'Z'))
            return paramChar - 'A';
        if ((paramChar >= 'a') && (paramChar <= 'z'))
            return paramChar - 'a' + 26;
        if ((paramChar >= '0') && (paramChar <= '9')) {
            return paramChar - '0' + 26 + 26;
        }
        switch (paramChar) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
        }
        throw new RuntimeException("unexpected code: " + paramChar);
    }

    private static byte[] toByte(String paramString) {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        try {
            decode(paramString, localByteArrayOutputStream);
        } catch (Throwable localThrowable1) {
            throw new RuntimeException();
        }
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        try {
            localByteArrayOutputStream.close();
            localByteArrayOutputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayOfByte;
    }

    private static void decode(String paramString, OutputStream paramOutputStream) throws IOException {
        int i = 0;

        int j = paramString.length();
        while (true) {
            if ((i < j) && (paramString.charAt(i) <= ' ')) {
                i++;
                continue;
            }

            if (i == j) {
                break;
            }

            int k = (getCode(paramString.charAt(i)) << 18) + (getCode(paramString.charAt(i + 1)) << 12)
                    + (getCode(paramString.charAt(i + 2)) << 6) + getCode(paramString.charAt(i + 3));

            paramOutputStream.write(k >> 16 & 0xFF);
            if (paramString.charAt(i + 2) == '=') {
                break;
            }
            paramOutputStream.write(k >> 8 & 0xFF);
            if (paramString.charAt(i + 3) == '=') {
                break;
            }
            paramOutputStream.write(k & 0xFF);

            i += 4;
        }
    }
}