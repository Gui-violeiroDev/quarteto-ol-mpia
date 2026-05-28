package com.olympia.enums;

public enum TipoFormacao {
    DUO("Duo", 3000.00, "1 Violino + 1 Cello"),
    TRIO("Trio", 4000.00, "1 Violino + 1 Viola + 1 Cello"),
    QUARTETO("Quarteto", 5000.00, "2 Violinos + 1 Viola + 1 Cello"),
    QUARTETO_PIANO("Quarteto + Piano", 6000.00, "2 Violinos + 1 Viola + 1 Cello + Piano");

    private final String descricao;
    private final Double valorBase;
    private final String composicao;

    TipoFormacao(String descricao, Double valorBase, String composicao) {
        this.descricao = descricao;
        this.valorBase = valorBase;
        this.composicao = composicao;
    }

    public String getDescricao() { return descricao; }
    public Double getValorBase() { return valorBase; }
    public String getComposicao() { return composicao; }
}
