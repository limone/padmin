package padmin.resource;

import org.apache.wicket.request.resource.AbstractResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import padmin.app.PadminException;

public class ClosureResourceReference extends AbstractResource {
  protected final Logger log = LoggerFactory.getLogger(ClosureResourceReference.class);
  protected String jsContent = "";
  
  public ClosureResourceReference() {
    new Thread() {
      @Override
      public void run() {
        try {
          jsContent = ClosureCompiler.compileJavaScript(new String[]{"jquery-1.7.1.min.js", "jquery-ui-1.8.17.custom.min.js"}, new String[]{"plugins.js", "scripts.js"});
        } catch (PadminException pe) {
          log.error("Could not compile JS.", pe);
        }
      }
    }.start();
  }
  
  @Override
  protected ResourceResponse newResourceResponse(Attributes attributes) {
    if (jsContent.isEmpty()) {
      log.warn("No JavaScript content available - closure compiler still going.");
    }
    
    final ResourceResponse resourceResponse = new ResourceResponse();
    resourceResponse.setWriteCallback(new WriteCallback() {
      @Override
      public void writeData(Attributes attribs) {
        attribs.getResponse().write(jsContent.getBytes());
      }
    });
    return resourceResponse;
  }
}
