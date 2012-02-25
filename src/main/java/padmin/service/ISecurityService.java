package padmin.service;

import java.io.Serializable;

import padmin.model.User;

public interface ISecurityService extends Serializable {
  public User getUser(Long id);
  
  public User getUserByUsername(String username);
  
  public User getUserByEmail(String emailAddress);
  
  public User saveUser(User user);
  
  public void deleteUser(User user);
}