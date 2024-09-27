package org.example.simpledes.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.simpledes.domain.dto.DecodeDto;
import org.example.simpledes.domain.dto.EncodeDto;
import org.example.simpledes.manager.DESManager;
import org.example.simpledes.service.DESService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DESServiceImpl implements DESService {
    private final DESManager desManager;

    public DESServiceImpl(DESManager desManager) {
        this.desManager = desManager;
    }

    public String des(String text, String subKey1, String subKey2) {
        String tentativeText = desManager.initialPermutation(text);
        String left = tentativeText.substring(0, tentativeText.length() / 2);
        String right = tentativeText.substring(tentativeText.length() / 2);

        String tentativeRight = desManager.roundFunction(right, subKey1);

        left = desManager.xor(left, tentativeRight);

        //交换
        String temp = left;
        left = right;
        right = temp;

        tentativeRight = desManager.roundFunction(right, subKey2);

        left = desManager.xor(left, tentativeRight);

        return desManager.finalPermutation(left + right);
    }

    public String encode(EncodeDto encodeDto) {
        String plainText = encodeDto.getPlainText();
        String secretKey = encodeDto.getSecretKey();

        String[] subKeys = desManager.secretKeyExpand(secretKey);
        String subKey1 = subKeys[0];
        String subKey2 = subKeys[1];

        String cipherText = des(plainText, subKey1, subKey2);

        log.info("cipherText:" + cipherText);
        log.info("------------------------");

        return cipherText;
    }

    public String decode(DecodeDto decodeDto) {
        String cipherText = decodeDto.getCipherText();
        String secretKey = decodeDto.getSecretKey();

        String[] subKeys = desManager.secretKeyExpand(secretKey);
        String subKey1 = subKeys[0];
        String subKey2 = subKeys[1];

        String plainText = des(cipherText, subKey2, subKey1);

        log.info("plainText:" + plainText);
        log.info("------------------------");

        return plainText;
    }
}
