package br.com.vah.guiabi.entities.dbamv;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by Jairoportela on 05/04/2016.
 */
@Entity
@Table(name = "SETOR", schema = "DBAMV")
@NamedQueries({@NamedQuery(name = Setor.ALL, query = "SELECT s FROM Setor s"),
    @NamedQuery(name = Setor.COUNT, query = "SELECT COUNT(s) FROM Setor s")})
public class Setor extends BaseEntity {

  public final static String ALL = "Setor.all";
  public final static String COUNT = "Setor.count";

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CD_SETOR")
  private Long id;

  @Column(name = "NM_SETOR")
  private String title;

  /**
   * @return the id
   */
  @Override
  public Long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  @Override
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getLabelForSelectItem() {
    return getTitle();
  }


}
