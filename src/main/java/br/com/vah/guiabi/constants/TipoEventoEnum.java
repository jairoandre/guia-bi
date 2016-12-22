package br.com.vah.guiabi.constants;

/**
 * Created by jairoportela on 02/09/2016.
 */
public enum TipoEventoEnum {
  CRIACAO("Criação"),
  COMENTARIO("Comentário"),
  ATUALIZACAO("Atualização"),
  STATUS("Status");

  private String label;

  TipoEventoEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
