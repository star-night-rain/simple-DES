package org.example.simpledes.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DecodeDto implements Serializable {
    //密文
    private String cipherText;

    //密钥
    private String secretKey;
}
