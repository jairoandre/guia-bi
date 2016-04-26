package br.com.vah.guiabi.converters;

import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.service.ConvenioService;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.SetorService;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ConvenioConverter extends GenericConverter<Convenio> {

  private
  @Inject
  ConvenioService service;

  @Override
  public DataAccessService<Convenio> getService() {
    return service;
  }
}