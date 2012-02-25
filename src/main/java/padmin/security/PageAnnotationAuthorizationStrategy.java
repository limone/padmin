package padmin.security;

import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;

import padmin.app.PadminSession;

public class PageAnnotationAuthorizationStrategy extends AbstractPageAuthorizationStrategy {
  private Class<? extends WebPage> loginPage;
  
  public PageAnnotationAuthorizationStrategy(Class<? extends WebPage> loginPage) {
    this.loginPage = loginPage;
  }
  
  @Override
  protected <T extends Page> boolean isPageAuthorized(Class<T> pageClass) {
    if (pageClass.isAnnotationPresent(RequiresAuthentication.class)) {
      if (pageClass.isAnnotationPresent(BypassAuthentication.class)) {
        return true;
      }
      
      checkAuthenticated();
      
      return true;
    }
    
    return true;
  }
  
  private void checkAuthenticated() {
    if (!PadminSession.get().isSignedIn()) {
      throw new RestartResponseAtInterceptPageException(this.loginPage);
    }
  }
}