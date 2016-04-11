package br.com.vah.guiabi.service;

import br.com.vah.guiabi.entities.dbamv.Setor;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class SetorService extends DataAccessService<Setor> {

  public SetorService() {
    super(Setor.class);
  }
}
