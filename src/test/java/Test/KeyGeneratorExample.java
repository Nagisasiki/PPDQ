package Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class KeyGeneratorExample {
    public static void main(String[] args) {
        try {
            // ���� KeyGenerator ����
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");

            // ������Կ
            SecretKey secretKey = keyGenerator.generateKey();

            // ��ȡ��Կ���ֽ�����
            byte[] keyBytes = secretKey.getEncoded();

            // ��ӡ��Կ���ֽ�����
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
