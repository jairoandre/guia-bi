package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.BaseEntity;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Setor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entidade que representa uma guia.
 * <p>
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "TB_GUIABI_GUIA", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = Guia.ALL, query = "SELECT g FROM Guia g"),
    @NamedQuery(name = Guia.COUNT, query = "SELECT COUNT(g) FROM Guia g")})
public class Guia extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "Guia.all";
  public static final String COUNT = "Guia.count";


  @Id
  @SequenceGenerator(name = "seqGuia", sequenceName = "SEQ_GUIABI_GUIA", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGuia")
  @Column(name = "ID")
  private Long id;

  @Column(name = "CD_TIPO", nullable = false)
  @Enumerated(EnumType.STRING)
  private TipoGuiaEnum tipo;

  @ManyToOne
  @JoinColumn(name = "CD_SETOR")
  private Setor setor;

  @ManyToOne
  @JoinColumn(name = "CD_ATENDIMENTO")
  private Atendimento atendimento;

  @Column(name = "CD_ESTADO", nullable = false)
  @Enumerated(EnumType.STRING)
  private EstadosGuiaEnum estado;

  @Column(name = "DT_GUIA")
  private Date data;

  @Column(name = "DT_RECEBIMENTO")
  private Date dataRecebimento;

  @Column(name = "DT_AUDITORIA")
  private Date dataAuditoria;

  @Column(name = "DT_SOLICITACAO_CONVENIO")
  private Date dataSolicitacaoConvenio;

  @Column(name = "DT_REPOSTA_CONVENIO")
  private Date dataRespostaConvenio;

  @OneToMany(mappedBy = "guia", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
  private List<HistoricoGuia> historico = new ArrayList<>();

  @Column(name = "CD_DESCRICAO")
  private String descricao;

  public Guia() {
    this.estado = EstadosGuiaEnum.PENDENTE;
  }

  public Guia(Atendimento atendimento, TipoGuiaEnum tipo) {
    this();
    this.atendimento = atendimento;
    this.tipo = tipo;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public TipoGuiaEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoGuiaEnum tipo) {
    this.tipo = tipo;
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  public Atendimento getAtendimento() {
    return atendimento;
  }

  public void setAtendimento(Atendimento atendimento) {
    this.atendimento = atendimento;
  }

  public EstadosGuiaEnum getEstado() {
    return estado;
  }

  public void setEstado(EstadosGuiaEnum estado) {
    this.estado = estado;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  public Date getDataRecebimento() {
    return dataRecebimento;
  }

  public void setDataRecebimento(Date dataRecebimento) {
    this.dataRecebimento = dataRecebimento;
  }

  public Date getDataAuditoria() {
    return dataAuditoria;
  }

  public void setDataAuditoria(Date dataAuditoria) {
    this.dataAuditoria = dataAuditoria;
  }

  public Date getDataSolicitacaoConvenio() {
    return dataSolicitacaoConvenio;
  }

  public void setDataSolicitacaoConvenio(Date dataSolicitacaoConvenio) {
    this.dataSolicitacaoConvenio = dataSolicitacaoConvenio;
  }

  public Date getDataRespostaConvenio() {
    return dataRespostaConvenio;
  }

  public void setDataRespostaConvenio(Date dataRespostaConvenio) {
    this.dataRespostaConvenio = dataRespostaConvenio;
  }

  public List<HistoricoGuia> getHistorico() {
    return historico;
  }

  public void setHistorico(List<HistoricoGuia> historico) {
    this.historico = historico;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
