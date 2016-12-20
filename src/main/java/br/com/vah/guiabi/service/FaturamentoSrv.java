package br.com.vah.guiabi.service;

import br.com.vah.guiabi.reports.Faturamento;
import br.com.vah.guiabi.reports.ReportLoader;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Jairoportela on 16/12/2016.
 */
@Stateless
public class FaturamentoSrv {

  private
  @PersistenceContext
  EntityManager em;

  private
  @Inject
  ReportLoader reportLoader;

  public Session getSession() {
    return em.unwrap(Session.class);
  }

  public List<Faturamento> getFaturamento() {
    String sql =
        "SELECT " +
            "  GRUPO, " +
            "  CONV, " +
            "  SUM(QTD)   AS QTD, " +
            "  CAST(AVG(MEDIA) AS INT) AS MEDIA " +
            "FROM (SELECT " +
            "        C.NM_CONVENIO AS CONV, " +
            "        CASE " +
            "        WHEN (G.DT_RESPOSTA_CONVENIO IS NULL) " +
            "          THEN 1 " +
            "        WHEN (G.DT_RESPOSTA_CONVENIO IS NOT NULL) " +
            "          THEN 0 " +
            "        END           AS QTD, " +
            "        CASE " +
            "        WHEN (G.DT_RESPOSTA_CONVENIO IS NULL) " +
            "          THEN 0 " +
            "        WHEN (G.DT_RESPOSTA_CONVENIO IS NOT NULL) " +
            "          THEN CAST((G.DT_RESPOSTA_CONVENIO - G.DT_GUIA) AS INT) " +
            "        END           AS MEDIA, " +
            "        CASE " +
            "        WHEN (S.NM_SETOR = 'PRONTO SOCORRO') " +
            "          THEN 'PRONTO SOCORRO' " +
            "        WHEN (S.NM_SETOR <> 'PRONTO SOCORRO') " +
            "          THEN 'INTERNAÇÃO' " +
            "        END           AS GRUPO " +
            "      FROM USRDBVAH.TB_GUIABI_GUIA G " +
            "        LEFT JOIN DBAMV.TB_ATENDIME ATD ON ATD.CD_ATENDIMENTO = G.CD_ATENDIMENTO " +
            "        LEFT JOIN DBAMV.CONVENIO C ON C.CD_CONVENIO = ATD.CD_CONVENIO " +
            "        LEFT JOIN DBAMV.SETOR S ON S.CD_SETOR = G.CD_SETOR " +
            "      WHERE DT_GUIA > to_date('01-08-2016', 'DD-MM-YYYY')) " +
            "GROUP BY CONV, GRUPO " +
            "ORDER BY GRUPO, QTD DESC";

    Session session = getSession();
    SQLQuery query = session.createSQLQuery(sql);

    List<Object[]> result = (List<Object[]>) query.list();

    List<Faturamento> list = new ArrayList<>();

    Map<String, Faturamento> map = new HashMap<>();

    for (Object[] obj : result) {
      String grupo = (String) obj[0];
      String convenio = (String) obj[1];

      String grupoItemKey = grupo + convenio;

      Integer pendentes = ((BigDecimal) obj[2]).intValue();
      Integer daysAvg = ((BigDecimal) obj[3]).intValue();
      Faturamento fat = new Faturamento();
      fat.setGrupo(grupo);
      fat.setConvenio(convenio);
      fat.setGuiasQtd(pendentes);
      fat.setGuiasDias(daysAvg);
      map.put(grupoItemKey, fat);
      list.add(fat);
    }

    String sqlRemessas = "SELECT " +
        "  GRUPO, " +
        "  NM_CONVENIO CONV, " +
        "  STATUS, " +
        "  COUNT(*)                      QTD,   " +
        "  TRUNC(AVG(QTDE_DIAS))         MEDIA_DIAS,   " +
        "  ROUND(SUM(VL_TOTAL_CONTA), 2) MEDIA_VALOR " +
        "FROM (   " +
        "  SELECT " +
        "    'INTERNAÇÃO' GRUPO, " +
        "    C.NM_CONVENIO, " +
        "    TRUNC(SYSDATE - A.DT_FINAL) QTDE_DIAS, " +
        "    (CASE " +
        "     WHEN (TRUNC(SYSDATE - A.DT_FINAL) < 30) " +
        "       THEN 'LT30' " +
        "     WHEN (TRUNC(SYSDATE - A.DT_FINAL) >= 30) " +
        "       THEN 'GE30' " +
        "     END) STATUS, " +
        "    A.VL_TOTAL_CONTA   " +
        "  FROM DBAMV.REG_FAT A   " +
        "    LEFT JOIN DBAMV.CONVENIO C " +
        "      ON A.CD_CONVENIO = C.CD_CONVENIO   " +
        "    LEFT JOIN DBAMV.TB_ATENDIME D " +
        "      ON A.CD_ATENDIMENTO = D.CD_ATENDIMENTO " +
        "    LEFT JOIN DBAMV.LEITO L " +
        "      ON L.CD_LEITO = D.CD_LEITO " +
        "  WHERE A.DT_FINAL IS NOT NULL   " +
        "        AND A.CD_REMESSA IS NULL   " +
        "        AND A.CD_MULTI_EMPRESA = 1   " +
        "        AND A.CD_CONVENIO <> 1   " +
        "        AND D.TP_ATENDIMENTO = 'I'   " +
        "        AND A.VL_TOTAL_CONTA > 0   " +
        "        AND D.DT_ATENDIMENTO >= TO_DATE('01-01-2016', 'DD-MM-YYYY')   " +
        "  UNION   " +
        "  SELECT " +
        "    (CASE " +
        "     WHEN (TP_ATENDIMENTO = 'U') " +
        "       THEN 'PRONTO SOCORRO' " +
        "     WHEN (TP_ATENDIMENTO = 'E') " +
        "       THEN 'EXTERNO' " +
        "     END) GRUPO, " +
        "    C.NM_CONVENIO, " +
        "    (CASE " +
        "      WHEN (TP_ATENDIMENTO = 'U') " +
        "        THEN " +
        "          TRUNC(SYSDATE - D.DT_ALTA) " +
        "      WHEN (TP_ATENDIMENTO = 'E') " +
        "        THEN " +
        "          TRUNC(SYSDATE - D.DT_ATENDIMENTO) " +
        "    END) QTDE_DIAS, " +
        "    (CASE " +
        "     WHEN (TP_ATENDIMENTO = 'E' AND TRUNC(SYSDATE - D.DT_ATENDIMENTO) < 30) " +
        "       THEN 'LT30' " +
        "     WHEN (TP_ATENDIMENTO = 'U' AND TRUNC(SYSDATE - D.DT_ALTA) < 30) " +
        "       THEN 'LT30' " +
        "     WHEN (TP_ATENDIMENTO = 'E' AND TRUNC(SYSDATE - D.DT_ATENDIMENTO) >= 30) " +
        "       THEN 'GE30' " +
        "     WHEN (TP_ATENDIMENTO = 'U' AND TRUNC(SYSDATE - D.DT_ALTA) >= 30) " +
        "       THEN 'GE30' " +
        "     END) STATUS, " +
        "    T.VL_TOTAL_CONTA   " +
        "  FROM DBAMV.REG_AMB T   " +
        "    LEFT JOIN DBAMV.ITREG_AMB B " +
        "      ON T.CD_REG_AMB = B.CD_REG_AMB   " +
        "    LEFT JOIN DBAMV.CONVENIO C " +
        "      ON T.CD_CONVENIO = C.CD_CONVENIO   " +
        "    LEFT JOIN DBAMV.TB_ATENDIME D " +
        "      ON B.CD_ATENDIMENTO = D.CD_ATENDIMENTO " +
        "  WHERE T.CD_REMESSA IS NULL   " +
        "        AND T.CD_MULTI_EMPRESA = 1   " +
        "        AND T.CD_CONVENIO <> 1   " +
        "        AND ((D.DT_ALTA IS NULL AND D.TP_ATENDIMENTO = 'E') OR (D.DT_ALTA IS NOT NULL AND D.TP_ATENDIMENTO = 'U')) " +
        "        AND T.VL_TOTAL_CONTA > 0   " +
        "        AND D.DT_ATENDIMENTO >= TO_DATE('01-01-2016', 'DD-MM-YYYY'))   " +
        "GROUP BY   " +
        "  GRUPO, " +
        "  NM_CONVENIO, " +
        "  STATUS " +
        "ORDER BY " +
        "  GRUPO, CONV";

    query = session.createSQLQuery(sqlRemessas);
    result = (List<Object[]>) query.list();

    NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("pt", "BR"));

