package miltex.br.consultor.dto;

import java.util.Date;

public class PedidosVO {

    private Long id;
    //TODO consertar depois no SERVICO a data está vindo errada...
    //private Date dataPedido;
    //private Date dataUltAtualizacao;
    private Integer quantidade;
    private String usuUltAtualizacao;
    private ProdutoVO produto;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Date getDataUltAtualizacao() {
        return dataUltAtualizacao;
    }

    public void setDataUltAtualizacao(Date dataUltAtualizacao) {
        this.dataUltAtualizacao = dataUltAtualizacao;
    }*/

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getUsuUltAtualizacao() {
        return usuUltAtualizacao;
    }

    public void setUsuUltAtualizacao(String usuUltAtualizacao) {
        this.usuUltAtualizacao = usuUltAtualizacao;
    }

    public ProdutoVO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoVO produto) {
        this.produto = produto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
