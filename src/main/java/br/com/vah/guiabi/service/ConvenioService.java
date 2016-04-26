package br.com.vah.guiabi.service;

import br.com.vah.guiabi.entities.dbamv.Convenio;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;

/**
 * Created by Jairoportela on 06/04/2016.
 */
@Stateless
public class ConvenioService extends DataAccessService<Convenio> {

  public ConvenioService() {
    super(Convenio.class);
  }

}
