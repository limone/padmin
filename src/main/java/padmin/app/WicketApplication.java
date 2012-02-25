package padmin.app;

import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.AbstractResource.ResourceResponse;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.request.resource.caching.ResourceUrl;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import padmin.BasePage;
import padmin.model.Config;
import padmin.page.HomePage;
import padmin.page.security.SignInPage;
import padmin.security.PageAnnotationAuthorizationStrategy;
import padmin.service.IConfigService;

@Component
public class WicketApplication extends WebApplication {
  private static final Logger log = LoggerFactory.getLogger(WicketApplication.class);
  private static final String DEFAULT_ENCODING = "UTF-8";
  
  @Autowired
  private IConfigService cs;
  
  public WicketApplication() {
    // empty
  }
  
  public static WicketApplication get()
  {
    return (WicketApplication) Application.get();
  }

  @Override
  protected void init() {
    log.debug("Bringing Wicket application online.");
    
    super.init();
    getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    new AnnotatedMountScanner().scanPackage("padmin").mount(this);

    getMarkupSettings().setDefaultMarkupEncoding(DEFAULT_ENCODING);
    getRequestCycleSettings().setResponseRequestEncoding(DEFAULT_ENCODING);
    
    getSecuritySettings().setAuthorizationStrategy(new PageAnnotationAuthorizationStrategy(getLoginPage()));

    if (getConfigurationType().equals(RuntimeConfigurationType.DEVELOPMENT)) {
      getMarkupSettings().setStripWicketTags(true);
      getMarkupSettings().setStripComments(true);
      getMarkupSettings().setCompressWhitespace(true);

      getDebugSettings().setAjaxDebugModeEnabled(true);
      getDebugSettings().setComponentUseCheck(true);
      getDebugSettings().setDevelopmentUtilitiesEnabled(true);

      getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
    }
    
    getResourceSettings().setCachingStrategy(new IResourceCachingStrategy() {
      @Override
      public void undecorateUrl(ResourceUrl url) {
        // empty
      }
      
      @Override
      public void decorateUrl(ResourceUrl url, IStaticCacheableResource resource) {
        // empty
      }
      
      @Override
      public void decorateResponse(ResourceResponse response, IStaticCacheableResource resource) {
        // empty
      }
    });
    
    Config isConfigured = cs.getConfig("is.configured");
    if (isConfigured == null) {
      isConfigured = new Config("is.configured", "false");
      cs.saveConfig(isConfigured);
    }
    
    Config defaultSoa = cs.getConfig("default.soa");
    if (defaultSoa == null) {
      defaultSoa = new Config("default.soa", "localhost. hostmaster.localhost. [SERIAL]");
      cs.saveConfig(defaultSoa);
    }
    
    Config defaultNs1 = cs.getConfig("default.ns.1");
    if (defaultNs1 == null) {
      defaultNs1 = new Config("default.ns.1", "localhost");
      cs.saveConfig(defaultNs1);
    }
    
    Config defaultNs2 = cs.getConfig("default.ns.2");
    if (defaultNs2 == null) {
      defaultNs2 = new Config("default.ns.2", "localhost");
      cs.saveConfig(defaultNs2);
    }
    
    log.debug("Wicket application up and running.");
  }
  
  public boolean isPadminConfigured() {
    Config isConfigured = cs.getConfig("is.configured");
    if (isConfigured == null || isConfigured.getValue() == null || !isConfigured.getValue().equalsIgnoreCase("true")) {
      return false;
    }
    return true;
  }

  @Override
  public Session newSession(Request request, Response response) {
    return new PadminSession(request);
  }

  @Override
  public Class<? extends BasePage> getHomePage() {
    return HomePage.class;
  }
  
  public Class<? extends WebPage> getLoginPage() {
    return SignInPage.class;
  }
}