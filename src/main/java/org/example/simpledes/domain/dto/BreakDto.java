package org.example.simpledes.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BreakDto implements Serializable {
    private List<String> plainTexts;
    private List<String> cipherTexts;
}
