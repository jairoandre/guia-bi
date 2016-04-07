package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public enum RolesEnum {
  ADMINISTRATOR("Administrador"),
  USER("Usuário"),
  AUTHORIZER("Autorizadora");

  private String label;

  private RolesEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static List<SelectItem> getSelectItems() {
    List<SelectItem> items = new ArrayList<>();
    items.add(new SelectItem(null, "Selecione..."));
    for (RolesEnum role : RolesEnum.values()) {
      items.add(new SelectItem(role, role.getLabel()));
    }
    return items;
  }
}
