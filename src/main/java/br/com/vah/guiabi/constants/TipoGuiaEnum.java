package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jairoportela on 05/04/2016.
 */
public enum TipoGuiaEnum {
  INTERNACAO,
  CONSULTA,
  PRORROGACAO,
  PROCEDIMENTO,
  OPME,
  QUIMIOTERAPIA,
  RADIOTERAPIA,
  PARECER,
  BIBAP,
  OUTROS;

  private String getLabel() {
    switch (this) {
      case INTERNACAO:
        return "Internação";
      case CONSULTA:
        return "Consulta";
      case PRORROGACAO:
        return "Prorrogação";
      case PROCEDIMENTO:
        return "Procedimento";
      case OPME:
        return "OPME";
      case QUIMIOTERAPIA:
        return "Quimioterapia";
      case RADIOTERAPIA:
        return "Radioterapia";
      case PARECER:
        return "Parecer";
      case BIBAP:
        return "BIBAP";
      case OUTROS:
        return "Outros";
      default:
        return "N/A";
    }
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
