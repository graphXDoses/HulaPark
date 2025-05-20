package com.parkingapp.hulapark.Utilities.AES;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";

    // Should be 16 bytes for AES-128
    private static final String SECRET_KEY = "1234567890123456"; // 🔐 Replace with secure key
    private static final String INIT_VECTOR = "abcdef9876543210"; // 🔐 Replace with secure IV

    public static String encrypt(String data) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(CHARSET));
        SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), "AES");

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
        return Base64.encodeToString(encrypted, Base64.NO_WRAP);
    }

    public static String decrypt(String encrypted) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(CHARSET));
        SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(CHARSET), "AES");

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.NO_WRAP));
        return new String(original);
    }
}