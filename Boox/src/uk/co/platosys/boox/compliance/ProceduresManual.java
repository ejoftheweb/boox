package uk.co.platosys.boox.compliance;

import java.util.ArrayList;
import java.util.List;

import uk.co.platosys.boox.core.Enterprise;

public class ProceduresManual {
	public static final String TABLE_NAME="bx_tasks";
	public static final String TASKNAME_COLNAME="Task";
	public static final String TASKDESCRIPTION_COLNAME="Description";
	public static final String FREQUENCY_COLNAME="Frequency";
	public static final String LAST_DATE_COLNAME="last_date";
	public static final String NEXT_DATE_COLNAME="next_date";
	public static final String ROLE_COLNAME="role";
	public static final String OWNER_COLNAME="owner";
	public static final String DELEGATE_COLNAME="delegate";
	List<Task> procedures;

	private ProceduresManual(Enterprise enterprise) {
		this.procedures=new ArrayList<Task>();
	}
    public static ProceduresManual getProceduresManual(Enterprise enterprise){
    	return new ProceduresManual(enterprise);
    }
}
