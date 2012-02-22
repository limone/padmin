package padmin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import padmin.data.IGenericDao;
import padmin.model.Config;

@Service
@Transactional
public class ConfigService implements IConfigService {
  private static final Logger log = LoggerFactory.getLogger(ConfigService.class);
  
  @Autowired
  private IGenericDao gd;
  
  @Override
  public void saveConfig(Config config) {
    log.debug("Saving configuration.");
    gd.merge(config);
  }

  @Override
  public Config getConfig(String key) {
    log.debug("Loading configuration.");
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("key", key);
    return gd.namedQuerySingle("Config.GetByKey", params);
  }

  @Override
  public List<Config> getAllConfigs() {
    return gd.query("SELECT c FROM Config c", null);
  }
}