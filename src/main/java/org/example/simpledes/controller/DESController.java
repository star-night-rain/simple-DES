package org.example.simpledes.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.simpledes.domain.dto.DecodeDto;
import org.example.simpledes.domain.dto.EncodeDto;
import org.example.simpledes.service.DESService;
import org.example.simpledes.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/des")
@Slf4j
public class DESController {

    private final DESService desService;

    public DESController(DESService desService) {
        this.desService = desService;
    }

    @PostMapping("/encode")
    public Result encode(@RequestBody EncodeDto encodeDto) {
        log.info("plainText:" + encodeDto.getPlainText());
        String cipherText = desService.encode(encodeDto);
        return Result.success(cipherText);
    }

    @PostMapping("/decode")
    public Result decode(@RequestBody DecodeDto decodeDto) {
        log.info("cipherText:" + decodeDto.getCipherText());
        String plainText = desService.decode(decodeDto);
        return Result.success(plainText);
    }
}
