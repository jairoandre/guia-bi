package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.entities.usrdbvah.GuiaSispag;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.GuiaSispagSrv;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 19/12/2016.
 */
@Named
@ViewScoped
public class GuiaSispagCtrl extends AbstractCtrl<GuiaSispag> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  GuiaSispagSrv service;

  private
  @Inject
  SessionCtrl sessionCtrl;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
    prepareSearch();
  }

  @Override
  public DataAccessService<GuiaSispag> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public GuiaSispag createNewItem() {
    return new GuiaSispag();
  }

  @Override
  public String path() {
    return "guias";
  }

  @Override
  public String getEntityName() {
    return "Guia";
  }

}
