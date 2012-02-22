package padmin.page.config;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import padmin.BasePage;
import padmin.model.Config;
import padmin.service.IConfigService;
import padmin.wicket.FilteredListModel;

@MountPath("/config.html")
public class ConfigPage extends BasePage {
  protected static final Logger log = LoggerFactory.getLogger(ConfigPage.class);
  
  @SpringBean
  private IConfigService cs;

  public ConfigPage() {
    final ConfigModel model = new ConfigModel();
    setDefaultModel(new CompoundPropertyModel<ConfigModel>(model));

    model.setConfigureItems(cs.getAllConfigs());

    final Form<ConfigModel> configureForm = new Form<ConfigModel>("configureForm");

    final WebMarkupContainer configureContainer = new WebMarkupContainer("configureContainer");
    configureForm.add(configureContainer.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

    configureContainer.add(new ListView<Config>("configureItems", new FilteredListModel<Config>(model.getConfigureItems()) {
      @Override
      protected boolean accept(Config k) {
        if (k == null || k.getKey().equalsIgnoreCase("is.configured")) { return false; }
        return true;
      }
    }) {
      @Override
      protected void populateItem(ListItem<Config> item) {
        final Config config = item.getModelObject();

        item.add(new Label("configName", new ResourceModel(config.getKey())));
        item.add(new AjaxEditableLabel<String>("configValue", new PropertyModel<String>(config, "value")));
      }
    });
    
    configureForm.add(new AjaxButton("submitButton", configureForm) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        log.debug("Saving configuration options.");
        log.debug(model.toString());
      }
      
      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
        log.warn("Could not validate form.");
      }
    });

    add(configureForm);
  }

  private static final class ConfigModel implements Serializable {
    private List<Config> configureItems;

    public ConfigModel() {
      // empty
    }

    @Override
    public String toString() {
      return "ConfigModel [configureItems=" + configureItems + "]";
    }

    public List<Config> getConfigureItems() {
      return configureItems;
    }

    public void setConfigureItems(List<Config> configureItems) {
      this.configureItems = configureItems;
    }
  }
}