package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.service.AtendimentoService;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.GuiaService;
import br.com.vah.guiabi.util.GenericLazyDataModel;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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

  private Long atendimentoId;

  private List<SelectItem> tipos;

  @PostConstruct
  public void init() {
    setSearchField("id");
    logger.info(this.getClass().getSimpleName() + " created");
    setItem(createNewItem());
    getItem().setSetor(session.getSetor());
    initLazyModel(service);
    tipos = TipoGuiaEnum.getSelectItems();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() != null) {
      updateAtendimentoId();
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

  public Long getAtendimentoId() {
    return atendimentoId;
  }

  public void setAtendimentoId(Long atendimentoId) {
    this.atendimentoId = atendimentoId;
  }

  private void inserirMensagemAtendimentoNaoEncontrado() {
    FacesContext.getCurrentInstance().addMessage("form:guiaForm:atendimento", new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Não foi possível localizar o atendimento"));
  }

  public void searchAtendimento() {
    if (atendimentoId == null) {
      getItem().setAtendimento(null);
    } else {
      try {
        Atendimento atendimento = atendimentoService.find(atendimentoId);
        if (atendimento == null) {
          getItem().setAtendimento(null);
          inserirMensagemAtendimentoNaoEncontrado();
        } else {
          getItem().setAtendimento(atendimento);
        }
      } catch (Exception e) {
        inserirMensagemAtendimentoNaoEncontrado();
      }
    }
  }

  public void updateAtendimentoId() {
    if(getItem().getAtendimento() == null){
      atendimentoId = null;
    } else {
      atendimentoId = getItem().getAtendimento().getId();
    }
  }

  @Override
  public void search() {
    resetSearchParams();
    searchById();
  }
}
