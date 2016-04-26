package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jairoportela on 18/04/2016.
 */
@Entity
@Table(name = "TB_GUIABI_COMENTARIO", schema = "USRDBVAH")
public class Comentario extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqGuiaComentario", sequenceName = "SEQ_GUIABI_COMENTARIO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGuiaComentario")
  @Column(name = "ID")
  private Long id;

  @Column(name = "NM_COMENTARIO", nullable = false)
  private String comentario;

  @Column(name = "DT_CRIACAO")
  private Date data;

  @ManyToOne
  @JoinColumn(name = "ID_USER")
  private User autor;

  @ManyToOne
  @JoinColumn(name = "ID_GUIA")
  private Guia guia;

  public Comentario(){}

  public Comentario(Guia guia, User autor, String comentario, Date data) {
    this();
    this.guia = guia;
    this.autor = autor;
    this.comentario = comentario;
    this.data = data;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public Date getData() {
    return data;
  }

  public void setData(Date data) {
    this.data = data;
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

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
