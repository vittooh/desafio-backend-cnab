package com.dbl.cnabdesafiobackend.cnab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dbl.cnabdesafiobackend.utils.Constantes.*;

@Service
public class CnabService {

    public static final String LINHA_DO_ARQUIVO_TEM_MENOS_DE_80_CARACTERES = "Linha do arquivo tem menos de 80 caracteres";
    private final TransacoesCnabRepository transacoesCnabRepository;

    private final Logger logger = LoggerFactory.getLogger(CnabService.class);

    public CnabService(TransacoesCnabRepository transacoesCnabRepository) {
        this.transacoesCnabRepository = transacoesCnabRepository;
    }


    public void processaArquivoCnab(MultipartFile multipartFile) throws Exception {
        logger.info("Arquivo cnab recebido, iniciando leitura do inputstream");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
        List<TransacoesCnab> transacoes = new ArrayList<>();
        while (bufferedReader.ready()) {
            logger.info("Lendo linha e realizando o parse");
            String linha = bufferedReader.readLine();
            if (linha.length() != 80) {
                logger.error(LINHA_DO_ARQUIVO_TEM_MENOS_DE_80_CARACTERES);
                throw new Exception(LINHA_DO_ARQUIVO_TEM_MENOS_DE_80_CARACTERES);
            }
            TransacoesCnab transacoesCnab = new TransacoesCnab(
                    TipoTransacao.convertePorId(Integer.parseInt(linha.substring(0, 1))),
                    linha.substring(INICIO_DATA, FIM_DATA),
                    new BigDecimal(linha.substring(INICIO_VALOR, FIM_VALOR)),
                    linha.substring(INICIO_CPF, FIM_CPF),
                    linha.substring(INICIO_CARTAO, FIM_CARTAO),
                    linha.substring(INICIO_HORA, FIM_HORA),
                    linha.substring(INICIO_DONO_LOJA, FIM_DONO_LOJA).trim(),
                    linha.substring(INICIO_NOME_LOJA).trim()
            );
            transacoes.add(transacoesCnab);
            logger.info("linha processada com sucesso");
        }
        logger.info("salvando entradas de cnab no banco de dados");
        transacoesCnabRepository.saveAll(
                transacoes
        );
        logger.info("finalizando processamento de arquivo de cnab");
    }
}
