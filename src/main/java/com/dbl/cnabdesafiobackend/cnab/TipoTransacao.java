package com.dbl.cnabdesafiobackend.cnab;

import java.util.Arrays;
import java.util.Objects;

public enum TipoTransacao {
    DEBITO(1, Natureza.ENTRADA, Constants.Positivo), BOLETO(2, Natureza.SAIDA, Constants.Negativo), FINANCIAMENTO(3, Natureza.SAIDA, Constants.Negativo), CREDITO(4, Natureza.ENTRADA, Constants.Positivo), RECEBIMENTO(5, Natureza.ENTRADA, Constants.Positivo), VENDAS(6, Natureza.ENTRADA, Constants.Positivo), RECEBIMENTO_TED(7, Natureza.ENTRADA, Constants.Positivo), RECEBIMENTO_DOC(8, Natureza.ENTRADA, Constants.Positivo), ALUGUEL(9, Natureza.SAIDA, Constants.Negativo);

    private Natureza natureza;
    private Integer id;
    private String sinal;

    TipoTransacao(int id, Natureza natureza, String sinal) {
        this.id = id;
        this.natureza = natureza;
        this.sinal = sinal;
    }

    private static class Constants {
        public static final String Positivo = "+";
        public static final String Negativo = "-";
    }

    public static TipoTransacao convertePorId(Integer id) throws Exception {
        return Arrays
                .stream(
                        TipoTransacao.values())
                .filter(tipoTransacao -> Objects.equals(tipoTransacao.id, id))
                .findFirst()
                .orElseThrow(() ->
                        new Exception("impossivel fazer parse para tipoTransacao com id " + id)
                );
    }


}
