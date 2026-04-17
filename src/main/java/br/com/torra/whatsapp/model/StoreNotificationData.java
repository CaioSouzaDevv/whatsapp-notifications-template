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

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getMetaAtivados() {
        return metaAtivados;
    }

    public String getQtdAprovacoes() {
        return qtdAprovacoes;
    }

    public String getQtdAprovacoesLy() {
        return qtdAprovacoesLy;
    }

    public String getQtdPropostas() {
        return qtdPropostas;
    }

    public String getMetaPadraoPcj() {
        return metaPadraoPcj;
    }

    public String getMetaPadraoParticipacao() {
        return metaPadraoParticipacao;
    }

    public String getPhone() {
        return phone;
    }
}