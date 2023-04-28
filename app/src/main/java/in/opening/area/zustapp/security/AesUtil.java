package in.opening.area.zustapp.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String KEY = "mysecretkey12345";
    private static final String IV = "mysecretiv67890";

    public static String encrypt(String value) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        return android.util.Base64.encodeToString(encrypted, android.util.Base64.DEFAULT);
    }

    public static String decrypt(String encrypted) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] decoded = android.util.Base64.decode(encrypted, android.util.Base64.DEFAULT);
        byte[] original = cipher.doFinal(decoded);
        return new String(original, StandardCharsets.UTF_8);
    }

}


