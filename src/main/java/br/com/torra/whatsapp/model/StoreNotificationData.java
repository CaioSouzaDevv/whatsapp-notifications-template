package br.com.torra.whatsapp.model;

public class StoreNotificationData {

    private String storeId;
    private String storeName;
    private String metaAtivados;
    private String qtdAprovacoes;
    private String qtdAprovacoesLy;
    private String qtdPropostas;
    private String metaPadraoPcj;
    private String metaPadraoParticipacao;
    private String phone;

    private String nivel;
    private String grupo;
    private String coordenadorCartao;
    private String regional;
    private String pcjPercentual;
    private String appPcjMeta;
    private String participacaoPercentual;
    private String appParticipacaoMeta;
    private String indicados;
    private String aprovados;
    private String qtdVendasNaoCartao;
    private String aproveitamentoPercentual;
    private String emprestimoPessoal;
    private String metaEmprestimoPessoal;

    public StoreNotificationData(
            String storeId,
            String storeName,
            String metaAtivados,
            String qtdAprovacoes,
            String qtdAprovacoesLy,
            String qtdPropostas,
            String metaPadraoPcj,
            String metaPadraoParticipacao,
            String phone) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.metaAtivados = metaAtivados;
        this.qtdAprovacoes = qtdAprovacoes;
        this.qtdAprovacoesLy = qtdAprovacoesLy;
        this.qtdPropostas = qtdPropostas;
        this.metaPadraoPcj = metaPadraoPcj;
        this.metaPadraoParticipacao = metaPadraoParticipacao;
        this.phone = phone;
    }

    public String getStoreId() { return storeId; }
    public String getStoreName() { return storeName; }
    public String getMetaAtivados() { return metaAtivados; }
    public String getQtdAprovacoes() { return qtdAprovacoes; }
    public String getQtdAprovacoesLy() { return qtdAprovacoesLy; }
    public String getQtdPropostas() { return qtdPropostas; }
    public String getMetaPadraoPcj() { return metaPadraoPcj; }
    public String getMetaPadraoParticipacao() { return metaPadraoParticipacao; }
    public String getPhone() { return phone; }

    public String getNivel() { return nivel; }
    public String getGrupo() { return grupo; }
    public String getCoordenadorCartao() { return coordenadorCartao; }
    public String getRegional() { return regional; }
    public String getPcjPercentual() { return pcjPercentual; }
    public String getAppPcjMeta() { return appPcjMeta; }
    public String getParticipacaoPercentual() { return participacaoPercentual; }
    public String getAppParticipacaoMeta() { return appParticipacaoMeta; }
    public String getIndicados() { return indicados; }
    public String getAprovados() { return aprovados; }
    public String getQtdVendasNaoCartao() { return qtdVendasNaoCartao; }
    public String getAproveitamentoPercentual() { return aproveitamentoPercentual; }
    public String getEmprestimoPessoal() { return emprestimoPessoal; }
    public String getMetaEmprestimoPessoal() { return metaEmprestimoPessoal; }

    public void setNivel(String nivel) { this.nivel = nivel; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public void setCoordenadorCartao(String coordenadorCartao) { this.coordenadorCartao = coordenadorCartao; }
    public void setRegional(String regional) { this.regional = regional; }
    public void setPcjPercentual(String pcjPercentual) { this.pcjPercentual = pcjPercentual; }
    public void setAppPcjMeta(String appPcjMeta) { this.appPcjMeta = appPcjMeta; }
    public void setParticipacaoPercentual(String participacaoPercentual) { this.participacaoPercentual = participacaoPercentual; }
    public void setAppParticipacaoMeta(String appParticipacaoMeta) { this.appParticipacaoMeta = appParticipacaoMeta; }
    public void setIndicados(String indicados) { this.indicados = indicados; }
    public void setAprovados(String aprovados) { this.aprovados = aprovados; }
    public void setQtdVendasNaoCartao(String qtdVendasNaoCartao) { this.qtdVendasNaoCartao = qtdVendasNaoCartao; }
    public void setAproveitamentoPercentual(String aproveitamentoPercentual) { this.aproveitamentoPercentual = aproveitamentoPercentual; }
    public void setEmprestimoPessoal(String emprestimoPessoal) { this.emprestimoPessoal = emprestimoPessoal; }
    public void setMetaEmprestimoPessoal(String metaEmprestimoPessoal) { this.metaEmprestimoPessoal = metaEmprestimoPessoal; }
}