package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jairoportela on 07/04/2016.
 */
public enum EstadosGuiaEnum {
  PENDENTE("Pendente", "fa fa-warning", "warning-bg"),
  NEGADO("Negado", "fa fa-thumbs-down", "deny-bg"),
  REVISAO("Revisao", "fa fa-thumb-tack", "review-bg"),
  PARCIAL("Autorizado parcialmente", "fa fa-thumbs-up", "partial-bg"),
  AUTORIZADO("Autorizado", "fa fa-thumbs-up", "authorized-bg");

  private String label;
  private String icon;
  private String colors;

  EstadosGuiaEnum(String label, String styleClass, String colors) {
    this.label = label;
    this.icon = styleClass;
    this.colors = colors;
  }

  public String getLabel() {
    return label;
  }

  public String getIcon() {
    return icon;
  }

  public String getColors() {
    return colors;
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
