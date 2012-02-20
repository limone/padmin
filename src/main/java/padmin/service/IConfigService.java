package padmin.service;

import java.io.Serializable;
import java.util.Map;

public interface IConfigService extends Serializable {
  public void saveConfig(Map<String,String> config);
  public Map<String,String> loadConfig();
}