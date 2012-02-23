package padmin.page;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import padmin.BasePage;
import padmin.model.Domain;
import padmin.page.domain.ManageDomainPage;
import padmin.service.IDomainService;

@MountPath("/home.html")
public class HomePage extends BasePage {
  protected static final Logger log = LoggerFactory.getLogger(HomePage.class);
  
  @SpringBean
  IDomainService ds;
  
  public HomePage() {
    final ListDomainsModel domains = new ListDomainsModel(ds.getDomains());
    
    log.debug("There are {} domains in pDNS.", domains.size());
    
    final WebMarkupContainer domainsContainer = new WebMarkupContainer("domainsContainer");
    add(domainsContainer.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
    
    domainsContainer.add(new ListView<Domain>("domains", domains) {
      @Override
      protected void populateItem(ListItem<Domain> item) {
        final Domain domain = item.getModelObject();
        final AjaxEditableLabel<String> nameLabel = new AjaxEditableLabel<>("domainName", new Model<>(domain.getName()));
        nameLabel.add(new IValidator<String>() {
          @Override
          public void validate(IValidatable<String> validatable) {
            log.debug("Validating domain name: {}", validatable.getValue());
          }
        });
        item.add(nameLabel.setType(String.class).setRequired(true));
        
        item.add(new Label("recordCount", Integer.toString(item.getModelObject().getRecords().size())));
        
        item.add(new AjaxLink<Object>("deleteLink") {
          @Override
          public void onClick(AjaxRequestTarget target) {
            log.debug("User wants to delete {}.", domain.getName());
            ds.deleteDomain(domain);
            domains.refreshDomains();
            target.add(domainsContainer);
          }

          @Override
          protected IAjaxCallDecorator getAjaxCallDecorator() {
            return new AjaxCallDecorator() {
              @Override
              public CharSequence decorateScript(Component c, CharSequence script) {
                return "padmin.deleteLink('" + domain.getName() + "', function() {" + script + "});";
              }
            };
          }
        });
        
        item.add(new Link<Object>("editLink") {
          @Override
          public void onClick() {
            log.debug("User wants to edit {}.", domain.getName());
            setResponsePage(ManageDomainPage.class, new PageParameters().add("id", domain.getId()));
          }
        });
      }
    });
    
    add(new BookmarkablePageLink<>("addDomainLink", ManageDomainPage.class));
  }
  
  private final class ListDomainsModel extends ListModel<Domain> {
    public ListDomainsModel(List<Domain> domains) {
      super(domains);
    }
    
    public int size() {
      return getObject().size();
    }
    
    public void refreshDomains() {
      this.setObject(ds.getDomains());
    }
  }
}