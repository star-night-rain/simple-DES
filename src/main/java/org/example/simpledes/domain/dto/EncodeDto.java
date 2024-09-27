package org.example.simpledes.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EncodeDto implements Serializable {
    //明文
    private String plainText;

    //密钥
    private String secretKey;
}
