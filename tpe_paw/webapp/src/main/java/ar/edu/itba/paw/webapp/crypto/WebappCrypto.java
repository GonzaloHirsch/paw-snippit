package ar.edu.itba.paw.webapp.crypto;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class WebappCrypto {

    public static final String TEST_KEY = "thakeyyy";

    //TODO StackOverflow
    private static byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String[] generateOtps(String key){
        String[] otps = new String[3];
        try {
            Base32 base32 = new Base32();
            String encodedKey = new String(base32.encode(key.getBytes()));
            String secretKeyHex = Hex.encodeHexString(base32.decode(encodedKey));
            Long utcNow = Instant.now().getEpochSecond();
            utcNow = utcNow / 300;
            utcNow--;
            for (int i = 0; i < 3; i++) {
                int otp = generateSingleCode(secretKeyHex, utcNow);
                String result = Integer.toString(otp);
                while (result.length() < 6) {
                    result = "0" + result;
                }
                otps[i] = result;
                utcNow++;
            }
        } catch (Exception e){
            return new String[0];
        }
        return otps;
    }
    public static String generateOtp(String key){
        String result;
        try {
            Base32 base32 = new Base32();
            String encodedKey = new String(base32.encode(key.getBytes()));
            String secretKeyHex = Hex.encodeHexString(base32.decode(encodedKey));
            Long utcNow = Instant.now().getEpochSecond();
            utcNow = utcNow / 300;
            int otp = generateSingleCode(secretKeyHex, utcNow);
            result = Integer.toString(otp);
            while (result.length() < 6) {
                result = "0" + result;
            }
        } catch (Exception e){
            return "";
        }
        return result;
    }

    private static int generateSingleCode(String secretKeyHex, Long utcNow) throws NoSuchAlgorithmException, InvalidKeyException {
        String steps = Long.toHexString(utcNow).toUpperCase();
        while (steps.length() < 16) {
            steps = "0" + steps;
        }
        byte[] msg = hexStringToBytes(steps);
        byte[] k = hexStringToBytes(secretKeyHex);
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(k, "RAW");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(msg);
        int offset = hmacData[hmacData.length - 1] & 0xf;
        int binary = ((hmacData[offset] & 0x7f) << 24) | ((hmacData[offset + 1] & 0xff) << 16) | ((hmacData[offset + 2] & 0xff) << 8) | (hmacData[offset + 3] & 0xff);
        int otp = binary % 1000000;
        return otp;
    }
}
