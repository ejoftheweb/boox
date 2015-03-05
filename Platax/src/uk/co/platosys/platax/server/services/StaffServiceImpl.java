package uk.co.platosys.platax.server.services;

import java.util.ArrayList;

import uk.co.platosys.platax.client.services.StaffService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.shared.boox.GWTEmployee;
import uk.co.platosys.platax.shared.boox.GWTRole;

public class StaffServiceImpl extends Booxlet implements StaffService {

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GWTEmployee fireEmployee(GWTEmployee employee, String enterpriseName) {
		// TODO Auto-generated method stub
		return null;
	}

}
