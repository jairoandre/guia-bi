package br.com.vah.guiabi.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.vah.guiabi.constants.RolesEnum;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.User;
import br.com.vah.guiabi.reports.ReportLoader;
import br.com.vah.guiabi.service.UserService;
import br.com.vah.guiabi.util.DateUtility;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

/**
 * Login Controller class allows only authenticated users to log in to the web
 * application.
 *
 * @author Emre Simtay <emre@simtay.com>
 */
@SuppressWarnings("serial")
@Named
@SessionScoped
public class SessionCtrl implements Serializable {

  private
  transient Logger logger = Logger.getLogger("LoginController");

  private
  @Inject
  UserService userService;


  private String username;
  private String password;
  private String conveniosStr;
  private User user;
  private Setor setor;
  private Convenio convenioToAdd;
  private List<Convenio> convenios = new ArrayList<>();


  /**
   * Creates a new instance of LoginController
   */
  public SessionCtrl() {
  }

  // Getters and Setters

  /**
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  public User getUser() {
    return user;
  }

  /**
   * Listen for button clicks on the #{loginController.login} action,
   * validates the username and password entered by the user and navigates to
   * the appropriate page.
   *
   * @param actionEvent
   */
  public void login(ActionEvent actionEvent) {

    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
    try {
      String navigateString = "/pages/index.xhtml";
      /**
       * Realiza autenticação
       */
      String usernameLC = this.username.toLowerCase();

      request.login(usernameLC, password);

      /**
       * Verifica se o usuário já está configurado no sistema
       */

      user = userService.findByLogin(usernameLC);


      /**
       * Primeiro acesso
       */
      if (user == null) {
        user = new User();
        user.setLogin(usernameLC);
        user = userService.create(user);
      }

      // gets the user principle and navigates to the appropriate page

      Principal principal = request.getUserPrincipal();

      try {
        logger.log(Level.INFO, "User ({0}) loging in #" + DateUtility.getCurrentDateTime(),
            request.getUserPrincipal().getName());

        if (RolesEnum.AUTHORIZER.equals(user.getRole())) {
          navigateString = "/pages/selecionarSetor.xhtml";
        }

        context.getExternalContext().redirect(request.getContextPath() + navigateString);
      } catch (IOException ex) {
        logger.log(Level.SEVERE, "IOException, Login Controller" + "Username : " + principal.getName(), ex);
        context.addMessage(null, new FacesMessage("Error!", "Exception occured"));
      }
    } catch (ServletException e) {
      e.printStackTrace();
      logger.log(Level.SEVERE, e.toString());
      context.addMessage(null, new FacesMessage("Erro!", "Usuário ou senha inválida."));
    }
  }

  /**
   * Listen for logout button clicks on the #{loginController.logout} action
   * and navigates to login screen.
   */
  public void logout() {

    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
        .getRequest();
    logger.log(Level.INFO, "User ({0}) loging out #" + DateUtility.getCurrentDateTime(),
        request.getUserPrincipal().getName());
    if (session != null) {
      session.invalidate();
    }
    FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
        .handleNavigation(FacesContext.getCurrentInstance(), null, "/login.xhtml?faces-redirect=true");
  }

  public boolean isUserInRoles(String roles) {
    boolean atLeastOneRole = false;
    for (String roleStr : roles.trim().split(",")) {
      RolesEnum role = RolesEnum.valueOf(roleStr);
      RolesEnum userRole = user.getRole();
      if (userRole.equals(role) || userRole.hasSubRole(role)) {
        atLeastOneRole = true;
        break;
      }
    }
    return atLeastOneRole;
  }

  public void addConvenioToList() {
    if (convenioToAdd != null) {
      if (!convenios.contains(convenioToAdd)) {
        convenios.add(convenioToAdd);
        conveniosStr = StringUtils.join(convenios, ",");
      }
      convenioToAdd = null;
    }
  }

  public void removeConvenio(Convenio convenio) {
    convenios.remove(convenio);
  }

  public String prosseguir() {
    return "/pages/guia/list.xhtml?faces-redirect=true";
  }

  public Setor getSetor() {
    return setor;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
  }

  public Convenio getConvenioToAdd() {
    return convenioToAdd;
  }

  public void setConvenioToAdd(Convenio convenioToAdd) {
    this.convenioToAdd = convenioToAdd;
  }



  public List<Convenio> getConvenios() {
    return convenios;
  }

  public void setConvenios(List<Convenio> convenios) {
    this.convenios = convenios;
  }

  public String getConveniosStr() {
    return conveniosStr;
  }

  public void onSetorSelect(SelectEvent event) {
    this.setor = (Setor) event.getObject();
  }
}