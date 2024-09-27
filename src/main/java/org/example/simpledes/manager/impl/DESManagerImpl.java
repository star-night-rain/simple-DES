package org.example.simpledes.manager.impl;


import lombok.extern.slf4j.Slf4j;
import org.example.simpledes.manager.DESManager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DESManagerImpl implements DESManager {

    public String permutation(String data, int[] PBox) {
        StringBuilder str = new StringBuilder();
        for (int pBox : PBox) {
            char num = data.charAt(pBox - 1);
            str.append(num);
        }

        return str.toString();
    }

    public String initialPermutation(String data) {
        int[] PBox = {2, 6, 3, 1, 4, 8, 5, 7};
        return permutation(data, PBox);
    }

    public String[] secretKeyExpand(String secretKey) {
        int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
        String str = permutation(secretKey, P10);

        String left = str.substring(0, str.length() / 2);
        String right = str.substring(str.length() / 2);

        int[] leftShift1 = {2, 3, 4, 5, 1};
        String left1 = permutation(left, leftShift1);
        String right1 = permutation(right, leftShift1);

        int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
        String subKey1 = permutation(left1 + right1, P8);
        log.info("subKey1:" + subKey1);

        int[] leftShift2 = {3, 4, 5, 1, 2};
        String left2 = permutation(left, leftShift2);
        String right2 = permutation(right, leftShift2);
        String subKey2 = permutation(left2 + right2, P8);
        log.info("subKey2:" + subKey2);

        return new String[]{subKey1, subKey2};
    }

    public String roundFunction(String data, String subKey) {
        int[] EPBox = {4, 1, 2, 3, 2, 3, 4, 1};
        String tentative = permutation(data, EPBox);
        tentative = xor(tentative, subKey);

        int[][] SBox1 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 0, 2}};
        int[][] SBo2 = {{0, 1, 2, 3}, {2, 3, 1, 0}, {3, 0, 1, 2}, {2, 1, 0, 3}};
        String left = Substitution(tentative.substring(0, 4), SBox1);
        String right = Substitution(tentative.substring(4), SBo2);

        int[] SPBox = {2, 4, 3, 1};
        tentative = permutation(left + right, SPBox);

        return tentative;
    }


    public String xor(String str1, String str2) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < str1.length(); i++) {
            char c1 = str1.charAt(i);
            char c2 = str2.charAt(i);
            if (c1 == c2) {
                str.append('0');
            } else {
                str.append("1");
            }
        }
        return str.toString();
    }

    public String Substitution(String data, int[][] SBox) {
        String str1 = data.substring(1, 3);
        int col = Integer.parseInt(str1, 2);

        String str2 = String.valueOf(data.charAt(0)) +
                data.charAt(3);
        int row = Integer.parseInt(str2, 2);

        int num = SBox[row][col];

        String binaryNum = Integer.toBinaryString(num);

        return (binaryNum.length() >= 2) ? binaryNum.substring(0, 2) :
                String.format("%2s", binaryNum).replace(' ', '0');
    }

    public String finalPermutation(String data) {
        int[] PBox = {4, 1, 3, 5, 7, 2, 8, 6};
        return permutation(data, PBox);
    }
}
