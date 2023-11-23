package Methods.PPDQ;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SecureHMACHashGenerator {
    private byte[] key;

    public SecureHMACHashGenerator(byte[] key) {
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
