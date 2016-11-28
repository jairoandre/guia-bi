package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.BaseEntity;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Especialidade;
import br.com.vah.guiabi.entities.dbamv.ProFat;
import br.com.vah.guiabi.entities.dbamv.Setor;

import javax.persistence.*;
import java.util.*;

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

  @ManyToOne
  @JoinColumn(name = "CD_ESPECIALIDADE")
  private Especialidade especialidade;

  @ManyToOne
  @JoinColumn(name = "CD_PRO_FAT")
  private ProFat proFat;

  @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
  @JoinTable(name = "TB_GUIABI_GUIA_PROFAT", joinColumns = {
      @JoinColumn(name = "ID")}, inverseJoinColumns = {@JoinColumn(name = "CD_PRO_FAT")}, schema = "USRDBVAH")
  private Set<ProFat> procedimentos = new LinkedHashSet<>();

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

  @Column(name = "DT_RESPOSTA_CONVENIO")
  private Date dataRespostaConvenio;

  @Column(name = "CD_DESCRICAO")
  private String descricao;

  @OneToMany(mappedBy = "guia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<HistoricoGuia> historico;

  @OneToMany(mappedBy = "guia", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<Comentario> comentarios;

  //@Column(name = "CD_CONTA")
  @Transient
  private Long conta;

  public Guia() {
    this.estado = EstadosGuiaEnum.PENDENTE;
    this.historico = new ArrayList<>();
    this.comentarios = new ArrayList<>();
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

  public Especialidade getEspecialidade() {
    return especialidade;
  }

  public void setEspecialidade(Especialidade especialidade) {
    this.especialidade = especialidade;
  }

  public ProFat getProFat() {
    return proFat;
  }

  public void setProFat(ProFat proFat) {
    this.proFat = proFat;
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

  public List<Comentario> getComentarios() {
    return comentarios;
  }

  public void setComentarios(List<Comentario> comentarios) {
    this.comentarios = comentarios;
  }

  public Set<ProFat> getProcedimentos() {
    return procedimentos;
  }

  public void setProcedimentos(Set<ProFat> procedimentos) {
    this.procedimentos = procedimentos;
  }

  public Long getConta() {
    return conta;
  }

  public void setConta(Long conta) {
    this.conta = conta;
  }

  @Override
  public String getLabelForSelectItem() {
    StringBuffer buffer = new StringBuffer();
    if (atendimento != null) {
      buffer.append(atendimento.getId());
      buffer.append(" - ");
      buffer.append(atendimento.getConvenio().getTitle());
    }
    return buffer.toString();
  }

  public String getProcedimentosStr() {
    if (procedimentos != null) {
      StringBuffer buffer = new StringBuffer();
      for (ProFat procedimento : procedimentos) {
        buffer.append(procedimento.getTitle());
        buffer.append(", ");
      }
      String bufferStr = buffer.toString();
      if (bufferStr.length() > 2) {
        return bufferStr.substring(0, bufferStr.length() - 2);
      }
    }
    return "Sem procedimentos";
  }
}
