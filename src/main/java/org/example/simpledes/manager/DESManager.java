package org.example.simpledes.manager;

public interface DESManager {
    String initialPermutation(String data);

    String[] secretKeyExpand(String secretKey);

    String roundFunction(String data, String subKey);

    String xor(String str1, String str2);

    String finalPermutation(String data);
}
