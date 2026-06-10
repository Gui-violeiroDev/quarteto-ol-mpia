package com.olympia.dto.response;
public class ApiResponse<T> {
    private boolean sucesso; private String mensagem; private T dados;
    public ApiResponse() {}
    public ApiResponse(boolean sucesso, String mensagem, T dados) { this.sucesso=sucesso; this.mensagem=mensagem; this.dados=dados; }
    public static <T> ApiResponse<T> ok(String mensagem, T dados) { return new ApiResponse<>(true, mensagem, dados); }
    public static <T> ApiResponse<T> erro(String mensagem) { return new ApiResponse<>(false, mensagem, null); }
    public boolean isSucesso() { return sucesso; } public void setSucesso(boolean v) { this.sucesso = v; }
    public String getMensagem() { return mensagem; } public void setMensagem(String v) { this.mensagem = v; }
    public T getDados() { return dados; } public void setDados(T v) { this.dados = v; }
}
