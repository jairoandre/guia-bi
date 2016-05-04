package br.com.vah.guiabi.service;


import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.Comentario;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.HistoricoGuia;
import br.com.vah.guiabi.entities.usrdbvah.User;
import br.com.vah.guiabi.exceptions.GuiaPersistException;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;

import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class GuiaService extends DataAccessService<Guia> {

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

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Guia.class);
    Long atendimentoId = (Long) params.getParams().get("atendimento");
    String pacienteParam = (String) params.getParams().get("paciente");
    Setor setor = (Setor) params.getParams().get("setor");
    EstadosGuiaEnum[] estados = (EstadosGuiaEnum[]) params.getParams().get("estados");
    TipoGuiaEnum[] tipos = (TipoGuiaEnum[]) params.getParams().get("tipos");
    User autor = (User) params.getParams().get("autor");
    Convenio[] convenios = (Convenio[]) params.getParams().get("convenios");
    Date[] dateRange = (Date[]) params.getParams().get("dateRange");
    String dateField = (String) params.getParams().get("dateField");
    Criteria atendimentoAlias = criteria.createAlias("atendimento", "a");
    if (atendimentoId != null) {
      atendimentoAlias.add(Restrictions.eq("a.id", atendimentoId));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      atendimentoAlias.createAlias("a.paciente", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
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
      DetachedCriteria histCriteria = DetachedCriteria.forClass(HistoricoGuia.class, "h").add(Restrictions.eq("h.autor", autor)).add(Restrictions.eq("h.acao", AcoesGuiaEnum.CRIACAO)).setProjection(Projections.property("h.guia"));
      criteria.add(Subqueries.propertyIn("id", histCriteria));
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

  public void saveAll(List<Guia> guias) throws GuiaPersistException {
    for (Guia guia : guias) {
      try {
        create(guia);
      } catch (Exception e) {
        throw new GuiaPersistException(String.format("Erro ao criar guia para o atendimento %s", guia.getAtendimento().getId().toString()));
      }

    }

  }

}
