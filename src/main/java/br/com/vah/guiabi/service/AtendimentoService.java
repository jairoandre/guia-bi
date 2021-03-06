package br.com.vah.guiabi.service;

import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.entities.dbamv.RegFaturamento;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

import javax.ejb.Stateless;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class AtendimentoService extends DataAccessService<Atendimento> {

  public AtendimentoService() {
    super(Atendimento.class);
  }

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Atendimento.class);
    Long idParam = (Long) params.getParams().get("id");
    String pacienteParam = (String) params.getParams().get("paciente");
    Boolean semAltaRecente = (Boolean) params.getParams().get("semAltaRecente");
    if (idParam != null) {
      criteria.add(Restrictions.eq("id", idParam));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      criteria.createAlias("paciente", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
    }
    if (semAltaRecente != null && semAltaRecente) {
      Disjunction disjunction = Restrictions.disjunction();
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      calendar.add(Calendar.MONTH, -4);
      disjunction.add(Restrictions.isNull("dataAlta"));
      disjunction.add(Restrictions.ge("dataAlta", calendar.getTime()));
      criteria.add(disjunction);

      DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegFaturamento.class, "r");
      Disjunction regFatDisj = Restrictions.disjunction();
      regFatDisj.add(Restrictions.eq("r.fechada", "N"));
      regFatDisj.add(Restrictions.isNull("r.remessa"));
      detachedCriteria.add(regFatDisj);
      detachedCriteria.setProjection(Projections.property("r.atendimento"));

      criteria.add(Subqueries.propertyIn("id", detachedCriteria));

    }
    criteria.add(Restrictions.eq("multiEmpresa", 1));
    return criteria;
  }
}
