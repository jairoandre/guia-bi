package br.com.vah.guiabi.converters;

import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.service.AtendimentoService;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.SetorService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class AtendimentoConverter extends GenericConverter<Atendimento> {

  private @Inject
  AtendimentoService service;


  @Override
  public DataAccessService<Atendimento> getService() {
    return service;
  }
}