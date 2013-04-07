package padmin.wicket;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ValidationErrorFeedback;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FormValidator implements IVisitor<FormComponent<?>, Void> {
  private static final Logger     log    = LoggerFactory.getLogger(FormValidator.class);

  private final AjaxRequestTarget target;
  private final List<String>      errors = new ArrayList<>();

  public FormValidator(AjaxRequestTarget target) {
    this.target = target;
  }

  @Override
  public void component(final FormComponent<?> formComponent, final IVisit<Void> visit) {
    Form<?> form = formComponent.getForm();
    if (!form.isVisibleInHierarchy() || !form.isEnabledInHierarchy()) {
      visit.dontGoDeeper();
      return;
    }

    if (formComponent.isVisibleInHierarchy() && formComponent.isEnabledInHierarchy()) {
      validate(formComponent);
    }

    if (formComponent.processChildren() == false) {
      visit.dontGoDeeper();
    }
  }

  private void validate(FormComponent<?> formComponent) {
    log.debug("Validating {}.", formComponent.getMarkupId());

    if (!formComponent.isValid() && formComponent.isEnabled() && formComponent.isRequired()) {
      log.debug("{} is not valid - getting error messages.", formComponent.getMarkupId());

      for (FeedbackMessage message : formComponent.getFeedbackMessages()) {
        if (message != null) {
          message.markRendered();

          ValidationErrorFeedback feedback = (ValidationErrorFeedback) message.getMessage();
          errors.add(StringEscapeUtils.escapeEcmaScript((String) feedback.getMessage()));
        }
      }
    }
  }
  
  public void renderMessages() {
    String errorMessage = "";
    for (String error : errors) {
      errorMessage += "'" + error + "',";
    }
    errorMessage = errorMessage.substring(0, errorMessage.lastIndexOf(","));
    target.appendJavaScript("padmin.displayError([" + errorMessage + "]);");
  }
}