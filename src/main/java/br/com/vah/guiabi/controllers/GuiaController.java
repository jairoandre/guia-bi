package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.service.AtendimentoService;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.GuiaService;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Named
@ViewScoped
public class GuiaController extends AbstractController<Guia> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  GuiaService service;

  private
  @Inject
  SessionController session;

  private
  @Inject
  AtendimentoService atendimentoService;

  private List<SelectItem> tipos;

  private Boolean somenteDoSetor = true;

  private Boolean somentePendentes = false;

  private Boolean somenteMinhaAutoria = true;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    initLazyModel(service);
    getLazyModel().getSearchParams().addRelations("historico");
    prepareSearch();
    tipos = TipoGuiaEnum.getSelectItems();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() == null) {
      getItem().setSetor(session.getSetor());
      service.addHistorico(session.getUser(), getItem(), AcoesGuiaEnum.CRIACAO);
    }
  }


  @Override
  public DataAccessService<Guia> getService() {
    return service;
  }

  @Override
  public Logger getLogger() {
    return logger;
  }

  @Override
  public Guia createNewItem() {
    return new Guia();
  }

  @Override
  public String path() {
    return "guia";
  }

  @Override
  public String getEntityName() {
    return "Guia";
  }

  public List<SelectItem> getTipos() {
    return tipos;
  }

  public Boolean getSomenteMinhaAutoria() {
    return somenteMinhaAutoria;
  }

  public void setSomenteMinhaAutoria(Boolean somenteMinhaAutoria) {
    this.somenteMinhaAutoria = somenteMinhaAutoria;
  }

  public Boolean getSomentePendentes() {
    return somentePendentes;
  }

  public void setSomentePendentes(Boolean somentePendentes) {
    this.somentePendentes = somentePendentes;
  }

  public Boolean getSomenteDoSetor() {
    return somenteDoSetor;
  }

  public void setSomenteDoSetor(Boolean somenteDoSetor) {
    this.somenteDoSetor = somenteDoSetor;
  }

  public void onchangeTipo() {
    if (TipoGuiaEnum.PRORROGACAO.equals(getItem().getTipo())) {
      getItem().setData(new Date());
      getItem().setDataRecebimento(null);
    } else {
      getItem().setData(null);
      getItem().setDataRecebimento(new Date());
    }

  }

  public void receber(Guia guia) {
    guia.setDataRecebimento(new Date());
    service.addHistorico(session.getUser(), guia, AcoesGuiaEnum.RECEBIMENTO);
    setItem(guia);
    doSave();
  }

  public void auditar(Guia guia) {
    guia.setDataAuditoria(new Date());
    service.addHistorico(session.getUser(), guia, AcoesGuiaEnum.AUDITORIA);
    setItem(guia);
    doSave();
  }

  public void solicitarConvenio(Guia guia) {
    guia.setDataSolicitacaoConvenio(new Date());
    service.addHistorico(session.getUser(), guia, AcoesGuiaEnum.SOLICITACAO);
    setItem(guia);
    doSave();
  }

  public void respostaConvenio(Guia guia) {
    guia.setDataRespostaConvenio(new Date());
    guia.setEstado(EstadosGuiaEnum.FINALIZADO);
    service.addHistorico(session.getUser(), guia, AcoesGuiaEnum.RESPOSTA);
    setItem(guia);
    doSave();
  }

  @Override
  public void prepareSearch() {
    resetSearchParams();
    String regex = "[0-9]+";
    if (getSearchTerm() != null && getSearchTerm().matches(regex)) {
      setSearchParam("atendimento", Long.valueOf(getSearchTerm()));
    }else {
      setSearchParam("paciente", getSearchTerm());
    }
    if(somenteDoSetor){
      setSearchParam("setor", session.getSetor());
    }
    if(somentePendentes){
      setSearchParam("estado", EstadosGuiaEnum.PENDENTE);
    }
    if(somenteMinhaAutoria){
      setSearchParam("autor", session.getUser());
    }
  }
}
