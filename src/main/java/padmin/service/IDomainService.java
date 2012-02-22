package padmin.service;

import java.io.Serializable;
import java.util.List;

import padmin.model.Domain;

public interface IDomainService extends Serializable {
  public Domain getDomain(Long domainId);

  public List<Domain> getDomains();

  public void saveDomain(Domain domain);
  
  public void deleteDomain(Domain domain);
}