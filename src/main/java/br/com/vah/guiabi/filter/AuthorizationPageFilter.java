package br.com.vah.guiabi.filter;


import br.com.vah.guiabi.constants.RolesEnum;
import br.com.vah.guiabi.controllers.SessionCtrl;
import br.com.vah.guiabi.constants.RestrictViewsEnum;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.User;


import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * To prevent user from going back to Login page if the user already logged in
 *
 * @author Emre Simtay <emre@simtay.com>
 */
public class AuthorizationPageFilter implements Filter {

  private
  @Inject
  SessionCtrl sessionCtrl;

  public static final String ERROR_ACCESS_DENIED = "/error-access-denied.xhtml";
  public static final String LOGIN = "/login.xhtml";
  public static final String SELECIONAR_SETOR = "/selecionarSetor.xhtml";


  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (request.getUserPrincipal() != null) {

      User user = sessionCtrl.getUser();
      Setor setor = sessionCtrl.getSetor();
      List<Convenio> convenios = sessionCtrl.getConvenios();

      String[] splitPath = request.getRequestURI().split(request.getContextPath());

      String view = splitPath[0];

      if (splitPath.length > 1) {
        view = splitPath[1];
      }

      /**
       * Verifica se a view requisitada se encontra na enumeração de views restritas
       */
      RestrictViewsEnum restrictView = RestrictViewsEnum.getByView(view);

      if (restrictView == null || restrictView.checkRole(user.getRole())) {
        if (RolesEnum.AUTHORIZER.equals(user.getRole()) && !(setor != null || !convenios.isEmpty())) {
          response.sendRedirect(request.getContextPath() + SELECIONAR_SETOR);
        } else {
          filterChain.doFilter(servletRequest, servletResponse);
        }
      } else {
        response.sendRedirect(request.getContextPath() + ERROR_ACCESS_DENIED);
      }

    } else {
      // Usuário não logado
      response.sendRedirect(request.getContextPath() + LOGIN);
    }


  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }
}