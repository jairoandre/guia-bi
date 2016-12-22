package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.constants.RolesSispagEnum;
import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "TB_SISPAG_USUARIO", schema = "USRDBVAH")
@NamedQueries({@NamedQuery(name = UserSispag.ALL, query = "SELECT u FROM User u"),
    @NamedQuery(name = UserSispag.COUNT, query = "SELECT COUNT(u) FROM User u"),
    @NamedQuery(name = UserSispag.FIND_BY_LOGIN, query = "SELECT u FROM User u where u.login = :login")})
public class UserSispag extends BaseEntity {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String ALL = "UserSispag.populatedItems";
  public static final String COUNT = "UserSispag.countTotal";
  public static final String FIND_BY_LOGIN = "UserSispag.findByLogin";

  @Id
  @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_SISPAG_USUARIO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
  @Column(name = "ID")
  private Long id;

  @Column(name = "DS_LOGIN", nullable = false, unique = true)
  private String login;

  @Column(name = "NM_TITULO", nullable = true, length = 75)
  private String title;

  @Column(name = "NM_EMAIL", length = 75)
  private String email;

  @Column(name = "CD_ROLE", nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private RolesSispagEnum role;

  public UserSispag() {
    role = RolesSispagEnum.USUARIO;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;

  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public RolesSispagEnum getRole() {
    return role;
  }

  public void setRole(RolesSispagEnum role) {
    this.role = role;
  }

  @Override
  public String getLabelForSelectItem() {
    return login;
  }

}