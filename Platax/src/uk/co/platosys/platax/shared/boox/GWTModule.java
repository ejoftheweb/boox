package uk.co.platosys.platax.shared.boox;
import java.io.Serializable;

import uk.co.platosys.pws.values.ValuePair;

public class GWTModule implements Serializable, GWTSelectable, ValuePair {
/**
	 * 
	 */
	private static final long serialVersionUID = -472981090717729594L;
private String name;
private String sysname;
private String description;
private boolean isMultiSelect;
private String segment;
private boolean isSelected;
	public GWTModule() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isMultiSelect() {
		return isMultiSelect;
	}
	public void setMultiSelect(boolean isMultiSelect) {
		this.isMultiSelect = isMultiSelect;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	@Override
	public String getSysname() {
		return sysname;
	}
	
	@Override
	public void setSysname(String sysname) {
		this.sysname=sysname;
	}
	
	@Override
	public String getLabel() {
		return description;
	}
	@Override
	public void setLabel(String label) {
	}
	@Override
	public String getValue() {
		return name;
	}
	

}
