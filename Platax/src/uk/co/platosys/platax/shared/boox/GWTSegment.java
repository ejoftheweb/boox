package uk.co.platosys.platax.shared.boox;

import java.util.ArrayList;
import java.util.List;

public class GWTSegment implements GWTSelectable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
String name;
String sysname;
   String description;
   String instructions;
   List<GWTModule> modules=new ArrayList<GWTModule>();
   int rowIndex=0;
   
	boolean multiselect;
	
	public GWTSegment(){}

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
	   public List<GWTModule> getModules() {
			return modules;
		}

		public void setModules(List<GWTModule> modules) {
			this.modules = modules;
		}
   public void addModule(GWTModule module){
	   this.modules.add(module);
   }
	

	public boolean isMultiSelect() {
		return multiselect;
	}

	public void setMultiSelect(boolean multiselect) {
		this.multiselect = multiselect;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	@Override
	public String getSysname() {
		return sysname;
	}

	@Override
	public void setSysname(String sysname) {
		this.sysname=sysname;
	}
   
	 
	

}
