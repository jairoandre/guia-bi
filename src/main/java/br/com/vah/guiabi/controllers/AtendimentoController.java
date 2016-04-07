package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.service.AtendimentoService;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.SetorService;
import br.com.vah.guiabi.util.GenericLazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Named
@ViewScoped
public class AtendimentoController extends AbstractController<Atendimento> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  AtendimentoService service;

  @PostConstruct
  public void init() {
    setSearchField("id");
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public DataAccessService<Atendimento> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Atendimento createNewItem() {
    return new Atendimento();
  }

  @Override
  public String path() {
    return "atendimento";
  }

  @Override
  public String getEntityName() {
    return "Atendimento";
  }

  @Override
  public void search() {
    resetSearchParams();
    searchById();
  }
}
