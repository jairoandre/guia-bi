package br.com.vah.guiabi.constants;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairoportela on 09/09/2016.
 */
public enum StatusEnum {
  CRIADA("Criada"),
  ANALISE("Em análise"),
  PENDENTE("Pendente"),
  AUTORIZADA("Autorizada"),
  PARCIAL("Autor. Parcial"),
  NEGADA("Negada"),
  LIBERADA("Liberada"),
  A_LIBERAR("A liberar"),
  EDICAO("Em edição"),
  A_SOLICITAR("A solicitar"),
  RE_ANALISE("Re-análise"),
  CANCELADA("Cancelada");

  private String label;

  StatusEnum(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public static List<SelectItem> getSelectItems() {
    List<SelectItem> selectItems = new ArrayList<>();
    selectItems.add(new SelectItem(null, "Escolha..."));
    for (StatusEnum tipo : StatusEnum.values()) {
      selectItems.add(new SelectItem(tipo, tipo.getLabel()));
    }
    return selectItems;
  }

  public Boolean contains(String value) {
    String[] names = value.split(",");
    for (String name : names) {
      try {
        StatusEnum _enum = StatusEnum.valueOf(name);
        if (this.equals(_enum)) {
          return true;
        }
      } catch (Exception e) {
      }
    }
    return false;
  }
}
