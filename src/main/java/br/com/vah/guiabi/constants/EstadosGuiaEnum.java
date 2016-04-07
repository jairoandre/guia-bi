package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jairoportela on 07/04/2016.
 */
public enum EstadosGuiaEnum {
  PENDENTE("Pendente"),
  AUTORIZADO("Autorizado"),
  NEGADO("Negado");

  private String label;

  private EstadosGuiaEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static List<SelectItem> getSelectItems() {
    List<SelectItem> items = new ArrayList<>();
    items.add(new SelectItem(null, "Selecione..."));
    for (EstadosGuiaEnum estado : EstadosGuiaEnum.values()) {
      items.add(new SelectItem(estado, estado.getLabel()));
    }
    return items;
  }


}
