package uk.co.platosys.platax.shared.boox;

import java.io.Serializable;

public interface GWTSelectable extends Serializable {
 public String getName();
 public String getSysname();
 public String getDescription();
 public void setName(String name);
 public void setSysname(String sysname);
 public void setDescription(String description);
 public boolean isMultiSelect();
}
