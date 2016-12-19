package br.com.vah.guiabi.reports;

import java.math.BigDecimal;

/**
 * Created by Jairoportela on 16/12/2016.
 */
public class Faturamento {

  private String grupo;
  private String convenio;
  private Integer ge30Qtd = 0;
  private Integer ge30Dias = 0;
  private BigDecimal ge30Total = BigDecimal.ZERO;
  private Integer lt30Qtd = 0;
  private Integer lt30Dias = 0;
  private BigDecimal lt30Total = BigDecimal.ZERO;
  private Integer guiasQtd = 0;
  private Integer guiasDias = 0;

  public String getGrupo() {
    return grupo;
  }

  public void setGrupo(String grupo) {
    this.grupo = grupo;
  }

  public String getConvenio() {
    return convenio;
  }

  public void setConvenio(String convenio) {
    this.convenio = convenio;
  }

  public Integer getGe30Qtd() {
    return ge30Qtd;
  }

  public void setGe30Qtd(Integer ge30Qtd) {
    this.ge30Qtd = ge30Qtd;
  }

  public Integer getGe30Dias() {
    return ge30Dias;
  }

  public void setGe30Dias(Integer ge30Dias) {
    this.ge30Dias = ge30Dias;
  }

  public BigDecimal getGe30Total() {
    return ge30Total;
  }

  public void setGe30Total(BigDecimal ge30Total) {
    this.ge30Total = ge30Total;
  }

  public Integer getLt30Qtd() {
    return lt30Qtd;
  }

  public void setLt30Qtd(Integer lt30Qtd) {
    this.lt30Qtd = lt30Qtd;
  }

  public Integer getLt30Dias() {
    return lt30Dias;
  }

  public void setLt30Dias(Integer lt30Dias) {
    this.lt30Dias = lt30Dias;
  }

  public BigDecimal getLt30Total() {
    return lt30Total;
  }

  public void setLt30Total(BigDecimal lt30Total) {
    this.lt30Total = lt30Total;
  }

  public Integer getGuiasQtd() {
    return guiasQtd;
  }

  public void setGuiasQtd(Integer guiasQtd) {
    this.guiasQtd = guiasQtd;
  }

  public Integer getGuiasDias() {
    return guiasDias;
  }

  public void setGuiasDias(Integer guiasDias) {
    this.guiasDias = guiasDias;
  }
}
