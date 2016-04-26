package br.com.vah.guiabi.service;

import br.com.vah.guiabi.entities.dbamv.Especialidade;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class EspecialidadeService extends DataAccessService<Especialidade> {

  public EspecialidadeService() {
    super(Especialidade.class);
  }

}
