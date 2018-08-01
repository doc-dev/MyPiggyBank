package it.docdev.mypiggybank.utils;

import java.security.MessageDigest;
import java.sql.Timestamp;

public class Utilities {

    public static final String KEY_NAME = "MyPiggyBank";
    public static long ADDEBITO = 1;
    protected static long ACCREDITO = 0;

    public static Timestamp adesso() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String hash(String base, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
