package br.com.vah.guiabi.filter;


import br.com.vah.guiabi.controllers.LoginController;


import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * To prevent user from going back to Login page if the user already logged in
 *
 * @author Emre Simtay <emre@simtay.com>
 */
public class AuthorizationPageFilter implements Filter {

  private
  @Inject
  LoginController loginController;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (request.getUserPrincipal() != null) {

      //User user = loginController.getUser();

      String[] splitPath = request.getRequestURI().split(request.getContextPath());

      String view = splitPath[0];

      if (splitPath.length > 1) {
        view = splitPath[1];
      }
      
      filterChain.doFilter(servletRequest, servletResponse);

      /**
       * Verifica se a view requisitada se encontra na enumeração de views restritas
       */
      /*RestrictViewsEnum restrictView = RestrictViewsEnum.getByView(view);

      if (restrictView == null || restrictView.checkRoles(user.getRoles())) {
        filterChain.doFilter(servletRequest, servletResponse);
      } else {
        response.sendRedirect(request.getContextPath() + "/error-access-denied.xhtml");
      }*/
      
    } else {
      // Usuário não logado
      response.sendRedirect(request.getContextPath() + "/login.xhtml");
    }
    
    
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }
}