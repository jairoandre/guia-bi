package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.StatusEnum;
import br.com.vah.guiabi.entities.BaseEntity;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Prestador;

import javax.persistence.*;
import java.util.Date;

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

  @Column(name = "DT_EMISSAO")
  private Date dataEmissao;

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

  public Date getDataEmissao() {
    return dataEmissao;
  }

  public void setDataEmissao(Date dataEmissao) {
    this.dataEmissao = dataEmissao;
  }
}