    for (Object[] obj : result) {
      String grupo = (String) obj[0];
      String convenio = (String) obj[1];

      String grupoItemKey = grupo + convenio;

      String status = (String) obj[2];

      Integer qtd = ((BigDecimal) obj[3]).intValue();
      Integer mediaDias = ((BigDecimal) obj[4]).intValue();
      BigDecimal mediaValor = (BigDecimal) obj[5];

      Faturamento fat = map.get(grupoItemKey);
      if (fat == null) {
        fat = new Faturamento();
        fat.setGrupo(grupo);
        fat.setConvenio(convenio);
        map.put(grupoItemKey, fat);
        list.add(fat);
      }
      if (status.equals("LT30")) {
        fat.setLt30Qtd(qtd);
        fat.setLt30Dias(mediaDias);
        fat.setLt30Total(mediaValor);
      } else {
        fat.setGe30Qtd(qtd);
        fat.setGe30Dias(mediaDias);
        fat.setGe30Total(mediaValor);
      }
    }

    list.sort((Faturamento f1, Faturamento f2) ->
        f1.getGrupo().equals(f2.getGrupo()) ?
            f2.getGe30Qtd().compareTo(f1.getGe30Qtd()) :
            f1.getGrupo().compareTo(f2.getGrupo()));

    return list;
  }

  public StreamedContent getReport() {
    return reportLoader.imprimeRelatorio("balanco", getFaturamento());
  }

}
