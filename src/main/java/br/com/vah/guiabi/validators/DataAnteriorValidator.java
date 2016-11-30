package br.com.vah.guiabi.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jairoportela on 30/11/2016.
 */
@FacesValidator("dataAnteriorValidator")
public class DataAnteriorValidator implements Validator {
  @Override
  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    if (value == null) {
      FacesMessage msg = new FacesMessage(" Data obrigatória.", "Por favor, a data anterior de resposta do convênio.");
      msg.setSeverity(FacesMessage.SEVERITY_ERROR);
      throw new ValidatorException(msg);
    } else {
      Calendar informed = Calendar.getInstance();
      informed.setTime((Date) value);


      Calendar current = Calendar.getInstance();
      current.setTime(new Date());
      current.set(Calendar.HOUR_OF_DAY, 0);
      current.set(Calendar.MINUTE, 0);
      current.set(Calendar.SECOND, 0);
      current.set(Calendar.MILLISECOND, 0);

      if (informed.compareTo(current) >= 0) {
        FacesMessage msg = new FacesMessage(" Data inválida.", "Por favor, informe uma data anterior menor que a data corrente.");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(msg);
      }
    }

  }
}
