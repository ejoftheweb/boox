package uk.co.platosys.boox.stake;

import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.exceptions.BooxException;

public class LimitedCompany extends Enterprise {
	private ShareRegister shareRegister;
	
  private LimitedCompany(String enterpriseID) throws BooxException{
	  super(enterpriseID);
  }

public void setShareRegister(ShareRegister shareRegister) {
	this.shareRegister = shareRegister;
}

public ShareRegister getShareRegister() {
	return shareRegister;
}
}
