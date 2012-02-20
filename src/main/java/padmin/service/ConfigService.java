package padmin.service;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

@Service
@Transactional
public class ConfigService implements IConfigService, ServletContextAware {
  private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
  private Map<String,String> config;
  
  @SuppressWarnings("hiding")
  @Override
  public void saveConfig(Map<String, String> config) {
    log.debug("Saving configuration.");
    this.config = config;
  }

  @Override
  public Map<String, String> loadConfig() {
    log.debug("Loading configuration.");
    return config;
  }

  @Override
  public void setServletContext(ServletContext servletContext) {
    final String realPath = servletContext.getRealPath("/");
    log.debug("Attempting to see if {} can be used for configuration.", realPath);
    
    final File realPathDir = new File(realPath);
    if (realPathDir.exists() && realPathDir.isDirectory() && realPathDir.canRead() && realPathDir.canWrite()) {
      // empty
    }
  }
}