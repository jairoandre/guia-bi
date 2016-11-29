package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.service.ConvenioService;
import br.com.vah.guiabi.service.DataAccessService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 29/11/2016.
 */
@Named
@ViewScoped
public class ConvenioCtrl extends AbstractCtrl<Convenio> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  ConvenioService service;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
  }


  @Override
  public DataAccessService<Convenio> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Convenio createNewItem() {
    return new Convenio();
  }

  @Override
  public String path() {
    return "convenio";
  }

  @Override
  public String getEntityName() {
    return "Convenio";
  }

  @Override
  public void prepareSearch() {
    setSearchParam("title", getSearchTerm());
  }

  public List<Convenio> completeMethod(String query) {
    setSearchTerm(query);
    prepareSearch();
    return getLazyModel().load(10);
  }
}
