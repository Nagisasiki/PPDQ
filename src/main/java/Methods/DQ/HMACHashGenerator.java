package Methods.DQ;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;

public class HMACHashGenerator {
    private byte[] key;

    public HMACHashGenerator(byte[] key) {
        this.key = key;
    }

    public int generateHMAC(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data);
        return ByteBuffer.wrap(hmacBytes).getInt();
    }
}
