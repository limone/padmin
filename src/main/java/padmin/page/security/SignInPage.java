package padmin.page.security;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import padmin.BasePage;
import padmin.model.SignIn;
import padmin.security.BypassAuthentication;
import padmin.security.SecurityProvider;
import padmin.wicket.FormValidator;

@BypassAuthentication
@MountPath("/signin.html")
public class SignInPage extends BasePage {
  protected static final Logger log = LoggerFactory.getLogger(SignInPage.class);
  
  @SpringBean
  protected SecurityProvider secProv;
  
  public SignInPage() {
    final SignIn model = new SignIn();
    setDefaultModel(new CompoundPropertyModel<>(model));
    
    final Form<SignIn> signInForm = new Form<>("signInForm");
    add(signInForm);
    
    signInForm.add(new RequiredTextField<>("username"));
    signInForm.add(new PasswordTextField("password"));
    signInForm.add(new AjaxButton("signInButton", signInForm) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        log.debug("Attempting to sign in user.");
      }
      
      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
        log.error("Error with sign in form.");
        final FormValidator validator = new FormValidator(target);
        form.visitFormComponents(validator);
        validator.renderMessages();
      }
    });
  }
}