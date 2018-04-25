package com.suncity.pay.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ToolKit {
	static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	// 支付公钥、代付公钥、商户私钥
	/*public static final String PAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCji9sOuiizT37B1ugQS84rm5ppSAbTjX+KXizGnSCoW1pL07LVrPBVRN7JJWgEDcC5sdPtsjA6+btW4AWPZV7gyDLEerQpC+uizaMXiQYvOY5dRbk0joJq2vDH0vZ19DDEwzPQxiwhjfPLulyW901lySSJAbagvsh/gdGmI3/dKQIDAQAB";
	public static final String REMIT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbSTQeDgW0gawN8Iacz57mNKBfNJQCFANSv3I0ITDCZw1LGqDuV7QeHs6YfUSihQXm/YE0QjEaCkUY0GV7kFAqBf+NvZjVgYzzFf+5cTenYUh+YItKqmyoudz7WF/DMA4iowrJkfcrTMkiiPQD/ZcuLxERQD2KFhgTyFBgxyGgvwIDAQAB";
	public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMOAiGISeGX7hBWkxaJdxE76FWrywdX5GTa6LYV6r49nmNQ/rRWQ+bgORqrcWvHBbf2EsGRjQJLg5gZzscy/0vsX9Yk6YExmpA9YEGeUZg83P9Lrbi0rPvlZF7fume6Kp0unBllNF24bZqJJSrCiEEsGTxB1PLdkV6T0WuHqjus3AgMBAAECgYAYJF4zJi7CAT49YfyZ4VRloFJWw6WWI82uSW1np0/YuKVRyI86M43y4ahuXwnIAufvP5x8uRj3Slh9gXn6W9HUwoHBdHvTqihcf7Zr2dGtsQXZmwNSdroXB8VZe1n3gpALkbAjnoy9Lj7mbgJfcnE2eQXE2XpDYJlEtJg0kZb+kQJBAPhVz0kkXVTiYcVCqLgjuN9kb6sm4Payhgmjiick9XCaB3ZOOG9/tTNYrc1owebMzobi3pBDHRGV5USSW0KIftkCQQDJiUTE0EC+kBboFjqZJnwXBnuMi9Rpi/vO20rDhTRtWoozcuhA8DTPgVwdNqEcm10MPevCBI1bwGkdC0PS5pCPAkAYxia5u0D8WOE8FpxSUm39Cz4Aqw5CTikFCSWdJhi+NP+Nk9wZc3oWN0CPf2XoqoHn/vYJGkjqGjQXFSq5hnhxAkBEVgU5bxvlAdqii5cHAyOkcxBFkcxOlaamd3kcXvht8/tUgqv6CPj/O2/OgA2VM6ETW5OrT/vWlqGNtZoj0wZFAkEA2JCBYhtP8EG2WA1SE8j8IgLtH4fJGVlOhPKkFs20nOLD2lfod6Rhpz+C6l6M6f0WKCeTXBlEZgG8Wn+vilRRDg==";
*/
	// RSA非对称密钥算法
	public static final String KEY_ALGORITHM = "RSA";

	public final static String CHARSET = "UTF-8";

	/**
	 * 获取响应报文
	 */
	private static String getResponseBodyAsString(InputStream in) {
		try {
			BufferedInputStream buf = new BufferedInputStream(in);
			byte[] buffer = new byte[1024];
			StringBuffer data = new StringBuffer();
			int readDataLen;
			while ((readDataLen = buf.read(buffer)) != -1) {
				data.append(new String(buffer, 0, readDataLen, CHARSET));
			}
			System.out.println("响应报文=" + data);
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 提交请求
	 */
	public static String request(String url, String params) {
		try {
			System.out.println("请求报文:" + params);
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(1000 * 10);
			// conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			// conn.setRequestProperty("Accept","*/*");
			conn.setRequestProperty("Charset", CHARSET);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(params.toString().getBytes(CHARSET));
			outStream.flush();
			outStream.close();
			return getResponseBodyAsString(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * MD5加密
	 */
	public final static String MD5(String s, String encoding) {
		try {
			byte[] btInput = s.getBytes(encoding);
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
				str[k++] = HEX_DIGITS[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 转换成Json格式
	 */
	public static String mapToJson(Map<String, String> map) {
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		StringBuffer json = new StringBuffer();
		json.append("{");
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = entry.getValue();
			json.append("\"").append(key).append("\"");
			json.append(":");
			json.append("\"").append(value).append("\"");
			if (it.hasNext()) {
				json.append(",");
			}
		}
		json.append("}");
		System.out.println("mapToJson=" + json.toString());
		return json.toString();
	}

	/**
	 * 生成数字字母随机字符串
	 * @param num 随机字符串长度
	 * @return 生成的随机字符串
	 */
	public static String randomStr(int num) {
		char[] randomMetaData = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
				'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
				'5', '6', '7', '8', '9' };
		Random random = new Random();
		String tNonceStr = "";
		for (int i = 0; i < num; i++) {
			tNonceStr += (randomMetaData[random.nextInt(randomMetaData.length - 1)]);
		}
		return tNonceStr;
	}

	/**
	 * 公钥加密
	 * @param data待加密数据
	 * @param key 密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
		// 解码支付公钥
		byte[] key = Base64.getDecoder().decode(publicKey);
		// 实例化密钥工厂
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 密钥材料转换
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
			// 产生公钥
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
			// 数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			int blockSize = cipher.getOutputSize(data.length) - 11;
			return doFinal(data, cipher, blockSize);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私钥解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String privateKeyValue) {
		byte[] key = Base64.getDecoder().decode(privateKeyValue);
		try {
			// 取得私钥
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 生成私钥
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			// 数据解密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int blockSize = cipher.getOutputSize(data.length);
			return doFinal(data, cipher, blockSize);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密解密共用核心代码，分段加密解密
	 * @param decryptData 要加密的数据
	 * @param cipher
	 * @return
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws IOException
	 */
	public static byte[] doFinal(byte[] decryptData, Cipher cipher, int blockSize)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		int offSet = 0;
		byte[] cache = null;
		int i = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		while (decryptData.length - offSet > 0) {
			if (decryptData.length - offSet > blockSize) {
				cache = cipher.doFinal(decryptData, offSet, blockSize);
			} else {
				cache = cipher.doFinal(decryptData, offSet, decryptData.length - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * blockSize;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}
}