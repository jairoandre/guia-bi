package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.service.FaturamentoSrv;
import org.primefaces.model.StreamedContent;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 19/12/2016.
 */
@Named
@ViewScoped
public class FaturamentoReportCtrl implements Serializable {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  FaturamentoSrv service;

  public StreamedContent getRelatorio() {
    try {
      return service.getReport();
    } catch (Exception e) {
      FacesContext ctx = FacesContext.getCurrentInstance();
      ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "erro", "relatório"));
      return null;
    }
  }


}
