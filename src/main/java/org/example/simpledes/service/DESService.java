package org.example.simpledes.service;

import org.example.simpledes.domain.dto.DecodeDto;
import org.example.simpledes.domain.dto.EncodeDto;

public interface DESService {
    String encode(EncodeDto encodeDto);

    String decode(DecodeDto decodeDto);
}
