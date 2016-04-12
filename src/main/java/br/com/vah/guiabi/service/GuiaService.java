package br.com.vah.guiabi.service;


import br.com.vah.guiabi.constants.AcoesGuiaEnum;
import br.com.vah.guiabi.constants.EstadosGuiaEnum;
import br.com.vah.guiabi.entities.dbamv.Setor;
import br.com.vah.guiabi.entities.usrdbvah.Guia;
import br.com.vah.guiabi.entities.usrdbvah.HistoricoGuia;
import br.com.vah.guiabi.entities.usrdbvah.User;
import br.com.vah.guiabi.util.PaginatedSearchParam;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.ejb.Stateless;

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

  public Criteria createCriteria(PaginatedSearchParam params) {
    Session session = getEm().unwrap(Session.class);
    Criteria criteria = session.createCriteria(Guia.class);
    Long atendimentoId = (Long) params.getParams().get("atendimento");
    String pacienteParam = (String) params.getParams().get("paciente");
    Setor setor = (Setor) params.getParams().get("setor");
    EstadosGuiaEnum estado = (EstadosGuiaEnum) params.getParams().get("estado");
    User autor = (User) params.getParams().get("autor");
    if(atendimentoId != null) {
      criteria.createAlias("atendimento", "a").add(Restrictions.eq("a.id", atendimentoId));
    } else if (pacienteParam != null && !pacienteParam.isEmpty()) {
      criteria.createAlias("atendimento", "a").createAlias("a.paciente", "p").add(Restrictions.ilike("p.name", pacienteParam, MatchMode.ANYWHERE));
    }
    if(setor != null){
      criteria.add(Restrictions.eq("setor", setor));
    }
    if(estado != null){
      criteria.add(Restrictions.eq("estado", estado));
    }
    if(autor != null){
      criteria.createAlias("historico", "h").add(Restrictions.eq("h.autor", autor)).add(Restrictions.eq("h.acao", AcoesGuiaEnum.CRIACAO));
    }
    return criteria;
  }

}
