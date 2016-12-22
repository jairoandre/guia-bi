package br.com.vah.guiabi.entities.usrdbvah;

import br.com.vah.guiabi.entities.BaseEntity;

import javax.persistence.*;

/**
 * Created by jairoportela on 13/09/2016.
 */
@Entity
@Table(name = "TB_SISPAG_ANEXO", schema = "USRDBVAH")
public class AnexoSispag extends BaseEntity {

  @Id
  @SequenceGenerator(name = "seqAnexo", sequenceName = "SEQ_SISPAG_ANEXO", schema = "USRDBVAH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAnexo")
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "ID_GUIA")
  private GuiaSispag guia;

  @Column(name = "NM_ANEXO")
  private String nome;

  @Lob
  @Column(name = "VL_ARQUIVO")
  private byte[] arquivo;

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

  public byte[] getArquivo() {
    return arquivo;
  }

  public void setArquivo(byte[] arquivo) {
    this.arquivo = arquivo;
  }

  public Integer getTamanhoArquivo() {
    return arquivo.length;
  }

  @Override
  public String getLabelForSelectItem() {
    return null;
  }
}
