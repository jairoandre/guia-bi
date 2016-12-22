package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public enum RolesSispagEnum {
  USUARIO("Usuário"),
  RECEPCAO("Recepção", USUARIO),
  CIRURGICO("Cirúrgico", USUARIO),
  ADMINISTRADOR("Administrador", CIRURGICO, RECEPCAO);

  private String label;
  private RolesSispagEnum[] subroles;

  RolesSispagEnum(String label, RolesSispagEnum... subroles) {
    this.label = label;
    this.subroles = subroles;
  }

  public String getLabel() {
    return label;
  }

  public static List<SelectItem> getSelectItems() {
    List<SelectItem> items = new ArrayList<>();
    items.add(new SelectItem(null, "Selecione..."));
    for (RolesSispagEnum role : RolesSispagEnum.values()) {
      items.add(new SelectItem(role, role.getLabel()));
    }
    return items;
  }

  public boolean hasSubRole(RolesSispagEnum role) {
    if (this.equals(role)) {
      return true;
    } else if (this.subroles != null) {
      for (RolesSispagEnum subrole : this.subroles) {
        if (subrole.hasSubRole(role)) {
          return true;
        }
      }

    }
    return false;
  }
}
