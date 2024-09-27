package org.example.simpledes.service;

import org.example.simpledes.domain.dto.BreakDto;
import org.example.simpledes.domain.dto.DecodeDto;
import org.example.simpledes.domain.dto.EncodeDto;
import org.example.simpledes.domain.vo.BreakVo;
import org.example.simpledes.domain.vo.DecodeVo;
import org.example.simpledes.domain.vo.EncodeVo;

public interface DESService {
    EncodeVo encode(EncodeDto encodeDto);

    DecodeVo decode(DecodeDto decodeDto);

    BreakVo bruteForceKey(BreakDto breakDto);
}
