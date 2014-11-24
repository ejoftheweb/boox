package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

public interface GWTSelectable extends Serializable {
 public String getName();
 public String getDescription();
 public boolean isMultiSelect();
}
