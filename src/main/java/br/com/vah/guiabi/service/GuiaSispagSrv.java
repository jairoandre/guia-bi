package br.com.vah.guiabi.service;

import br.com.vah.guiabi.constants.StatusEnum;
import br.com.vah.guiabi.constants.TipoGuiaEnum;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.GuiaSispag;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;
import java.util.Calendar;
import java.util.LinkedHashSet;

/**
 * Created by Jairoportela on 19/12/2016.
 */
@Stateless
public class GuiaSispagSrv extends DataAccessService<GuiaSispag> {

  public GuiaSispagSrv() {
    super(GuiaSispag.class);
  }

  @Override
  public Criteria createCriteria(PaginatedSearchParam params) {

    Calendar cld = Calendar.getInstance();
    cld.add(Calendar.MONTH, -3);

    String paciente = (String) params.getParams().get("paciente");
    String convenio = (String) params.getParams().get("convenio");

    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(GuiaSispag.class);
    criteria.add(Restrictions.isNotNull("statusOpme"));
    criteria.setFetchMode("convenio", FetchMode.SELECT);
    criteria.setFetchMode("prestador", FetchMode.SELECT);
    Disjunction disjunction = Restrictions.disjunction();
    disjunction.add(Restrictions.ge("dataEmissao", cld.getTime()));
    disjunction.add(Restrictions.ne("statusOpme", StatusEnum.AUTORIZADA));
    criteria.add(disjunction);
    if (paciente != null) {
      criteria.add(Restrictions.like("paciente", paciente, MatchMode.ANYWHERE));
    }
    if (convenio != null) {
      criteria.createAlias("convenio", "c").add(Restrictions.like("c.title", convenio, MatchMode.ANYWHERE));
    }
    return criteria;
  }

  public GuiaSispag initializeLists(GuiaSispag guia) {
    GuiaSispag att = find(guia.getId());
    new LinkedHashSet<>(att.getItens());
    new LinkedHashSet<>(att.getOpmes());
    new LinkedHashSet<>(att.getMateriais());
    new LinkedHashSet<>(att.getEventos());
    new LinkedHashSet<>(att.getAnexos());
    return att;
  }

}
