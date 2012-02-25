package padmin.page.security;

import org.apache.commons.lang3.tuple.Pair;
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
import padmin.app.PadminSession;
import padmin.app.WicketApplication;
import padmin.model.SignIn;
import padmin.model.User;
import padmin.security.BypassAuthentication;
import padmin.security.LoginStatusType;
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
        final String username = model.getUsername();
        log.debug("Attempting to sign in user {}.", username);
        Pair<LoginStatusType,User> loginResult = secProv.login(username, model.getPassword());
        switch (loginResult.getLeft()) {
          case BAD_CREDENTIALS:
            log.warn("Password for {} did not match.", username);
            target.appendJavaScript("padmin.displayError(['Password for " + username + " did not match.']);");
            break;
          case DISABLED:
            log.warn("Account for {} is disabled.", username);
            target.appendJavaScript("padmin.displayError(['Account for " + username + " is currently disabled.']);");
            break;
          case INVALID_USERNAME:
            log.warn("Unknown username ({}).", username);
            target.appendJavaScript("padmin.displayError(['" + username + " cannot be found in the system.']);");
            break;
          case SUCCESS:
            log.info("User {} signed in.", username);
            PadminSession.get().setUser(loginResult.getRight());
            setResponsePage(WicketApplication.get().getHomePage());
            break;
          case FAILED:
          case UNKNOWN_FAILURE:
            log.error("Unexpected error while trying to sign in {}.", username);
            target.appendJavaScript("padmin.displayError(['An unexpected error occured.  Please cry for a while.']);");
            break;
        }
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