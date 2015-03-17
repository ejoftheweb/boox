package uk.co.platosys.platax.server.services;

import java.util.ArrayList;

import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.staff.Staff;
import uk.co.platosys.platax.client.services.StaffService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.shared.boox.GWTEmployee;
import uk.co.platosys.platax.shared.boox.GWTRole;

public class StaffServiceImpl extends Booxlet implements StaffService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ArrayList<GWTEmployee> getEmployees(String enterpriseName,
			GWTRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTEmployee addEmployee(GWTEmployee employee, String enterpriseName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTEmployee getEmployee(String sysname, String enterpriseName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTEmployee hireEmployee(GWTEmployee employee, String enterpriseName) {
		try{
		Enterprise enterprise= getEnterprise(enterpriseName);
		Clerk clerk = getClerk(enterprise);
		Staff staff = Staff.createStaff(enterprise, clerk, employee.getName());
		employee.setSysname(staff.getSysname());
		return employee;
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	public GWTEmployee fireEmployee(GWTEmployee employee, String enterpriseName) {
		// TODO Auto-generated method stub
		return null;
	}
    public GWTEmployee convert(Staff staff){
    	GWTEmployee employee = new GWTEmployee();
    	//employee.setFamilyName(staff.);
    	return employee;
    }
}
