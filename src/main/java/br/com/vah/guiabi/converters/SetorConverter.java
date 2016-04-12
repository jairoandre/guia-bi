package br.com.vah.guiabi.converters;

import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.SetorService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class SetorConverter extends GenericConverter<Setor> {

  private
  @Inject
  SetorService service;

  @Override
  public DataAccessService<Setor> getService() {
    return service;
  }
}