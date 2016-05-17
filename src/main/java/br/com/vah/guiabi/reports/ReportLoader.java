package br.com.vah.guiabi.reports;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jairoportela on 29/04/2016.
 */
public class ReportLoader {

  public StreamedContent imprimeRelatorio() {

    List<ReportTotalPorSetor> lista = new ArrayList<>();

    lista.add(new ReportTotalPorSetor("Rubem Braga", "BRADESCO"));
    lista.add(new ReportTotalPorSetor("Rubem Braga", "Unimed"));
    lista.add(new ReportTotalPorSetor("Cora Coralina", "Unimed"));

    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lista);

    HashMap parameters = new HashMap();

    InputStream report = null;

    try {

      FacesContext facesContext = FacesContext.getCurrentInstance();

      facesContext.responseComplete();

      ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();

      JasperPrint jasperPrint = JasperFillManager.fillReport(scontext.getRealPath("/resources/reports/medias.jasper"), parameters, ds);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      JRPdfExporter exporter = new JRPdfExporter();

      exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
      exporter.setConfiguration(new SimplePdfExporterConfiguration());

      exporter.exportReport();

      report = new ByteArrayInputStream(baos.toByteArray());

    } catch (Exception e) {

      e.printStackTrace();

    }

    DefaultStreamedContent dsc = new DefaultStreamedContent(report);
    dsc.setContentType("application/pdf");
    dsc.setName("relatorio.pdf");

    return dsc;

  }
}
