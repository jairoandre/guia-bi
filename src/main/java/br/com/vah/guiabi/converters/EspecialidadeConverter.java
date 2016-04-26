package br.com.vah.guiabi.converters;

import br.com.vah.guiabi.entities.dbamv.Especialidade;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.EspecialidadeService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EspecialidadeConverter extends GenericConverter<Especialidade> {

  private
  @Inject
  EspecialidadeService service;

  @Override
  public DataAccessService<Especialidade> getService() {
    return service;
  }
}