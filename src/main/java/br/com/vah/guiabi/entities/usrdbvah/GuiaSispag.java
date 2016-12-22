package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.StatusEnum;
import br.com.vah.guiabi.entities.BaseEntity;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Prestador;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TB_SISPAG_GUIA", schema = "USRDBVAH")
public class GuiaSispag extends BaseEntity {

  @Id
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "CD_TIPO", nullable = false)
  private Integer tipo;

  @Column(name = "NM_PACIENTE")
  private String paciente;

  @ManyToOne
  @JoinColumn(name = "CD_CONVENIO", nullable = false)
  private Convenio convenio;

  @ManyToOne
  @JoinColumn(name = "CD_PRESTADOR", nullable = false)
  private Prestador prestador;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "CD_STATUS", nullable = false)
  private StatusEnum status = StatusEnum.PENDENTE;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "CD_STATUS_OPME")
  private StatusEnum statusOpme;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "CD_STATUS_MAT")
  private StatusEnum statusMat;

  @Column(name = "CD_GUIA")
  private String numeroGuia;

  @Column(name = "CD_SOLICITACAO")
  private String numeroSolicitacao;

  @Column(name = "CD_SENHA")
  private String senha;

  @Column(name = "DT_EMISSAO")
  private Date dataEmissao;

  @Column(name = "DT_ENTREGA")
  private Date dataEntrega;

  @Column(name = "DT_RESPOSTA")
  private Date dataResposta;

  @Column(name = "DT_ALTERACAO")
  private Date dataAlteracao;

  @Column(name = "NM_TELEFONE")
  private String numeroTelefone;

  @Column(name = "CD_CARTAO")
  private String cartao;

  @Column(name = "CD_CPF")
  private String cpf;

  @Column(name = "SN_OPME")
  private Boolean opme = false;

  @Column(name = "SN_MATERIAL")
  private Boolean material = false;

  @OneToMany(mappedBy = "guia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id DESC")
  private Set<ItemGuia> itens = new LinkedHashSet<>();

  @OneToMany(mappedBy = "guia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id DESC")
  private Set<ItemOpme> opmes = new LinkedHashSet<>();

  @OneToMany(mappedBy = "guia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id DESC")
  private Set<ItemMat> materiais = new LinkedHashSet<>();

  @OneToMany(mappedBy = "guia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id DESC")
  private Set<Evento> eventos = new LinkedHashSet<>();

  @OneToMany(mappedBy = "guia", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id DESC")
  private Set<AnexoSispag> anexos = new LinkedHashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }

  public Integer getTipo() {
    return tipo;
  }

  public void setTipo(Integer tipo) {
    this.tipo = tipo;
  }

  public String getPaciente() {
    return paciente;
  }

  public void setPaciente(String paciente) {
    this.paciente = paciente;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public void setConvenio(Convenio convenio) {
    this.convenio = convenio;
  }

  public Prestador getPrestador() {
    return prestador;
  }

  public void setPrestador(Prestador prestador) {
    this.prestador = prestador;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public StatusEnum getStatusOpme() {
    return statusOpme;
  }

  public void setStatusOpme(StatusEnum statusOpme) {
    this.statusOpme = statusOpme;
  }

  public StatusEnum getStatusMat() {
    return statusMat;
  }

  public void setStatusMat(StatusEnum statusMat) {
    this.statusMat = statusMat;
  }

  public Date getDataEmissao() {
    return dataEmissao;
  }

  public void setDataEmissao(Date dataEmissao) {
    this.dataEmissao = dataEmissao;
  }

  public String getNumeroGuia() {
    return numeroGuia;
  }

  public void setNumeroGuia(String numeroGuia) {
    this.numeroGuia = numeroGuia;
  }

  public String getNumeroSolicitacao() {
    return numeroSolicitacao;
  }

  public void setNumeroSolicitacao(String numeroSolicitacao) {
    this.numeroSolicitacao = numeroSolicitacao;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Date getDataEntrega() {
    return dataEntrega;
  }

  public void setDataEntrega(Date dataEntrega) {
    this.dataEntrega = dataEntrega;
  }

  public Date getDataResposta() {
    return dataResposta;
  }

  public void setDataResposta(Date dataResposta) {
    this.dataResposta = dataResposta;
  }

  public Date getDataAlteracao() {
    return dataAlteracao;
  }

  public void setDataAlteracao(Date dataAlteracao) {
    this.dataAlteracao = dataAlteracao;
  }

  public String getNumeroTelefone() {
    return numeroTelefone;
  }

  public void setNumeroTelefone(String numeroTelefone) {
    this.numeroTelefone = numeroTelefone;
  }

  public String getCartao() {
    return cartao;
  }

  public void setCartao(String cartao) {
    this.cartao = cartao;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public Boolean getOpme() {
    return opme;
  }

  public void setOpme(Boolean opme) {
    this.opme = opme;
  }

  public Boolean getMaterial() {
    return material;
  }

  public void setMaterial(Boolean material) {
    this.material = material;
  }

  public Set<ItemGuia> getItens() {
    return itens;
  }

  public void setItens(Set<ItemGuia> itens) {
    this.itens = itens;
  }

  public Set<ItemOpme> getOpmes() {
    return opmes;
  }

  public void setOpmes(Set<ItemOpme> opmes) {
    this.opmes = opmes;
  }

  public Set<ItemMat> getMateriais() {
    return materiais;
  }

  public void setMateriais(Set<ItemMat> materiais) {
    this.materiais = materiais;
  }

  public Set<Evento> getEventos() {
    return eventos;
  }

  public void setEventos(Set<Evento> eventos) {
    this.eventos = eventos;
  }

  public Set<AnexoSispag> getAnexos() {
    return anexos;
  }

  public void setAnexos(Set<AnexoSispag> anexos) {
    this.anexos = anexos;
  }
}

