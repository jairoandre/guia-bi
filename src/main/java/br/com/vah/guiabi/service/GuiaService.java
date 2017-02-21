package br.com.vah.guiabi.service;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.GuiaMv;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.Comentario;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.HistoricoGuia;
import br.com.vah.guiabi.entities.usrdbvah.User;
import br.com.vah.guiabi.exceptions.GuiaPersistException;
import br.com.vah.guiabi.reports.ReportLoader;
import br.com.vah.guiabi.reports.ReportTotalPorSetor;
import br.com.vah.guiabi.util.PaginatedSearchParam;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class GuiaService extends DataAccessService<Guia> {

  private
  @Inject
  ReportLoader reportLoader;

  public GuiaService() {
    super(Guia.class);
  }

  public void addHistorico(User author, Guia guia, AcoesGuiaEnum acao) {
    HistoricoGuia historicoGuia = new HistoricoGuia(author, guia, acao);
    guia.getHistorico().add(historicoGuia);
  }

  public void addComentario(Guia guia, User author, String comentario) {
    Comentario newComment = new Comentario(guia, author, comentario, new Date());
    guia.getComentarios().add(newComment);
  }

  private long diasDiff(Date d1, Date d2) {
    return (d1.getTime() - d2.getTime()) / 1000 / 60 / 60 / 24;
  }

  public StreamedContent relatorioTotalPorSetor(Date inicio, Date termino, List<Setor> setores, String[] relations) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Guia.class);

    Calendar beginCl = Calendar.getInstance();
    beginCl.setTime(inicio);
    beginCl.set(Calendar.HOUR_OF_DAY, 0);
    beginCl.set(Calendar.MINUTE, 0);
    beginCl.set(Calendar.SECOND, 0);

    Calendar endCl = Calendar.getInstance();
    endCl.setTime(termino);
    endCl.set(Calendar.HOUR_OF_DAY, 23);
    endCl.set(Calendar.MINUTE, 59);
    endCl.set(Calendar.SECOND, 59);

    criteria.add(Restrictions.between("data", beginCl.getTime(), endCl.getTime()));

    if (setores != null && !setores.isEmpty()) {
      criteria.add(Restrictions.in("setor", setores));
    }

    for (String relation : relations) {
      criteria.setFetchMode(relation, FetchMode.SELECT);
    }

    List<Guia> guias = criteria.list();

    Map<String, ReportTotalPorSetor> map = new HashMap<>();

    float somaReceb = 0, somaAudit = 0, somaSolic = 0, somaResp = 0;
    int totalReceb = 0, totalAudit = 0, totalSolic = 0, totalResp = 0;

    for (Guia guia : guias) {
      String setor = guia.getSetor().getTitle();
      Atendimento atendimento = guia.getAtendimento();
      String strConv = "SEM CONVÊNIO";
      if (atendimento != null) {
        Convenio convenio = atendimento.getConvenio();
        if (convenio != null) {
          strConv = convenio.getTitle();
        }
      }
      String key = setor + strConv;
      ReportTotalPorSetor report = map.get(key);
      if (report == null) {
        report = new ReportTotalPorSetor(setor, strConv);
        map.put(key, report);
      }

      report.setTotalCriadas(report.getTotalCriadas() + 1);

      switch (guia.getEstado()) {
        case PENDENTE:
          report.setTotalPendentes(report.getTotalPendentes() + 1);
          break;
        case NEGADO:
        case AUTORIZADO:
          report.setTotalFechadas(report.getTotalFechadas() + 1);
          break;
      }

      Date dataGuia = guia.getData();
      Date dataReceb = guia.getDataRecebimento();
      Date dataAudit = guia.getDataAuditoria();
      Date dataSolic = guia.getDataSolicitacaoConvenio();
      Date dataRespo = guia.getDataRespostaConvenio();

      if (dataReceb != null && dataReceb.after(dataGuia)) {
        somaReceb += diasDiff(dataReceb, dataGuia);
        report.setTempoMedioRecebimento(somaReceb / ++totalReceb);

      }

      if (dataAudit != null && dataAudit.after(dataGuia)) {
        somaAudit += diasDiff(dataAudit, dataGuia);
        report.setTempoMedioAuditoria(somaAudit / ++totalAudit);
      }

      if (dataSolic != null && dataSolic.after(dataGuia)) {
        somaSolic += diasDiff(dataSolic, dataGuia);
        report.setTempoMedioSolicitacao(somaSolic / ++totalSolic);
      }

      if (dataRespo != null && dataRespo.after(dataGuia)) {
        somaResp += diasDiff(dataRespo, dataGuia);
        report.setTempoMedioResposta(somaResp / ++totalResp);
      }
    }

    List<ReportTotalPorSetor> list = new ArrayList<>(map.values());
    Collections.sort(list, new Comparator<ReportTotalPorSetor>() {
      @Override
      public int compare(ReportTotalPorSetor o1, ReportTotalPorSetor o2) {
        int compareSetor = o1.getSetor().compareTo(o2.getSetor());
        if (compareSetor == 0) {
          return o2.getTotalCriadas().compareTo(o1.getTotalCriadas());
        }
        return compareSetor;
      }
    });

    return reportLoader.imprimeRelatorio("medias", list, inicio, termino);

  }

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Guia.class);
    Long atendimentoId = (Long) params.getParams().get("atendimento");
    String pacienteParam = (String) params.getParams().get("paciente");
    Setor setor = (Setor) params.getParams().get("setor");
    EstadosGuiaEnum[] estados = (EstadosGuiaEnum[]) params.getParams().get("estados");
    TipoGuiaEnum[] tipos = (TipoGuiaEnum[]) params.getParams().get("tipos");
    User autor = (User) params.getParams().get("autor");
    List<Convenio> convenios = (List<Convenio>) params.getParams().get("convenios");
    Date[] dateRange = (Date[]) params.getParams().get("dateRange");
    String dateField = (String) params.getParams().get("dateField");
    Boolean semRecebimentos = (Boolean) params.getParams().get("semRecebimentos");

    Criteria atendimentoAlias = criteria.createAlias("atendimento", "a");
    if (atendimentoId != null) {
      atendimentoAlias.add(Restrictions.eq("a.id", atendimentoId));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      atendimentoAlias.createAlias("a.paciente", "p")
          .add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
    }
    if (convenios != null) {
      atendimentoAlias.add(Restrictions.in("a.convenio", convenios));
    }
    if (setor != null) {
      criteria.add(Restrictions.eq("setor", setor));
    }
    if (estados != null) {
      criteria.add(Restrictions.in("estado", estados));
    }
    if (tipos != null) {
      criteria.add(Restrictions.in("tipo", tipos));
    }
    if (autor != null) {
      DetachedCriteria histCriteria = DetachedCriteria.forClass(HistoricoGuia.class, "h")
          .add(Restrictions.eq("h.autor", autor)).add(Restrictions.eq("h.acao", AcoesGuiaEnum.CRIACAO))
          .setProjection(Projections.property("h.guia"));
      criteria.add(Subqueries.propertyIn("id", histCriteria));
    }
    if (semRecebimentos) {
      criteria.add(Restrictions.isNull("dataRecebimento"));
    }
    if (dateRange != null) {
      String property = "data";
      switch (dateField) {
        case "R":
          property = "dataRecebimento";
          break;
        case "A":
          property = "dataAuditoria";
          break;
        case "S":
          property = "dataSolicitacaoConvenio";
          break;
        case "F":
          property = "dataRespostaConvenio";
          break;
        default:
          property = "data";
      }
      if (dateRange[0] != null) {
        if (dateRange[1] != null) {
          criteria.add(Restrictions.between(property, dateRange[0], dateRange[1]));
        } else {
          criteria.add(Restrictions.ge(property, dateRange[0]));
        }
      } else {
        criteria.add(Restrictions.le(property, dateRange[1]));
      }
    }
    return criteria;
  }

  public StreamedContent csvConsulta(PaginatedSearchParam params) {

    Criteria criteria = createCriteria(params);

    for (String relation : params.getRelations()) {
      criteria.setFetchMode(relation, FetchMode.SELECT);
    }

    List<Guia> guias = criteria.list();

    StringBuilder builder = new StringBuilder();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:MM:ss");
    builder.append(
        "SETOR,ATENDIMENTO,PACIENTE,CONVENIO,TIPO,DESCRIÇÃO,DATA,DATA RECEBIMENTO,DATA AUDITORIA,DATA SOLICITAÇÃO,DATA RESPOSTA\r\n");

    for (Guia guia : guias) {
      builder.append(guia.getSetor().getTitle());
      builder.append(",");
      builder.append(guia.getAtendimento().getId());
      builder.append(",");
      builder.append(guia.getAtendimento().getPaciente().getName());
      builder.append(",");
      builder.append(guia.getAtendimento().getConvenio().getTitle());
      builder.append(",");
      builder.append(guia.getTipo().getLabel());
      builder.append(",");
      builder.append(guia.getDescricao());
      builder.append(",");
      builder.append(guia.getData() == null ? "" : sdf.format(guia.getData()));
      builder.append(",");
      builder.append(guia.getDataRecebimento() == null ? "" : sdf.format(guia.getDataRecebimento()));
      builder.append(",");
      builder.append(guia.getDataAuditoria() == null ? "" : sdf.format(guia.getDataAuditoria()));
      builder.append(",");
      builder.append(
          guia.getDataSolicitacaoConvenio() == null ? "" : sdf.format(guia.getDataSolicitacaoConvenio()));
      builder.append(",");
      builder.append(guia.getDataRespostaConvenio() == null ? "" : sdf.format(guia.getDataRespostaConvenio()));
      builder.append("\r\n");
    }

    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(builder.toString().getBytes());
      DefaultStreamedContent dsc = new DefaultStreamedContent(bais);

      dsc.setContentType("text/plain");
      dsc.setContentEncoding("UTF-8");

      SimpleDateFormat sdfFile = new SimpleDateFormat("ddMMyy");
      dsc.setName(String.format("%s%s.csv", "CSV", sdfFile.format(new Date())));

      return dsc;
    } catch (Exception e) {
      return null;
    }

  }

  public void saveAll(List<Guia> guias) throws GuiaPersistException {
    for (Guia guia : guias) {
      try {
        create(guia);

        // -----
        criarGuiaMv(guia);
        // ----

      } catch (Exception e) {
        throw new GuiaPersistException(String.format("Erro ao criar guia para o atendimento %s",
            guia.getAtendimento().getId().toString()));
      }

    }

  }

  public Guia carregarListas(Guia guia) {
    Guia att = find(guia.getId());
    new LinkedHashSet<>(att.getAnexos());
    new LinkedHashSet<>(att.getHistorico());
    new LinkedHashSet<>(att.getComentarios());

    return att;
  }

  public Guia carregarAnexos(Guia guia) {
    Guia att = find(guia.getId());
    new LinkedHashSet<>(att.getAnexos());
    return att;
  }

  public Guia carregarHistorico(Guia guia) {
    Guia att = find(guia.getId());
    new LinkedHashSet<>(att.getHistorico());
    return att;
  }

  public Guia carregarComentarios(Guia guia) {
    Guia att = find(guia.getId());
    new LinkedHashSet<>(att.getComentarios());
    return att;
  }

  public Guia carregarHistoricoEComentarios(Guia guia) {
    Guia att = find(guia.getId());
    new LinkedHashSet<>(att.getHistorico());
    new LinkedHashSet<>(att.getComentarios());
    return att;
  }

  // TODO: Criação da guia na tabela do MV
  public GuiaMv criarGuiaMv(Guia guia) {

    GuiaMv guiaMv = new GuiaMv();

    guiaMv.setAtendimento(guia.getAtendimento());

    guiaMv.setCriacao(guia.getData());
    guiaMv.setPaciente(guia.getAtendimento().getPaciente());

    // Tipos de guia seguindo tabela MV
    if (guia.getTipo().getLabel().equalsIgnoreCase("Internação")) {
      guiaMv.setTipoGuia("I");
    }
    if ((guia.getTipo().getLabel().equalsIgnoreCase("Procedimento")) || (guia.getTipo().getLabel().equalsIgnoreCase("Mat./Med. Alto Custo"))
        || (guia.getTipo().getLabel().equalsIgnoreCase("Home Care")) || (guia.getTipo().getLabel().equalsIgnoreCase("Parecer"))
        || (guia.getTipo().getLabel().equalsIgnoreCase("Bipap"))) {
      guiaMv.setTipoGuia("P");
    }
    if (guia.getTipo().getLabel().equalsIgnoreCase("Prorrogação")) {
      guiaMv.setTipoGuia("R");
    }
    if (guia.getTipo().getLabel().equalsIgnoreCase("Consulta")) {
      guiaMv.setTipoGuia("C");
    }
    // ---------------------------------

    // tipo de situacao da guia seguindo tabela MV
    if (guia.getEstado().getLabel().equalsIgnoreCase("Negado")) {
      guiaMv.setTipoSituacao("N");
    }
    if (guia.getEstado().getLabel().equalsIgnoreCase("Autorizado")) {
      guiaMv.setTipoSituacao("A");
    }
    if (guia.getEstado().getLabel().equalsIgnoreCase("Revisao")) {
      guiaMv.setTipoSituacao("G");
    }
    if (guia.getEstado().getLabel().equalsIgnoreCase("Pendente")) {
      guiaMv.setTipoSituacao("P");
    }
    // -------------------------------------------
    guiaMv.setNomeAutorizador("SIS");
    guiaMv.setDiasSolicitados(1); // Default para 1 dia
    guiaMv.setConvenio(guia.getAtendimento().getConvenio().getId().intValue());
    guiaMv.setTipoAcomodacao(guia.getAtendimento().getTipoAcomodacao());

    guiaMv.setEmergencia("N");
    guiaMv.setUrgente("N");

    return guiaMv;

  }
}
