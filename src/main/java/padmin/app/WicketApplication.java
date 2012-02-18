package padmin.app;

import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import padmin.BasePage;
import padmin.page.HomePage;

@Component
public class WicketApplication extends WebApplication {
  private static final Logger log = LoggerFactory.getLogger(WicketApplication.class);
  private static final String DEFAULT_ENCODING = "UTF-8";

  @Override
  protected void init() {
    log.debug("Bringing Wicket application online.");
    
    super.init();
    getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    new AnnotatedMountScanner().scanPackage("padmin").mount(this);

    getMarkupSettings().setDefaultMarkupEncoding(DEFAULT_ENCODING);
    getRequestCycleSettings().setResponseRequestEncoding(DEFAULT_ENCODING);

    if (getConfigurationType().equals(RuntimeConfigurationType.DEVELOPMENT)) {
      getMarkupSettings().setStripWicketTags(true);
      getMarkupSettings().setStripComments(true);
      getMarkupSettings().setCompressWhitespace(true);

      getDebugSettings().setAjaxDebugModeEnabled(true);
      getDebugSettings().setComponentUseCheck(true);
      getDebugSettings().setDevelopmentUtilitiesEnabled(true);

      getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
    }
    
    log.debug("Wicket application up and running.");
  }

  @Override
  public Class<? extends BasePage> getHomePage() {
    return HomePage.class;
  }
}