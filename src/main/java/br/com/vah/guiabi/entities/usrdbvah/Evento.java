package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.TipoEventoEnum;
import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 01/09/2016.
 */
@Entity
@Table(name = "TB_SISPAG_EVENTO", schema = "USRDBVAH")
public class Evento extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqEvento", sequenceName = "SEQ_SISPAG_EVENTO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEvento")
  @Column(name = "ID")
  private Long id;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "CD_TIPO")
  private TipoEventoEnum tipo;

  @ManyToOne
  @JoinColumn(name = "ID_GUIA")
  private GuiaSispag guia;

  @ManyToOne
  @JoinColumn(name = "ID_USUARIO")
  private UserSispag usuario;

  @Column(name = "DT_EVENTO")
  private Date dataEvento;

  @Column(name = "NM_MENSAGEM")
  private String mensagem;

  @Column(name = "NM_DE")
  private String from;

  @Column(name = "NM_PARA")
  private String to;

  @Column(name = "NM_COMPLEMENTO")
  private String complemento;

  public Evento(){}

  public Evento(TipoEventoEnum tipo, GuiaSispag guia, UserSispag usuario) {
    this.tipo = tipo;
    this.guia = guia;
    this.usuario = usuario;
    this.dataEvento = new Date();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TipoEventoEnum getTipo() {
    return tipo;
  }

  public void setTipo(TipoEventoEnum tipo) {
    this.tipo = tipo;
  }

  public GuiaSispag getGuia() {
    return guia;
  }

  public void setGuia(GuiaSispag guia) {
    this.guia = guia;
  }

  public UserSispag getUsuario() {
    return usuario;
  }

  public void setUsuario(UserSispag usuario) {
    this.usuario = usuario;
  }

  public Date getDataEvento() {
    return dataEvento;
  }

  public void setDataEvento(Date dataEvento) {
    this.dataEvento = dataEvento;
  }

  public String getMensagem() {
    return mensagem;
  }

  public void setMensagem(String mensagem) {
    this.mensagem = mensagem;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getTexto() {
    if (tipo.equals(TipoEventoEnum.STATUS)) {
      return String.format("<b>Status %s</b>: de <i>[%s]</i> para <i>[%s]</i>.", complemento, from, to);
    } else if (TipoEventoEnum.ATUALIZACAO.equals(tipo) && (getMensagem() != null && !getMensagem().isEmpty())) {
      return String.format("<b>Atualização</b>: <i>%s</i>", mensagem);
    }
    return String.format("<b>%s</b>", tipo.getLabel());
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
