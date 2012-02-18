package padmin.app;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.wicketstuff.servlet3.WicketFilter3;

@WebFilter(value = "/*", initParams = { @WebInitParam(name = "applicationFactoryClassName", value = "org.apache.wicket.spring.SpringWebApplicationFactory") })
public final class WicketFilter extends WicketFilter3 {
  // empty
}