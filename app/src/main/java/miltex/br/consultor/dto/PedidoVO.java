package miltex.br.consultor.dto;

public class PedidoVO {

    private Long idCliente;
    private Long idProduto;
    private Integer quantidadeItens;

    public PedidoVO(Long cliente,Long produto,Integer quantidade){
        this.idCliente=cliente;
        this.idProduto=produto;
        this.quantidadeItens=quantidade;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidadeItens() {
        return quantidadeItens;
    }

    public void setQuantidadeItens(Integer quantidadeItens) {
        this.quantidadeItens = quantidadeItens;
    }
}
