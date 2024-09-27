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

    public boolean valid(String str) {
        return str.matches("[01]{8}");
    }

    public String transform(String originalText, String secretKey, int type) {
        String modifiedText = "";

        String[] subKeys = desManager.secretKeyExpand(secretKey);
        String subKey1 = subKeys[0];
        String subKey2 = subKeys[1];

        //8bit
        if (valid(originalText)) {
            {
                if (type == 1)
                    modifiedText = des(originalText, subKey1, subKey2);
                else
                    modifiedText = des(originalText, subKey2, subKey1);
            }
        } else {
            StringBuilder tentativeText = new StringBuilder();
            for (char c : originalText.toCharArray()) {
                String binaryNum = Integer.toBinaryString((int) c);
                log.info("binaryNum:" + binaryNum);
                String text = (binaryNum.length() >= 8) ? binaryNum.substring(0, 8) :
                        String.format("%8s", binaryNum).replace(' ', '0');
                log.info("text:" + text);
                String cipherStr = "";
                if (type == 1)
                    cipherStr = des(text, subKey1, subKey2);
                else
                    cipherStr = des(text, subKey2, subKey1);
                log.info("cipherStr:" + cipherStr);
                int cipherNum = Integer.parseInt(cipherStr, 2);
                log.info("cipherNum:" + cipherNum);
                char cipherChar = (char) cipherNum;
                log.info("cipherChar:" + cipherChar);
                tentativeText.append(cipherChar);
            }
            modifiedText = tentativeText.toString();
        }

        return modifiedText;
    }

    public String encode(EncodeDto encodeDto) {
        String plainText = encodeDto.getPlainText();
        String secretKey = encodeDto.getSecretKey();

        String cipherText = transform(plainText, secretKey, 1);


        log.info("cipherText:" + cipherText);
        log.info("------------------------");

        return cipherText;
    }

    public String decode(DecodeDto decodeDto) {
        String cipherText = decodeDto.getCipherText();
        String secretKey = decodeDto.getSecretKey();

        String plainText = transform(cipherText, secretKey, 2);

        log.info("plainText:" + plainText);
        log.info("------------------------");

        return plainText;
    }
}
