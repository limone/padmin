package padmin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import padmin.data.IGenericDao;
import padmin.model.Domain;
import padmin.model.Record;

@Service
@Transactional
public class DomainService implements IDomainService {
  private static final Logger log = LoggerFactory.getLogger(DomainService.class);
  
  @Autowired
  private IGenericDao gd;
  
  public DomainService() {
    // empty
  }
  
  @Override
  public List<Domain> getDomains() {
    log.debug("Retrieving all domains.");
    return gd.query("SELECT d FROM Domain d ORDER BY name", null);
  }
  
  @Override
  public Domain getDomain(Long domainId) {
    log.debug("Retrieving domain for ID {}.", domainId);
    return gd.find(Domain.class, domainId);
  }
  
  @Override
  public Domain saveDomain(Domain domain) {
    log.debug("Saving {}.", domain.toString());
    return gd.merge(domain);
  }

  @Override
  public void deleteDomain(Domain domain) {
    log.debug("Removing {}.", domain.toString());
    Domain tmpDomain = getDomain(domain.getId());
    gd.remove(tmpDomain);
  }

  @Override
  public Record getRecord(Long recordId) {
    log.debug("Retrieving record for ID {}.", recordId);
    return gd.find(Record.class, recordId);
  }

  @Override
  public void deleteRecord(Record record) {
    log.debug("Removing {}.", record.toString());
    Record tmpRecord = getRecord(record.getId());
    gd.remove(tmpRecord);
  }
}