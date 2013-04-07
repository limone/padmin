package padmin;

import org.apache.wicket.Application;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import padmin.app.PadminSession;
import padmin.app.WicketApplication;
import padmin.page.config.ConfigPage;
import padmin.page.security.SignInPage;
import padmin.security.RequiresAuthentication;

@RequiresAuthentication
public class BasePage extends WebPage {
  protected final Logger internalLog = LoggerFactory.getLogger(BasePage.class);
  
  public BasePage() {
    final WebMarkupContainer loggedInContainer = new WebMarkupContainer("loggedInContainer");
    add(loggedInContainer.setVisible(PadminSession.get().isSignedIn()));
    
    loggedInContainer.add(new BookmarkablePageLink<>("homeLink", Application.get().getHomePage()));
    loggedInContainer.add(new BookmarkablePageLink<>("configLink", ConfigPage.class));
    loggedInContainer.add(new Link<Object>("logoutLink") {
      @Override
      public void onClick() {
        internalLog.debug("{} is signing out.", PadminSession.get().getUser().getUsername());
        PadminSession.get().setUser(null);
        PadminSession.get().invalidate();
        setResponsePage(WicketApplication.get().getLoginPage());
      }
    });
    
    if (WicketApplication.get().isPadminConfigured() || getPage() instanceof ConfigPage || getPage() instanceof SignInPage) {
      add(new WebMarkupContainer("configured").setVisible(false));
    } else {
      add(new UnconfiguredFragment("configured", "configuredFragment", this));
    }
  }
  
  private static final class UnconfiguredFragment extends Fragment {
    public UnconfiguredFragment(String id, String markupId, MarkupContainer markupProvider) {
      super(id, markupId, markupProvider);
    }
  }
}