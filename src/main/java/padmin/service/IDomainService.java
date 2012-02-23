package padmin.service;

import java.io.Serializable;
import java.util.List;

import padmin.model.Domain;
import padmin.model.Record;

public interface IDomainService extends Serializable {
  public Domain getDomain(Long domainId);

  public List<Domain> getDomains();

  public Domain saveDomain(Domain domain);
  
  public void deleteDomain(Domain domain);
  
  public Record getRecord(Long recordId);
  
  public void deleteRecord(Record record);
}