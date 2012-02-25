package padmin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long    id;

  @Column(length = 16, nullable = false)
  private String  username;

  @Column(length = 34, nullable = false)
  private String  password;

  @Column(nullable = false)
  private String  fullname;

  @Column(nullable = false)
  private String  email;

  @Column(nullable = false, length = 2000)
  private String  description;

  @Column(nullable = false)
  private Boolean active = false;
  
  public User() {
    // empty
  }

  public User(String username, String password, String fullname, String email, String description, Boolean active) {
    this.username = username;
    this.password = password;
    this.fullname = fullname;
    this.email = email;
    this.description = description;
    this.active = active;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", password=" + password + ", fullname=" + fullname + ", email=" + email + ", description=" + description + ", active=" + active + "]";
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}