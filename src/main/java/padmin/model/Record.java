package padmin.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name="records")
public class Record implements Serializable {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(cascade={javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
  @JoinColumn(name="domain_id", nullable=false, updatable=false)
  @Cascade(CascadeType.SAVE_UPDATE)
  private Domain domain;
  
  @Column
  private String name;
  
  @Column
  private String type;
  
  @Column
  private String content;
  
  @Column
  private Integer ttl = 3600;
  
  @Column
  private Integer prio = 0;
  
  @Column(name="change_date")
  private Long changeDate;
  
  public Record() {
    // empty
  }

  @Override
  public String toString() {
    return "Record [id=" + id + ", domain=" + domain.getName() + ", name=" + name + ", type=" + type + ", content=" + content + ", ttl=" + ttl + ", prio=" + prio + ", changeDate=" + changeDate + "]";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Domain getDomain() {
    return domain;
  }

  public Record setDomain(Domain domain) {
    this.domain = domain;
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getTtl() {
    return ttl;
  }

  public void setTtl(Integer ttl) {
    this.ttl = ttl;
  }

  public Integer getPrio() {
    return prio;
  }

  public void setPrio(Integer prio) {
    this.prio = prio;
  }

  public Long getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Long changeDate) {
    this.changeDate = changeDate;
  }
}