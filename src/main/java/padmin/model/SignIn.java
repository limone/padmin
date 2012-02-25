package padmin.model;

import java.io.Serializable;

public class SignIn implements Serializable {
  private String username;
  private String password;
  
  public SignIn() {
    // empty
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
}