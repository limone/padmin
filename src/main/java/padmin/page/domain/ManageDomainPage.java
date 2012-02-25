package padmin.page.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.annotation.mount.MountPath;

import padmin.BasePage;
import padmin.model.Domain;
import padmin.model.Record;
import padmin.service.IConfigService;
import padmin.service.IDomainService;
import padmin.wicket.GridAttributeModifier;

@MountPath("/domain/manage")
public class ManageDomainPage extends BasePage {
  protected static final Logger       log   = LoggerFactory.getLogger(ManageDomainPage.class);

  protected static final List<String> types = Arrays.asList(new String[] { "A", "AAAA", "CNAME", "HINFO", "MX", "NAPTR", "NS", "PTR", "SOA", "SPF", "SRV", "SSHFP", "TXT", "RP" });

  @SpringBean
  protected IDomainService            ds;

  @SpringBean
  protected IConfigService            cs;

  public ManageDomainPage() {
    init(null);
  }

  public ManageDomainPage(PageParameters params) {
    if (params.isEmpty()) {
      log.error("ManageDomainPage was invoked with parameters, but they were empty?!");
      setResponsePage(Application.get().getApplicationSettings().getInternalErrorPage());
      return;
    } else if (!params.getNamedKeys().contains("id")) {
      log.error("ManageDomainPage was invoked with parameters, but didn't include the domain ID.");
      setResponsePage(Application.get().getApplicationSettings().getInternalErrorPage());
      return;
    }

    Long domainId = params.get("id").toLongObject();
    if (domainId == null) {
      log.error("ManageDomainPage was invoked with parameters, the domain ID was null.");
      setResponsePage(Application.get().getApplicationSettings().getInternalErrorPage());
      return;
    }

    init(domainId);
  }

  private final void init(Long domainId) {
    Domain tmpDomain;
    if (domainId == null) {
      tmpDomain = new Domain();
      tmpDomain.setType("MASTER");
      final String soa = cs.getConfig("default.soa").getValue();
      final String ns1 = cs.getConfig("default.ns.1").getValue();
      final String ns2 = cs.getConfig("default.ns.2").getValue();
      
      tmpDomain.getRecords().add(new Record(tmpDomain, tmpDomain.getName(), "SOA", soa));
      tmpDomain.getRecords().add(new Record(tmpDomain, tmpDomain.getName(), "NS", ns1));
      tmpDomain.getRecords().add(new Record(tmpDomain, tmpDomain.getName(), "NS", ns2));
    } else {
      tmpDomain = ds.getDomain(domainId);
    }

    final Domain d = tmpDomain;
    final CompoundPropertyModel<Domain> model = new CompoundPropertyModel<>(d);
    setDefaultModel(model);

    final Label domainName = new Label("title", new Model<>(domainId == null ? "Create Domain" : "Edit " + d.getName()));
    add(domainName.setOutputMarkupId(true));

    final Form<Domain> manageForm = new Form<>("manageDomainForm");
    manageForm.add(new AjaxEditableLabel<String>("name").setRequired(true));

    final List<Long> selected = new ArrayList<>();
    final CheckGroup<Long> selectedRecords = new CheckGroup<>("checkGroup", selected);
    manageForm.add(selectedRecords);

    final WebMarkupContainer recordContainer = new WebMarkupContainer("recordContainer");
    selectedRecords.add(recordContainer.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

    final ListView<Record> records = new ListView<Record>("records") {
      @Override
      protected void populateItem(ListItem<Record> item) {
        final Record r = item.getModelObject();
        item.add(new Check<>("checkbox", new PropertyModel<Long>(r, "id"), selectedRecords));
        item.add(new HiddenField<>("recordId", new PropertyModel<Long>(r, "id")));
        item.add(new AjaxEditableLabel<>("recordName", new PropertyModel<String>(r, "name")));
        item.add(new AjaxEditableChoiceLabel<String>("recordType", new PropertyModel<String>(r, "type"), types) {
          @Override
          public void onEdit(AjaxRequestTarget target) {
            getEditor().add(new GridAttributeModifier("grid_1"));
            super.onEdit(target);
          }
        });

        item.add(new AjaxEditableLabel<String>("recordContent", new PropertyModel<String>(r, "content")) {
          @Override
          public void onEdit(AjaxRequestTarget target) {
            getEditor().add(new GridAttributeModifier("grid_4"));
            super.onEdit(target);
          }
        });

        item.add(new AjaxEditableLabel<String>("recordPriority", new PropertyModel<String>(r, "prio")) {
          @Override
          public void onEdit(AjaxRequestTarget target) {
            getEditor().add(new GridAttributeModifier("grid_0_5"));
            super.onEdit(target);
          }
        });

        item.add(new AjaxEditableLabel<String>("recordTtl", new PropertyModel<String>(r, "ttl")) {
          @Override
          public void onEdit(AjaxRequestTarget target) {
            getEditor().add(new GridAttributeModifier("grid_0_5"));
            super.onEdit(target);
          }
        });
      }
    };
    recordContainer.add(records);

    manageForm.add(new AjaxLink<Object>("addRow") {
      @Override
      public void onClick(AjaxRequestTarget target) {
        log.debug("Adding a row.");
        final Domain domain = model.getObject();

        if (domain == null || domain.getId() == null) {
          log.warn("Cannot add a row if the domain has not yet been saved.");
          target.appendJavaScript("padmin.displayError('Please save the domain before trying to add a record.');");
          return;
        }

        domain.getRecords().add(new Record().setDomain(domain));
        final Domain newDomain = ds.saveDomain(domain);
        model.setObject(newDomain);
        target.add(recordContainer);
      }
    });

    manageForm.add(new AjaxButton("deleteRecords", manageForm) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        final List<Long> selectedRecs = selected;
        log.debug("Trying to delete records {}.", selectedRecs);

        if (!selectedRecs.isEmpty()) {
          for (Long recordId : selectedRecs) {
            if (recordId != null) {
              final Record rec = ds.getRecord(recordId);
              log.debug("Record: {}", rec);
              if (rec != null) {
                ds.deleteRecord(rec);
              }
            }
          }
          selected.clear();
        }

        final Domain newDomain = ds.getDomain(model.getObject().getId());
        log.debug("Refreshing domain model: {}", newDomain.toString());

        model.setObject(newDomain);
        domainName.setDefaultModel(new Model<>("Edit " + model.getObject().getName()));
        target.add(domainName);
        target.add(manageForm);
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
        log.debug("Errors while trying to delete records.");
      }
    });

    manageForm.add(new AjaxButton("saveChanges", new Model<>("Save Changes"), manageForm) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        final Domain domain = model.getObject();
        if (domain == null || domain.getName() == null || domain.getName().isEmpty()) {
          target.appendJavaScript("padmin.displayError('Please provide a name for this domain.');");
          return;
        }

        log.debug("Saving changes.");
        final Domain newDomain = ds.saveDomain(domain);
        log.debug("Refreshing domain model: {}", newDomain.toString());

        model.setObject(newDomain);
        domainName.setDefaultModel(new Model<>("Edit " + newDomain.getName()));
        target.add(domainName);
        target.add(manageForm);
      }

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {
        log.debug("Errors while trying to save changes.");
      }
    });

    add(manageForm.setOutputMarkupId(true));
  }
}