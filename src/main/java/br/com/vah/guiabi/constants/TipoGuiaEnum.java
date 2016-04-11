package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jairoportela on 05/04/2016.
 */
public enum TipoGuiaEnum {
  INTERNACAO("Internação"),
  CONSULTA("Consulta"),
  PRORROGACAO("Prorrogação"),
  PROCEDIMENTO("Procedimento"),
  OPME("OPME"),
  QUIMIOTERAPIA("Quimioterapia"),
  RADIOTERAPIA("Radioterapia"),
  PARECER("Parecer"),
  BIBAP("Bibap"),
  OUTROS("Outros");

  private String label;

  TipoGuiaEnum(String label) {
    this.label = label;
  }

  private String getLabel() {
    return label;
  }

  public static List<SelectItem> getSelectItems() {
    List<SelectItem> items = new ArrayList<>();
    items.add(new SelectItem(null, "Selecione..."));
    for (TipoGuiaEnum tipo : TipoGuiaEnum.values()) {
      items.add(new SelectItem(tipo, tipo.getLabel()));
    }
    return items;
  }

}
