package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Especialidade;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.service.*;
import br.com.vah.guiabi.util.DateUtility;
import br.com.vah.guiabi.util.ViewUtils;

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

  private
  @Inject
  ConvenioService convenioService;

  private
  @Inject
  EspecialidadeService especialidadeService;

  private List<SelectItem> tipos;

  private List<Convenio> convenios;

  private EstadosGuiaEnum[] estados;

  private TipoGuiaEnum[] tiposEnum;

  private Convenio[] selectedConvenios;

  private Boolean somenteDoSetor = true;

  private Boolean somenteMinhaAutoria = true;

  private String comentario;

  private Date inicioDate;

  private Date terminoDate;

  private String dateField = "G";

  private EstadosGuiaEnum[] selectedEstados;

  private TipoGuiaEnum[] selectedTipos;

  private Boolean dialogoRevisao = true;

  private Guia detachedGuia;

  public static final String[] RELATIONS = {"comentarios", "historico"};

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    initLazyModel(service, RELATIONS);
    prepareSearch();
    tipos = TipoGuiaEnum.getSelectItems();
    convenios = convenioService.findWithNamedQuery(Convenio.ALL);
    estados = EstadosGuiaEnum.values();
    tiposEnum = TipoGuiaEnum.values();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() == null) {
      getItem().setSetor(session.getSetor());
      service.addHistorico(session.getUser(), getItem(), AcoesGuiaEnum.CRIACAO);
    }
  }

  public void filterCurrentMonth() {
    Date[] thisMonth = ViewUtils.getDateRangeForThisMonth();
    inicioDate = thisMonth[0];
    terminoDate = thisMonth[1];
    prepareSearch();
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

  public Boolean getSomenteDoSetor() {
    return somenteDoSetor;
  }

  public void setSomenteDoSetor(Boolean somenteDoSetor) {
    this.somenteDoSetor = somenteDoSetor;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public List<Convenio> getConvenios() {
    return convenios;
  }

  public Convenio[] getSelectedConvenios() {
    return selectedConvenios;
  }

  public void setSelectedConvenios(Convenio[] selectedConvenios) {
    this.selectedConvenios = selectedConvenios;
  }

  public String getDateField() {
    return dateField;
  }

  public void setDateField(String dateField) {
    this.dateField = dateField;
  }

  public Date getInicioDate() {
    return inicioDate;
  }

  public void setInicioDate(Date inicioDate) {
    this.inicioDate = inicioDate;
  }

  public Date getTerminoDate() {
    return terminoDate;
  }

  public void setTerminoDate(Date terminoDate) {
    this.terminoDate = terminoDate;
  }

  public EstadosGuiaEnum[] getEstados() {
    return estados;
  }

  public TipoGuiaEnum[] getTiposEnum() {
    return tiposEnum;
  }

  public EstadosGuiaEnum[] getSelectedEstados() {
    return selectedEstados;
  }

  public void setSelectedEstados(EstadosGuiaEnum[] selectedEstados) {
    this.selectedEstados = selectedEstados;
  }

  public TipoGuiaEnum[] getSelectedTipos() {
    return selectedTipos;
  }

  public void setSelectedTipos(TipoGuiaEnum[] selectedTipos) {
    this.selectedTipos = selectedTipos;
  }

  public Boolean getDialogoRevisao() {
    return dialogoRevisao;
  }

  public Guia getDetachedGuia() {
    return detachedGuia;
  }

  public void setDetachedGuia(Guia detachedGuia) {
    this.detachedGuia = detachedGuia;
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

  public void clearSelectedConvenios() {
    this.selectedConvenios = null;
    prepareSearch();
  }

  public void clearSelectedEstados() {
    this.selectedEstados = null;
    prepareSearch();
  }

  public void clearSelectedTipos() {
    this.selectedTipos = null;
    prepareSearch();
  }

  public void receber(Guia guia) {
    Guia attachedGuia = service.find(guia.getId());
    attachedGuia.setDataRecebimento(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.RECEBIMENTO);
    setItem(attachedGuia);
    doSave();
    guia.setDataRecebimento(getItem().getDataRecebimento());
    guia.setHistorico(getItem().getHistorico());
  }

  public void auditar(Guia guia) {
    Guia attachedGuia = service.find(guia.getId());
    attachedGuia.setDataAuditoria(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.AUDITORIA);
    setItem(attachedGuia);
    doSave();
    guia.setDataAuditoria(getItem().getDataRecebimento());
    guia.setHistorico(getItem().getHistorico());
  }

  public void solicitarConvenio(Guia guia) {
    Guia attachedGuia = service.find(guia.getId());
    attachedGuia.setDataSolicitacaoConvenio(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.SOLICITACAO);
    setItem(guia);
    doSave();
    guia.setDataAuditoria(getItem().getDataRecebimento());
    guia.setHistorico(getItem().getHistorico());
  }

  public void negar(Guia guia) {
    Guia attachedGuia = service.find(guia.getId());
    attachedGuia.setDataRespostaConvenio(new Date());
    attachedGuia.setEstado(EstadosGuiaEnum.NEGADO);
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.NEGADO);
    setItem(attachedGuia);
    doSave();
    guia.setDataAuditoria(getItem().getDataRespostaConvenio());
    guia.setEstado(EstadosGuiaEnum.NEGADO);
    guia.setHistorico(getItem().getHistorico());
  }

  public void preRevisao(Guia guia) {
    dialogoRevisao = true;
    detachedGuia = guia;
  }

  public void preParcial(Guia guia) {
    dialogoRevisao = false;
    detachedGuia = guia;
  }

  private void saveAddingComment(EstadosGuiaEnum estado, AcoesGuiaEnum acao) {
    Guia attachedGuia = service.find(detachedGuia.getId());
    attachedGuia.setEstado(estado);
    service.addComentario(attachedGuia, session.getUser(), comentario);
    service.addHistorico(session.getUser(), attachedGuia, acao);
    comentario = null;
    setItem(attachedGuia);
    doSave();
    detachedGuia.setEstado(estado);
    detachedGuia.setComentarios(attachedGuia.getComentarios());
    detachedGuia.setHistorico(attachedGuia.getHistorico());
  }

  public void emRevisao() {
    saveAddingComment(EstadosGuiaEnum.REVISAO, AcoesGuiaEnum.REVISAO);
  }

  public void autorizarParcialmente() {
    saveAddingComment(EstadosGuiaEnum.PARCIAL, AcoesGuiaEnum.PARCIAL);
  }

  public void autorizar(Guia guia) {
    Guia attachedGuia = service.find(guia.getId());
    attachedGuia.setDataRespostaConvenio(new Date());
    attachedGuia.setEstado(EstadosGuiaEnum.AUTORIZADO);
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.AUTORIZADO);
    setItem(attachedGuia);
    doSave();
    guia.setEstado(EstadosGuiaEnum.AUTORIZADO);
    guia.setDataRespostaConvenio(attachedGuia.getDataRespostaConvenio());
    guia.setHistorico(attachedGuia.getHistorico());
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
    if(selectedEstados != null && selectedEstados.length > 0){
      setSearchParam("estados", selectedEstados);
    }
    if(selectedTipos != null && selectedTipos.length > 0){
      setSearchParam("tipos", selectedTipos);
    }
    if(somenteMinhaAutoria){
      setSearchParam("autor", session.getUser());
    }
    if(selectedConvenios != null &&  selectedConvenios.length > 0) {
      setSearchParam("convenios", selectedConvenios);
    }
    if(inicioDate != null || terminoDate != null){
      setSearchParam("dateRange", new Date[] {inicioDate, terminoDate});
      setSearchParam("dateField", dateField);
    }
  }
}
