package padmin.wicket;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

public abstract class FilteredListModel<K> extends LoadableDetachableModel<List<K>> {
  private IModel<List<? extends K>> inner;

  @Override
  protected void onDetach() {
    inner.detach();
  }

  public FilteredListModel(IModel<List<? extends K>> inner) {
    this.inner = inner;
  }

  public FilteredListModel(List<? extends K> inner) {
    this.inner = Model.ofList(inner);
  }

  @Override
  protected final List<K> load() {
    List<? extends K> input = inner.getObject();
    List<K> result = new ArrayList<K>(input.size());
    for (K k : input) {
      if (accept(k)) result.add(k);
    }
    return result;
  }

  protected abstract boolean accept(K k);
}