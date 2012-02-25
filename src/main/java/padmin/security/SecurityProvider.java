package padmin.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import padmin.model.User;
import padmin.service.ISecurityService;

@Service
public class SecurityProvider implements Serializable {
  private static final Logger log = LoggerFactory.getLogger(SecurityProvider.class);
  
  @Autowired
  private ISecurityService secSvc;

  private String username;
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public LoginStatusType login() {
    try {
      // Perform Auth here
      User u = secSvc.getUserByUsername(username);
      if (u == null) {
        return LoginStatusType.INVALID_USERNAME; 
      }
      
      // Validate password
      if (!u.getPassword().equals(password)) {
        return LoginStatusType.BAD_CREDENTIALS;
      }
      
      if (!u.getActive()) {
        return LoginStatusType.DISABLED;
      }
      
      return LoginStatusType.SUCCESS;
    } catch (Exception ex) {
      log.error("Could not login user due to unexpected error.", ex);
      return LoginStatusType.UNKNOWN_FAILURE;
    }
  }
}