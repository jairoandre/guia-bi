package br.com.vah.guiabi.entities.dbamv;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "TB_ATENDIME", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Atendimento.ALL, query = "SELECT a FROM Atendimento a"),
    @NamedQuery(name = Atendimento.COUNT, query = "SELECT COUNT(a) FROM Atendimento a")})
public class Atendimento extends BaseEntity {

  public final static String ALL = "Atendimento.all";
  public final static String COUNT = "Atendimento.count";

  @Id
  @Column(name = "CD_ATENDIMENTO")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CD_PACIENTE")
  private Paciente paciente;

  @ManyToOne
  @JoinColumn(name = "CD_CONVENIO")
  private Convenio convenio;

  @Column(name = "DT_ALTA")
  private Date dataAlta;

  @Column(name = "CD_MULTI_EMPRESA")
  private Integer multiEmpresa;


  @Column(name = "CD_TIP_ACOM")
  private Integer tipoAcomodacao;
  
  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return String.format("%d - %s", id, paciente.getName());
  }

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }

  public Convenio getConvenio() {
    return convenio;
  }

  public void setConvenio(Convenio convenio) {
    this.convenio = convenio;
  }

  public Date getDataAlta() {
    return dataAlta;
  }

  public void setDataAlta(Date dataAlta) {
    this.dataAlta = dataAlta;
  }

  public Integer getMultiEmpresa() {
    return multiEmpresa;
  }

  public void setMultiEmpresa(Integer multiEmpresa) {
    this.multiEmpresa = multiEmpresa;
  }

  public Integer getTipoAcomodacao() {
	return tipoAcomodacao;
}

  public void setTipoAcomodacao(Integer tipoAcomodacao) {
    this.tipoAcomodacao = tipoAcomodacao;
  }

  @Override
  public String getLabelForSelectItem() {
    return String.format("%l - %s", id, paciente.getName());
  }
}
