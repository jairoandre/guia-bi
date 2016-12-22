package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.entities.usrdbvah.GuiaSispag;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.GuiaSispagSrv;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
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

  private String pacienteTerm;
  private String convenioTerm;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    initLazyModel(service);
    prepareSearch();
  }

  @Override
  public void prepareSearch() {
    Map<String, Object> params = getLazyModel().getSearchParams().getParams();
    params.clear();
    if (!StringUtils.isEmpty(pacienteTerm)) {
      params.put("paciente", pacienteTerm.toUpperCase());
    }
    if (!StringUtils.isEmpty(convenioTerm)) {
      params.put("convenio", convenioTerm.toUpperCase());
    }
  }

  public void clearFilters() {
    getLazyModel().getSearchParams().getParams().clear();
    pacienteTerm = null;
    convenioTerm = null;
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

  public void preOpenModal(GuiaSispag guia) {
    setItem(service.initializeLists(guia));
  }

  public String getPacienteTerm() {
    return pacienteTerm;
  }

  public void setPacienteTerm(String pacienteTerm) {
    this.pacienteTerm = pacienteTerm;
  }

  public String getConvenioTerm() {
    return convenioTerm;
  }

  public void setConvenioTerm(String convenioTerm) {
    this.convenioTerm = convenioTerm;
  }
}
