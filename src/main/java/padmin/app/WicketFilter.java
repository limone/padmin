package padmin.app;

import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.wicketstuff.servlet3.WicketFilter3;

public final class WicketFilter extends WicketFilter3 {
  @Autowired
  protected WicketApplication app;

  @Override
  protected IWebApplicationFactory getApplicationFactory() {
    return new IWebApplicationFactory() {
      @Override
      public WebApplication createApplication(org.apache.wicket.protocol.http.WicketFilter filter) {
        app.setWicketFilter(filter);
        return app;
      }

      @Override
      public void destroy(org.apache.wicket.protocol.http.WicketFilter filter) {
        filter.destroy();
      }
    };
  }

}