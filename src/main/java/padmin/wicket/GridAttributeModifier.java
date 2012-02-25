package padmin.wicket;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;

public final class GridAttributeModifier extends AttributeModifier {
  public GridAttributeModifier(String cssClass) {
    super("class", new Model<>(cssClass));
  }
}