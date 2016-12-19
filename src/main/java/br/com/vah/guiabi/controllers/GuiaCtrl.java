package br.com.vah.guiabi.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.opencsv.CSVReader;

import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.AcoesRespostaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.GuiaFieldsEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.ProFat;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.Anexo;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.HistoricoGuia;
import br.com.vah.guiabi.entities.usrdbvah.User;
import br.com.vah.guiabi.exceptions.GuiaPersistException;
import br.com.vah.guiabi.service.AtendimentoService;
import br.com.vah.guiabi.service.ConvenioService;
import br.com.vah.guiabi.service.DataAccessService;
import br.com.vah.guiabi.service.EspecialidadeService;
import br.com.vah.guiabi.service.GuiaService;
import br.com.vah.guiabi.util.ViewUtils;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Named
@ViewScoped
public class GuiaCtrl extends AbstractCtrl<Guia> {

  private
  @Inject
  transient Logger logger;

  private
  @Inject
  GuiaService service;

  private
  @Inject
  SessionCtrl session;

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

  private Setor setor;

  private Boolean somenteMinhaAutoria = true;

  private Boolean somenteSemRecebimentos = false;

  private String comentario;

  private Date inicioDate;

  private Date terminoDate;

  private String dateField = "G";

  private EstadosGuiaEnum[] selectedEstados;

  private TipoGuiaEnum[] selectedTipos;

  private List<String> filtros;

  private Map<String, GuiaFieldsEnum> mapFiltros;

  public static final String[] RELATIONS = {"comentarios", "historico", "procedimentos"};

  private ProFat proFatToAdd;

  private AcoesRespostaEnum acaoResposta;

  private Date dataRespostaAnterior;

  private Boolean respostaAtual;

  private String modal;

