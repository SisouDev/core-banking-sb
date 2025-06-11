package com.banking.core_banking.config;
import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecretGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
        keyGen.init(512);
        System.out.println(Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded()));
    }
}
