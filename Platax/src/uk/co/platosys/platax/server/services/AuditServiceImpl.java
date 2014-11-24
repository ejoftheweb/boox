package uk.co.platosys.platax.server.services;

import java.util.Date;

import uk.co.platosys.boox.core.AuditElement;
import uk.co.platosys.boox.core.AuditLine;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.platax.server.core.PlataxServer;

import uk.co.platosys.platax.shared.boox.GWTAuditElement;
import uk.co.platosys.platax.shared.boox.GWTAuditLine;
import uk.co.platosys.util.ISODate;
import uk.co.platosys.xservlets.Xservlet;
public class AuditServiceImpl extends Xservlet {

	public AuditServiceImpl() {
		 
	}
   public static GWTAuditElement convert(AuditElement auditLine, Enterprise enterprise, Clerk clerk) throws PermissionsException{
	  GWTAuditLine gAuditLine=new GWTAuditLine();
	  gAuditLine.setBalance(PlataxServer.convert(auditLine.getBalance(enterprise, clerk)));
	  gAuditLine.setName(auditLine.getName());
	  gAuditLine.setDate( new Date(auditLine.getDate().getTime()));
	  return gAuditLine;
   }
}
