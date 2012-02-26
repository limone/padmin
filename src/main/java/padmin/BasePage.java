package padmin;

import org.apache.wicket.Application;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.string.UrlUtils;
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
    add(new WebComponent("ie9js").add(new AttributeModifier("src", new Model<>(UrlUtils.rewriteToContextRelative("js/ie9.js", RequestCycle.get())))));
    
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

  @Override
  public void renderHead(HtmlHeaderContainer container) {
    super.renderHead(container);
    
    container.getHeaderResponse().renderJavaScriptReference("js/jquery-1.7.1.min.js");
    container.getHeaderResponse().renderJavaScriptReference("js/jquery-ui-1.8.17.custom.min.js");
    container.getHeaderResponse().renderJavaScriptReference("js/plugins.js");
    container.getHeaderResponse().renderJavaScriptReference("js/scripts.js");
    
    container.getHeaderResponse().renderCSSReference("css/jquery-ui-1.8.17.custom.css");
    container.getHeaderResponse().renderCSSReference("css/grid.css");
    container.getHeaderResponse().renderCSSReference("css/style.css");
  }
  
  private static final class UnconfiguredFragment extends Fragment {
    public UnconfiguredFragment(String id, String markupId, MarkupContainer markupProvider) {
      super(id, markupId, markupProvider);
    }
  }
}