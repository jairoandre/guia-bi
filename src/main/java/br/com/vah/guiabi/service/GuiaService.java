package br.com.vah.guiabi.service;


import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.HistoricoGuia;
import br.com.vah.guiabi.entities.usrdbvah.User;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class GuiaService extends DataAccessService<Guia> {

  public GuiaService() {
    super(Guia.class);
  }

  public void addHistorico(User author, Guia guia) {
    HistoricoGuia historicoGuia = new HistoricoGuia(author, guia);
    guia.getHistorico().add(historicoGuia);
  }

}
