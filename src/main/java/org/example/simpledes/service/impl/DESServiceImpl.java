package org.example.simpledes.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.simpledes.domain.dto.BreakDto;
import org.example.simpledes.domain.dto.DecodeDto;
import org.example.simpledes.domain.dto.EncodeDto;
import org.example.simpledes.domain.vo.BreakVo;
import org.example.simpledes.domain.vo.DecodeVo;
import org.example.simpledes.domain.vo.EncodeVo;
import org.example.simpledes.manager.DESManager;
import org.example.simpledes.service.DESService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    public String transform(String originalText, String secretKey, int type) {
        String modifiedText = "";

        String[] subKeys = desManager.secretKeyExpand(secretKey);
        String subKey1 = subKeys[0];
        String subKey2 = subKeys[1];

        //8bit
        if (originalText.matches("[01]{8}")) {
            {
                if (type == 1)
                    modifiedText = des(originalText, subKey1, subKey2);
                else
                    modifiedText = des(originalText, subKey2, subKey1);
            }
        } else {
            StringBuilder tentativeText = new StringBuilder();

//            StringBuilder unicodeText = new StringBuilder();
//            for (char c : originalText.toCharArray()) {
//                if (!Character.isDefined(c) || Character.isISOControl(c)) {
//                    String unicode = String.format("\\u%04x", (int) c);
//                    unicodeText.append(unicode);
//                } else {
//                    unicodeText.append(c);
//                }
//            }
//            originalText = unicodeText.toString();
//            log.info("originalText:" + originalText);

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
//                if (!Character.isDefined(cipherChar) || Character.isISOControl(cipherChar)) {
//                    String unicode = String.format("\\u%04x", cipherNum);
//                    tentativeText.append(unicode);
//                } else {
//                    tentativeText.append(cipherChar);
//                }

                tentativeText.append(cipherChar);
            }
            modifiedText = tentativeText.toString();
        }

        return modifiedText;
    }

    public EncodeVo encode(EncodeDto encodeDto) {
        String plainText = encodeDto.getPlainText();
        String secretKey = encodeDto.getSecretKey();

        String cipherText = transform(plainText, secretKey, 1);


        log.info("cipherText:" + cipherText);
        log.info("------------------------");

        return new EncodeVo(cipherText);
    }

    public DecodeVo decode(DecodeDto decodeDto) {
        String cipherText = decodeDto.getCipherText();
        String secretKey = decodeDto.getSecretKey();

        String plainText = transform(cipherText, secretKey, 2);

        log.info("plainText:" + plainText);
        log.info("------------------------");

        return new DecodeVo(plainText);
    }


    public List<String> getMatchingKeys(List<String> plainTexts, List<String> cipherTexts, List<String> possibleKeys,
                                        int threadNums) {
        List<String> keys;

        try (ExecutorService executor = Executors.newFixedThreadPool(threadNums)) {
            List<Future<List<String>>> futures = new ArrayList<>();

            for (String possibleKey : possibleKeys) {
                futures.add(executor.submit(() -> {
                    List<String> foundKeys = new ArrayList<>();
                    for (int i = 0; i < plainTexts.size(); i++) {
                        String plainText = plainTexts.get(i);
                        String cipherText = cipherTexts.get(i);
                        String predCipherText = transform(plainText, possibleKey, 1);
                        if (predCipherText.equals(cipherText)) {
                            foundKeys.add(possibleKey);
                        }
                    }
                    return foundKeys;
                }));
            }

            keys = new ArrayList<>();
            for (Future<List<String>> future : futures) {
                try {
                    keys.addAll(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    log.info("error");
                }
            }

            executor.shutdown();


        }
        return keys;
    }

    public BreakVo bruteForceKey(BreakDto breakDto) {
        List<String> plainTexts = breakDto.getPlainTexts();
        List<String> cipherTexts = breakDto.getCipherTexts();
        int threadNums = 10;

        List<String> possibleKeys = desManager.generateKeys();

        long startTime = System.currentTimeMillis();

        List<String> keys = getMatchingKeys(plainTexts, cipherTexts, possibleKeys, threadNums);

        long endTime = System.currentTimeMillis();
        double time = (endTime - startTime) / 1000.0;
        log.info(String.format("All tasks completed in %.4f seconds.", time));

        log.info("the number of keys:" + keys.size());
        log.info("Found keys:");
        for (String key : keys)
            log.info(key);

        return new BreakVo(keys.size(), keys, time);
    }
}