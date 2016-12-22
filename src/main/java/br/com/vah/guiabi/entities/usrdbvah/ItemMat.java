package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by jairoportela on 01/09/2016.
 */
@Entity
@Table(name = "TB_SISPAG_IT_MAT", schema = "USRDBVAH")
public class ItemMat extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqItemMat", sequenceName = "SEQ_SISPAG_IT_MAT", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqItemMat")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_GUIA")
  private GuiaSispag guia;

  @Column(name = "NM_NOME")
  private String nome;

  @Column(name = "VL_QUANTIDADE")
  private Integer quantidade = 1;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GuiaSispag getGuia() {
    return guia;
  }

  public void setGuia(GuiaSispag guia) {
    this.guia = guia;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
