package br.com.vah.guiabi.entities.dbamv;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;

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

  @Override
  public String getLabelForSelectItem() {
    return String.format("%l - %s", id, paciente.getName());
  }
}
