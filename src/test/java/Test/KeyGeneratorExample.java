package Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorExample {
    public static void main(String[] args) {
        try {
            // 创建 KeyGenerator 对象
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");

            // 生成密钥
            SecretKey secretKey = keyGenerator.generateKey();

            // 获取密钥的字节数组
            byte[] keyBytes = secretKey.getEncoded();

            // 打印密钥的字节数组
            System.out.print("{");
            for (int i = 0; i < keyBytes.length; i++) {
                System.out.printf("0x%02x", keyBytes[i]);
                if (i < keyBytes.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
