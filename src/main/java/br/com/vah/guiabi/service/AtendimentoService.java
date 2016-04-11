package br.com.vah.guiabi.service;

import br.com.vah.guiabi.entities.dbamv.Atendimento;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
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
    if(idParam != null) {
      criteria.add(Restrictions.eq("id", idParam));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      criteria.createAlias("paciente", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
    }
    return criteria;
  }
}
