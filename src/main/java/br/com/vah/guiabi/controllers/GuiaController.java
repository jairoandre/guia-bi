package br.com.vah.guiabi.controllers;

import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Especialidade;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.service.*;
import br.com.vah.guiabi.util.DateUtility;
import br.com.vah.guiabi.util.ViewUtils;
import com.opencsv.CSVReader;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
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

  public void uploadValues(FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    byte[] data = file.getContents();
    CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(data)), ';');
    try {
      List<Guia> guias = new ArrayList<>();
      Integer line = 1;
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (String[] str : reader.readAll()) {
          Long idSector = Long.valueOf(str[0]);
          Long idConvenio = Long.valueOf(str[1]);
          Long cdAtendimento = Long.valueOf(str[2]);
          TipoGuiaEnum tipoGuia = TipoGuiaEnum.valueOf(str[3]);
          String descricao = str[4];
          Date dataGuia = str[5] == null || str[5].isEmpty() ? null : sdf.parse(str[5]);
          Date dataReceb = str[6] == null || str[6].isEmpty() ? null : sdf.parse(str[6]);
          Date dataAudit = str[7] == null || str[7].isEmpty() ? null : sdf.parse(str[7]);
          Date dataSolic = str[8] == null || str[8].isEmpty() ? null : sdf.parse(str[8]);
          Date dataRespo = str[9] == null || str[9].isEmpty() ? null : sdf.parse(str[9]);


          Guia guia = new Guia();

          Setor setor = new Setor();
          setor.setId(idSector);



          line++;

          guias.add(guia);
        }

        // service.saveAll(guias);

        Integer importedValues = guias.size();
        addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Importação realizada com sucesso: %d importados.", importedValues)), false);
      } catch (Exception e) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação: linha %s.", line)), false);
      }
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação:\n%s", e.getMessage())), false);
    }

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
    guia.setDataAuditoria(getItem().getDataAuditoria());
    guia.setHistorico(getItem().getHistorico());
  }

  public void solicitarConvenio(Guia guia) {
    Guia attachedGuia = service.find(guia.getId());
    attachedGuia.setDataSolicitacaoConvenio(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.SOLICITACAO);
    setItem(attachedGuia);
    doSave();
    guia.setDataSolicitacaoConvenio(getItem().getDataSolicitacaoConvenio());
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
    if(estado.equals(EstadosGuiaEnum.PARCIAL)) {
      attachedGuia.setDataRespostaConvenio(new Date());
      detachedGuia.setDataRespostaConvenio(attachedGuia.getDataRespostaConvenio());
    }
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
