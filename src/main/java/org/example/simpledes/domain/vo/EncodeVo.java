package org.example.simpledes.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class EncodeVo implements Serializable {
    private String cipherText;
}
