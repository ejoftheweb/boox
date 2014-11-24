package uk.co.platosys.boox.stake;

import java.util.HashMap;


public class ShareRegister extends HashMap<Shareholder, Double> {
	private LimitedCompany company;
	

  public ShareRegister(LimitedCompany company){
	  this.company=company;
  }
}
