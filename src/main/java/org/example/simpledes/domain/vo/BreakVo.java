package org.example.simpledes.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class BreakVo implements Serializable {
    private Integer nums;
    private List<String> keys;
    private Double time;
}
