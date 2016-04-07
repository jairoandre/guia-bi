package br.com.vah.guiabi.constants;

import java.util.Set;

import static br.com.vah.guiabi.constants.RolesEnum.*;

/**
 * Estados do lançamento.
 *
 * @author jairoportela
 */
public enum RestrictViewsEnum {
  USER_LIST("/pages/user/edit.xhtml", ADMINISTRATOR),
  USER_EDIT("/pages/user/list.xhtml", ADMINISTRATOR),
  CLIENT_EDIT("/pages/guia/edit.xhtml", ADMINISTRATOR, AUTHORIZER),
  SERVICE_EDIT("/pages/guia/edit.xhtml", ADMINISTRATOR, AUTHORIZER);

  private String view;

  private RolesEnum[] roles;

  RestrictViewsEnum(String view, RolesEnum... roles) {
    this.view = view;
    this.roles = roles;
  }

  public String getView() {
    return view;
  }

  public static RestrictViewsEnum getByView(String view) {
    for (RestrictViewsEnum viewEnum : RestrictViewsEnum.values()) {
      if (viewEnum.getView().equals(view)) {
        return viewEnum;
      }
    }
    return null;
  }

  public boolean checkRole(RolesEnum role) {
    boolean atLeastOne = false;
    for (RolesEnum thisRole : this.roles) {
      if(thisRole.equals(role)) {
        atLeastOne = true;
        break;
      }
    }
    return atLeastOne;
  }

}
