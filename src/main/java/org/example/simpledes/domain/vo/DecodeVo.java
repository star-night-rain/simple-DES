package org.example.simpledes.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DecodeVo implements Serializable {
    private String plainText;
}
