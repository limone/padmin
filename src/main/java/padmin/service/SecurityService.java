package padmin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import padmin.data.IGenericDao;
import padmin.model.User;

@Service
public class SecurityService implements ISecurityService {
  private static final Logger log = LoggerFactory.getLogger(SecurityService.class);
  
  @Autowired
  private IGenericDao gd;

  @Override
  public User getUser(Long id) {
    log.debug("Searching for user with ID {}.", id);
    return gd.find(User.class, id);
  }

  @Override
  public User getUserByEmail(String emailAddress) {
    log.debug("Searching for user with email address {}.", emailAddress);
    return null;
  }

  @Override
  public User saveUser(User user) {
    log.debug("Saving user {}.", user.toString());
    User tmpUser = gd.merge(user);
    return tmpUser;
  }

  @Override
  public void deleteUser(User user) {
    log.debug("Removing user {}.", user.toString());
    gd.remove(getUser(user.getId()));
  }

  @Override
  public User getUserByUsername(String username) {
    return null;
  }
}