package padmin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import padmin.data.IGenericDao;
import padmin.model.Config;

@Service
@Transactional
public class ConfigService implements IConfigService {
  @Autowired
  private IGenericDao gd;
  
  @Override
  public void saveConfig(Config config) {
    gd.merge(config);
  }

  @Override
  public Config getConfig(String key) {
    Map<String,Object> params = new HashMap<String,Object>();
    params.put("key", key);
    return gd.namedQuerySingle("Config.GetByKey", params);
  }

  @Override
  public List<Config> getAllConfigs() {
    return gd.query("SELECT c FROM Config c", null);
  }
}