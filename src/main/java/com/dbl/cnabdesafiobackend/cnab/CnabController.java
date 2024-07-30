package com.dbl.cnabdesafiobackend.cnab;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/v1/cnab")
@RestController
public class CnabController {

    private final CnabService cnabService;

    public CnabController(CnabService cnabService) {
        this.cnabService = cnabService;
    }


    @PostMapping
    public void processaArquivoCnab(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        cnabService.processaArquivoCnab(multipartFile);
    }
}