  @PostConstruct
  public void init() {
    logger.info(this.getClass().getSimpleName() + " created");
    initLazyModel(service, RELATIONS);
    getLazyModel().getSearchParams().setOrderBy("data");
    getLazyModel().getSearchParams().setAsc(false);
    prepareSearch();
    tipos = TipoGuiaEnum.getSelectItems();
    if (session.getConvenios().isEmpty()) {
      convenios = convenioService.findWithNamedQuery(Convenio.ALL);
    } else {
      if (session.getConvenios().size() > 1) {
        convenios = session.getConvenios();
      }
    }
    estados = EstadosGuiaEnum.values();
    tiposEnum = TipoGuiaEnum.values();
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getItem().getId() == null) {
      getItem().setSetor(session.getSetor());
      service.addHistorico(session.getUser(), getItem(), AcoesGuiaEnum.CRIACAO);
    } else {
      setItem(service.carregarListas(getItem()));
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

  public void removerAnexo(Anexo anexo) {
    getItem().getAnexos().remove(anexo);
  }

  public void uploadAnexo(FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    Anexo anexo = new Anexo();
    anexo.setGuia(getItem());
    anexo.setNmAnexo(file.getFileName());
    anexo.setArquivo(file.getContents());
    getItem().getAnexos().add(anexo);
  }

  public StreamedContent download(Anexo anexo) {
    InputStream report = new ByteArrayInputStream(anexo.getArquivo());
    DefaultStreamedContent dsc = new DefaultStreamedContent(report);
    dsc.setContentType("application/pdf");
    dsc.setName(anexo.getNmAnexo());
    return dsc;
  }

  public void setSetor(Setor setor) {
    this.setor = setor;
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

  public ProFat getProFatToAdd() {
    return proFatToAdd;
  }

  public void setProFatToAdd(ProFat proFatToAdd) {
    this.proFatToAdd = proFatToAdd;
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

  public Setor getSetor() {
    return setor;
  }

  public Boolean getRespostaAtual() {
    return respostaAtual;
  }

  public void setRespostaAtual(Boolean respostaAtual) {
    this.respostaAtual = respostaAtual;
  }

  public Date getDataRespostaAnterior() {
    return dataRespostaAnterior;
  }

  public void setDataRespostaAnterior(Date dataRespostaAnterior) {
    this.dataRespostaAnterior = dataRespostaAnterior;
  }

  public AcoesRespostaEnum getAcaoResposta() {
    return acaoResposta;
  }

  public void setAcaoResposta(AcoesRespostaEnum acaoResposta) {
    this.acaoResposta = acaoResposta;
  }

  public void preResposta(Guia guia, AcoesRespostaEnum acao) {
    setItem(service.carregarHistoricoEComentarios(guia));
    this.modal = "registrarRespostaDlg";
    this.respostaAtual = true;
    this.acaoResposta = acao;
  }

  public String registrarResposta() {
    getItem().setDataRespostaAnterior(dataRespostaAnterior);
    switch (acaoResposta) {
      case NEGADO:
        negar();
        break;
      case EM_REVISAO:
        emRevisao();
        break;
      case PARCIALMENTE:
        autorizarParcialmente();
        break;
      case AUTORIZADO:
        autorizar();
        break;
    }
    dataRespostaAnterior = null;
    acaoResposta = null;
    return "";
  }

  public void uploadValues(FileUploadEvent evt) {
    UploadedFile file = evt.getFile();
    byte[] data = file.getContents();
    CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(data)), ';');
    try {
      List<Guia> guias = new ArrayList<>();
      Integer line = 0;
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

        for (String[] str : reader.readAll()) {

          line++;

          Long idSector = Long.valueOf(str[0]);
          Long cdAtendimento = Long.valueOf(str[1]);
          if (cdAtendimento == null) {
            continue;
          }
          Atendimento atendimento = atendimentoService.find(cdAtendimento);

          TipoGuiaEnum tipoGuia = TipoGuiaEnum.valueOf(str[2]);

          String descricao = str[3];

          Date dataGuia = str[4] == null || str[4].isEmpty() ? null : sdf.parse(str[4]);
          Date dataReceb = str[5] == null || str[5].isEmpty() ? null : sdf.parse(str[5]);
          Date dataAudit = str[6] == null || str[6].isEmpty() ? null : sdf.parse(str[6]);
          Date dataSolic = str[7] == null || str[7].isEmpty() ? null : sdf.parse(str[7]);
          Date dataRespo = str[8] == null || str[8].isEmpty() ? null : sdf.parse(str[8]);
          Long idUser = Long.valueOf(str[9]);

          User user = new User();

          user.setId(idUser);

          Guia guia = new Guia(atendimento, tipoGuia);

          Setor setor = new Setor();
          setor.setId(idSector);
          guia.setSetor(setor);
          guia.setDescricao(descricao);
          guia.setData(dataGuia);
          guia.setDataRecebimento(dataReceb);
          guia.setDataAuditoria(dataAudit);
          guia.setDataSolicitacaoConvenio(dataSolic);

          guia.getHistorico().add(new HistoricoGuia(user, guia, AcoesGuiaEnum.CRIACAO));

          if (dataRespo != null) {
            guia.setEstado(EstadosGuiaEnum.AUTORIZADO);
            guia.getHistorico().add(new HistoricoGuia(user, guia, AcoesGuiaEnum.AUTORIZADO));
            guia.setDataRespostaConvenio(dataRespo);
          }
          guias.add(guia);
        }

        service.saveAll(guias);

        Integer importedValues = guias.size();
        Integer ignoredValues = line - importedValues;
        addMsg(new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", String.format("Importação realizada com sucesso: %d importados, %d ignorados.", importedValues, ignoredValues)), false);
      } catch (GuiaPersistException g) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_ERROR, String.format("Erro (linha %s)", line.toString()), g.getMessage()), false);

      } catch (Exception e) {
        addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação: linha %s.", line)), false);
      }
    } catch (Exception e) {
      addMsg(new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", String.format("Erro na importação:\n%s", e.getMessage())), false);
    }

  }

  public void addProFatToList() {
    if (proFatToAdd != null) {
      if (!getItem().getProcedimentos().contains(proFatToAdd)) {
        getItem().getProcedimentos().add(proFatToAdd);
      }
      proFatToAdd = null;
    }
  }

  public void removeProFat(ProFat proFatToRemove) {
    getItem().getProcedimentos().remove(proFatToRemove);
  }

  public void receber(Guia guia) {
    Guia attachedGuia = service.carregarListas(guia);
    attachedGuia.setDataRecebimento(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.RECEBIMENTO);
    setItem(attachedGuia);
    doSave();
    guia.setDataRecebimento(getItem().getDataRecebimento());
    guia.setHistorico(getItem().getHistorico());
  }

  public void auditar(Guia guia) {
    Guia attachedGuia = service.carregarListas(guia);
    attachedGuia.setDataAuditoria(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.AUDITORIA);
    setItem(attachedGuia);
    doSave();
    guia.setDataAuditoria(getItem().getDataAuditoria());
    guia.setHistorico(getItem().getHistorico());
  }
  public void solicitarConvenio(Guia guia) {
    Guia attachedGuia = service.carregarListas(guia);
    attachedGuia.setDataSolicitacaoConvenio(new Date());
    service.addHistorico(session.getUser(), attachedGuia, AcoesGuiaEnum.SOLICITACAO);
    setItem(attachedGuia);
    doSave();
    guia.setDataSolicitacaoConvenio(getItem().getDataSolicitacaoConvenio());
    guia.setDataAuditoria(getItem().getDataRecebimento());
    guia.setHistorico(getItem().getHistorico());
  }

  public void negar() {
    getItem().setDataRespostaConvenio(new Date());
    getItem().setEstado(EstadosGuiaEnum.NEGADO);
    service.addHistorico(session.getUser(), getItem(), AcoesGuiaEnum.NEGADO);
    doSave();
  }

  public void salvarNovoComentario() {
    service.addComentario(getItem(), session.getUser(), comentario);
    service.addHistorico(session.getUser(), getItem(), AcoesGuiaEnum.COMENTARIO);
    comentario = null;
    doSave();
  }

  private void saveAddingComment(EstadosGuiaEnum estado, AcoesGuiaEnum acao) {
    getItem().setEstado(estado);
    if (estado.equals(EstadosGuiaEnum.PARCIAL)) {
      getItem().setDataRespostaConvenio(new Date());
    }
    service.addComentario(getItem(), session.getUser(), comentario);
    service.addHistorico(session.getUser(), getItem(), acao);
    comentario = null;
    doSave();
  }

  public void emRevisao() {
    saveAddingComment(EstadosGuiaEnum.REVISAO, AcoesGuiaEnum.REVISAO);
  }

  public void autorizarParcialmente() {
    saveAddingComment(EstadosGuiaEnum.PARCIAL, AcoesGuiaEnum.PARCIAL);
  }

  public void autorizar() {
    getItem().setDataRespostaConvenio(new Date());
    getItem().setEstado(EstadosGuiaEnum.AUTORIZADO);
    service.addHistorico(session.getUser(), getItem(), AcoesGuiaEnum.AUTORIZADO);
    doSave();
  }

  public void clearSetor() {
    setor = null;
    prepareSearch();
  }

  @Override
  public void prepareSearch() {
    resetSearchParams();
    filtros = new ArrayList<>();
    mapFiltros = new HashMap<>();
    String regex = "[0-9]+";
    Boolean searchByAtendimento = false;
    if (getSearchTerm() != null && getSearchTerm().matches(regex)) {
      setSearchParam("atendimento", Long.valueOf(getSearchTerm()));
      searchByAtendimento = true;
      String atendimentoKey = "<b>Atendimento:</b> " + getSearchTerm();
      filtros.add(atendimentoKey);
      mapFiltros.put(atendimentoKey, GuiaFieldsEnum.ATENDIMENTO);
    } else {
      setSearchParam("paciente", getSearchTerm());
      if (getSearchTerm() != null) {
        String pacienteKey = "<b>Paciente:</b> " + getSearchTerm();
        filtros.add(pacienteKey);
        mapFiltros.put(pacienteKey, GuiaFieldsEnum.PACIENTE);
      }
    }
    setSearchParam("semRecebimentos", somenteSemRecebimentos);
    if (!searchByAtendimento) {
      if (session.getSetor() != null) {
        setSearchParam("setor", session.getSetor());
      } else {
        if (setor != null) {
          setSearchParam("setor", setor);
          String setorKey = "<b>Setor:</b> " + setor.getTitle();
          filtros.add(setorKey);
          mapFiltros.put(setorKey, GuiaFieldsEnum.SETOR);
        }
      }
      if (selectedEstados != null && selectedEstados.length > 0) {
        setSearchParam("estados", selectedEstados);
        String[] estadosStr = new String[selectedEstados.length];
        for (int i = 0, len = selectedEstados.length; i < len; i++) {
          estadosStr[i] = selectedEstados[i].getLabel();
        }
        String estadosKey = "<b>Estados:</b> " + StringUtils.join(estadosStr, ", ");
        filtros.add(estadosKey);
        mapFiltros.put(estadosKey, GuiaFieldsEnum.ESTADOS);
      }
      if (selectedTipos != null && selectedTipos.length > 0) {
        setSearchParam("tipos", selectedTipos);
        String[] tiposStr = new String[selectedTipos.length];
        for (int i = 0, len = selectedTipos.length; i < len; i++) {
          tiposStr[i] = selectedTipos[i].getLabel();
        }
        String tiposKey = "<b>Tipos:</b> " + StringUtils.join(tiposStr, ", ");
        filtros.add(tiposKey);
        mapFiltros.put(tiposKey, GuiaFieldsEnum.TIPOS);
      }
      if (somenteMinhaAutoria) {
        setSearchParam("autor", session.getUser());
      }
      if (selectedConvenios != null && selectedConvenios.length > 0) {
        setSearchParam("convenios", selectedConvenios);
        String[] conveniosStr = new String[selectedConvenios.length];
        for (int i = 0, len = selectedConvenios.length; i < len; i++) {
          conveniosStr[i] = selectedConvenios[i].getTitle();
        }
        String conveniosKey = "<b>Convênios:</b> " + StringUtils.join(conveniosStr, ", ");
        filtros.add(conveniosKey);
        mapFiltros.put(conveniosKey, GuiaFieldsEnum.CONVENIOS);
      } else if (!session.getConvenios().isEmpty()) {
        setSearchParam("convenios", session.getConvenios());
      }

      if (inicioDate != null || terminoDate != null) {
        setSearchParam("dateRange", new Date[]{inicioDate, terminoDate});
        setSearchParam("dateField", dateField);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateKey = String.format("<b>Data (%s):</b> %s à %s", dateField.equals("R") ? "Recebimento" : dateField.equals("A") ? "Auditoria" : dateField.equals("S") ? "Solicitação" : dateField.equals("F") ? "Resposta" : "Guia", sdf.format(inicioDate), sdf.format(terminoDate));
        filtros.add(dateKey);
        mapFiltros.put(dateKey, GuiaFieldsEnum.DATA);
      }
    }
  }

  public void setSomenteSemRecebimentos(Boolean somenteSemRecebimentos) {
    this.somenteSemRecebimentos = somenteSemRecebimentos;
  }

  public void removeFilterItem(String key) {
    GuiaFieldsEnum field = mapFiltros.get(key);
    switch (field) {
      case ATENDIMENTO:
      case PACIENTE:
        setSearchTerm(null);
        break;
      case SETOR:
        setor = null;
        break;
      case SETOR_SESSAO:
        break;
      case ESTADOS:
        selectedEstados = null;
        break;
      case TIPOS:
        selectedTipos = null;
        break;
      case CONVENIOS:
        selectedConvenios = null;
        break;
      case DATA:
        inicioDate = terminoDate = null;
        break;
      default:
        break;
    }
    prepareSearch();
  }

  public String getTipoInfo() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<b>Internação</b> - Guias de Internação<br/>");
    buffer.append("<b>Prorrogação</b> - Guias de Prorrogação<br/>");
    buffer.append("<b>Procedimento</b> - Guias de Procedimento (necessário informar o procedimento realizado)<br/>");
    buffer.append("<b>Mat./Med. de Alto Custo</b> - Guias de materiais ou medicamentos de alto custo<br/>");
    buffer.append("<b>Home Care</b> - Guias de serviço de <i>home care</i><br/>");
    buffer.append("<b>Parecer</b> - Guias de parecer (necessário informar especialidade)<br/>");
    buffer.append("<b>Bibap</b> - Guias BIBAP<br/>");
    buffer.append("<b>Outros</b> - Outras situações (radioterapia, quimioterapia, fono, acompanhamento nutricional, etc...)<br/>");
    return buffer.toString();
  }

  public StreamedContent getConsultaCsv() {
    prepareSearch();
    return service.csvConsulta(getLazyModel().getSearchParams());
  }

  public Boolean disableAcoes(Guia guia) {
    if (session.getSetor() != null) {
      return !session.getSetor().equals(guia.getSetor());
    }
    return false;
  }

  public String getModal() {
    return modal;
  }

  public List<String> getFiltros() {
    return filtros;
  }

  public void carregarAnexos(Guia guia) {
    this.modal = "anexosDlg";
    setItem(service.carregarAnexos(guia));
  }

  public void carregarHistorico(Guia guia) {
    this.modal = "historicoDlg";
    setItem(service.carregarHistorico(guia));
  }

  public void carregarComentario(Guia guia) {
    this.modal = "commentsDlg";
    setItem(service.carregarComentarios(guia));
  }

  public void preNewComment(Guia guia) {
    this.modal = "newCommentDlg";
    setItem(service.carregarHistoricoEComentarios(guia));
  }

  public Boolean getSomenteSemRecebimentos() {
    return somenteSemRecebimentos;
  }
}
