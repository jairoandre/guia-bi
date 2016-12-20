package br.com.vah.guiabi.service;

import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.GuiaSispag;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;

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
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(GuiaSispag.class);
    criteria.add(Restrictions.isNotNull("statusOpme"));
    criteria.setFetchMode("convenio", FetchMode.SELECT);
    criteria.setFetchMode("prestador", FetchMode.SELECT);
    return criteria;
  }

}
