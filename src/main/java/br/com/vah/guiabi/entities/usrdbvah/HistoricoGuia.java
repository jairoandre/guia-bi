package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Entity
@Table(name = "TB_GUIABI_HISTORICO", schema = "USRDBVAH")
public class HistoricoGuia extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqHistoricoGuia", sequenceName = "SEQ_GUIABI_HISTORICO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqHistoricoGuia")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_USER")
  private User autor;

  @ManyToOne
  @JoinColumn(name = "ID_GUIA")
  private Guia guia;

  @Column(name = "DT_ALTERACAO")
  private Date data;

  public HistoricoGuia() {
    this.data = new Date();
  }

  public HistoricoGuia(User autor, Guia guia) {
    this();
    this.autor = autor;
    this.guia = guia;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public User getAutor() {
    return autor;
  }

  public void setAutor(User autor) {
    this.autor = autor;
  }

  public Guia getGuia() {
    return guia;
  }

  public void setGuia(Guia guia) {
    this.guia = guia;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
