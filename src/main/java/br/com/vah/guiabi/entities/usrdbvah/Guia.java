package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.BaseEntity;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Setor;

import javax.persistence.*;
import java.util.Date;

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

  @Column(name = "DT_SECRETARIA")
  private Date dataSecretaria;

  @Column(name = "DT_FATURAMENTO")
  private Date dataFaturamento;

  @Column(name = "DT_AUDITORIA")
  private Date dataAuditoria;

  @Column(name = "DT_REPOSTA_CONVENIO")
  private Date dataRespostaConvenio;

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

  public Date getDataSecretaria() {
    return dataSecretaria;
  }

  public void setDataSecretaria(Date dataSecretaria) {
    this.dataSecretaria = dataSecretaria;
  }

  public Date getDataFaturamento() {
    return dataFaturamento;
  }

  public void setDataFaturamento(Date dataFaturamento) {
    this.dataFaturamento = dataFaturamento;
  }

  public Date getDataAuditoria() {
    return dataAuditoria;
  }

  public void setDataAuditoria(Date dataAuditoria) {
    this.dataAuditoria = dataAuditoria;
  }

  public Date getDataRespostaConvenio() {
    return dataRespostaConvenio;
  }

  public void setDataRespostaConvenio(Date dataRespostaConvenio) {
    this.dataRespostaConvenio = dataRespostaConvenio;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
