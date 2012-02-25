package padmin.security;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.tuple.Pair;
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
  
  public SecurityProvider() {
    // empty
  }

  public Pair<LoginStatusType,User> login(String username, String password) {
    try {
      // Perform Auth here
      User u = secSvc.getUserByUsername(username);
      if (u == null) {
        return Pair.of(LoginStatusType.INVALID_USERNAME, null); 
      }
      
      // Validate password
      final String hashedPassword = DigestUtils.md5Hex(password);
      log.debug("Validating incoming {} against existing {}.", hashedPassword, u.getPassword());
      if (!hashedPassword.equals(u.getPassword())) {
        return Pair.of(LoginStatusType.BAD_CREDENTIALS, null);
      }
      
      if (!u.getActive()) {
        return Pair.of(LoginStatusType.DISABLED, null);
      }
      
      return Pair.of(LoginStatusType.SUCCESS, u);
    } catch (Exception ex) {
      log.error("Could not login user due to unexpected error.", ex);
      return Pair.of(LoginStatusType.UNKNOWN_FAILURE, null);
    }
  }
}