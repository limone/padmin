package padmin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="padmin_config")
@NamedQueries({
  @NamedQuery(name="Config.GetByKey", query="SELECT c FROM Config c where c.key=:key")
})
public class Config implements Serializable {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="config_id")
  private Long id;
  
  @Column(nullable=false, updatable=false, unique=true)
  private String key;
  
  @Column
  private String value;
  
  public Config() {
    // empty
  }

  public Config(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public String toString() {
    return "Config [id=" + id + ", key=" + key + ", value=" + value + "]";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}