package padmin.service;

import java.io.Serializable;
import java.util.List;

import padmin.model.Config;

public interface IConfigService extends Serializable {
  public void saveConfig(Config config);
  public Config getConfig(String key);
  public List<Config> getAllConfigs();
}