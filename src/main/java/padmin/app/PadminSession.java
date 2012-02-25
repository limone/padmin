package padmin.app;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import padmin.model.User;

public class PadminSession extends WebSession {
  private User user;
  
  public PadminSession(Request request) {
    super(request);
  }
  
  public static PadminSession get()
  {
    return (PadminSession)Session.get();
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  public boolean isSignedIn() {
    return user != null;
  }
}