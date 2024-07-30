package com.dbl.cnabdesafiobackend;

import com.dbl.cnabdesafiobackend.cnab.CnabService;
import com.dbl.cnabdesafiobackend.cnab.TipoTransacao;
import com.dbl.cnabdesafiobackend.cnab.TransacoesCnab;
import com.dbl.cnabdesafiobackend.cnab.TransacoesCnabRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.dbl.cnabdesafiobackend.cnab.CnabService.LINHA_DO_ARQUIVO_TEM_MENOS_DE_80_CARACTERES;

public class CnabServiceTest {

    private final CnabService cnabService;

    private final TransacoesCnabRepository transacoesCnabRepository;

    public CnabServiceTest() {
        transacoesCnabRepository = Mockito.mock(TransacoesCnabRepository.class);
        this.cnabService = new CnabService(
                transacoesCnabRepository
        );
    }


    @Test
    void deveProcessarERetornarLinhaCorretamente() {
        MultipartFile multipartFile = new MockMultipartFile("file",
                "3201903010000014200096206760174753****3153153453JOÃO MACEDO   BAR DO JOÃO       ".getBytes(StandardCharsets.UTF_8)
        );

        ArgumentCaptor<List<TransacoesCnab>> captor = ArgumentCaptor.forClass(List.class);

        Mockito.when(
                transacoesCnabRepository.saveAll(captor.capture())
        ).thenReturn(new ArrayList<>());
        Assertions.assertDoesNotThrow(()
                -> cnabService.processaArquivoCnab(multipartFile));


        List<TransacoesCnab> transacoesCnabs = captor.getValue();
        Assertions.assertEquals(1, transacoesCnabs.size());
        TransacoesCnab transacoesCnab = transacoesCnabs.get(0);
        Assertions.assertEquals(TipoTransacao.FINANCIAMENTO, transacoesCnab.getTipo());
        Assertions.assertEquals("20190301", transacoesCnab.getData());
        Assertions.assertEquals(new BigDecimal("142"), transacoesCnab.getValor());
        Assertions.assertEquals("09620676017", transacoesCnab.getCpf());
        Assertions.assertEquals("4753****3153", transacoesCnab.getCartao());
        Assertions.assertEquals("153453", transacoesCnab.getHora());
        Assertions.assertEquals("JOÃO MACEDO", transacoesCnab.getDonoLoja());
        Assertions.assertEquals("BAR DO JOÃO", transacoesCnab.getNomeLoja());

        Mockito.verify(transacoesCnabRepository,
                Mockito.times(1)).saveAll(Mockito.anyCollection());
    }

    @Test
    void deveRetornarExcecaoLinhaArquivo() {
        MultipartFile multipartFile = new MockMultipartFile("file",
                "3****31".getBytes(StandardCharsets.UTF_8)
        );

        Exception e = Assertions.assertThrows(Exception.class,
                () -> cnabService.processaArquivoCnab(multipartFile));
        Assertions.assertEquals(LINHA_DO_ARQUIVO_TEM_MENOS_DE_80_CARACTERES, e.getMessage());
    }
}
