package padmin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="domains")
public class Domain implements Serializable {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  
  @Column
  private String name;
  
  @Column
  private String master;
  
  @Column(name="last_check")
  private Long lastCheck;
  
  @Column
  private String type;
  
  @Column(name="notified_serial")
  private Long notifiedSerial;
  
  @Column
  private String account;
  
  @OneToMany(mappedBy="domain", cascade=javax.persistence.CascadeType.ALL)
  @Cascade(CascadeType.ALL)
  private List<Record> records = new ArrayList<Record>();
  
  public Domain() {
    // empty
  }

  public Domain(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Domain [id=" + id + ", name=" + name + ", master=" + master + ", lastCheck=" + lastCheck + ", type=" + type + ", notifiedSerial=" + notifiedSerial + ", account=" + account + ", records=" + records + "]";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMaster() {
    return master;
  }

  public void setMaster(String master) {
    this.master = master;
  }

  public Long getLastCheck() {
    return lastCheck;
  }

  public void setLastCheck(Long lastCheck) {
    this.lastCheck = lastCheck;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getNotifiedSerial() {
    return notifiedSerial;
  }

  public void setNotifiedSerial(Long notifiedSerial) {
    this.notifiedSerial = notifiedSerial;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public List<Record> getRecords() {
    return records;
  }

  public void setRecords(List<Record> records) {
    this.records = records;
  }
}