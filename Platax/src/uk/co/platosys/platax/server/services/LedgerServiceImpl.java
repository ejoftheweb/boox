/*
 * This code is copyright Platosys, to the extent that copyright subsists.
 * Use, modification and distribution are subject to the consent of Platosys.
Please see the documentation that accompanied the distribution of this code for the licence terms
applicable to you. If you cannot find the licence documentation, or you would like to discuss alternative
terms and conditions, please contact Platosys at licensing@platosys.co.uk.
 */

package uk.co.platosys.platax.server.services;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.AuditLine;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Money;
import uk.co.platosys.platax.client.services.LedgerService;
import uk.co.platosys.platax.server.core.Booxlet;
import uk.co.platosys.platax.server.core.PXConstants;
import uk.co.platosys.platax.server.core.PlataxUser;
import uk.co.platosys.platax.shared.boox.GWTAuditLine;
import uk.co.platosys.platax.shared.boox.GWTEnterprise;
import uk.co.platosys.platax.shared.boox.GWTLedger;
import uk.co.platosys.platax.shared.exceptions.PlataxException;
import uk.co.platosys.pws.values.GWTMoney;
import uk.co.platosys.xservlets.Xservlet;

/**
 *
 * @author edward
 */
public class LedgerServiceImpl extends Booxlet implements LedgerService {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 6670653590480688155L;

	/** 
    * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
    * @param request servlet request
    * @param response servlet response
    */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out=null;
        try {
        	out = response.getWriter();
            /* TODO output your page here
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LedgerServiceImpl</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LedgerServiceImpl at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
            */
        }catch(Exception x){
        } finally { 
            out.close();
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
    * Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       //response.SC_METHOD_NOT_ALLOWED;
    } 

    /** 
    * Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
   
    /** 
    * Returns a short description of the servlet.
    */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

	@Override
	public ArrayList<GWTAuditLine> audit(String enterpriseID, String ledgername) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		try {
			Clerk clerk = (Clerk) session.getAttribute("clerk");
			Enterprise enterprise = Enterprise.getEnterprise(enterpriseID);
			Ledger ledger = Ledger.getLedger(enterprise, ledgername);
			ArrayList<AuditLine> auditList = ledger.lineAudit(enterprise, clerk);
			ArrayList<GWTAuditLine> gwtAuditList= new ArrayList<GWTAuditLine>();
			return gwtAuditList;
		} catch (BooxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (PermissionsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public GWTMoney readBalance(String enterpriseID, String ledgername) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		try {
			Clerk clerk = (Clerk) session.getAttribute("clerk");
			Enterprise enterprise = Enterprise.getEnterprise(enterpriseID);
			Ledger ledger = Ledger.getLedger(enterprise, ledgername);
			Money balance = ledger.getBalance(enterprise, clerk);
			GWTMoney gwtBalance= new GWTMoney(balance.getCurrency().getTLA(), balance.getAmount().doubleValue());
			return gwtBalance;
		} catch (BooxException e) {
			logger.log("Ledger service problem reading the balance", e);
			return null;
		} catch (PermissionsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public GWTLedger getGeneralLedger(GWTEnterprise gEnterprise) {
		 logger.log("LSI getting genledger for ent"+gEnterprise.getName()+" id="+gEnterprise.getEnterpriseID());
		 return getLedger(gEnterprise, gEnterprise.getName());
	}

	@Override
	public GWTLedger getLedger(GWTEnterprise gEnterprise, String ledgername) {
		try{
			PlataxUser pxuser =  (PlataxUser) getSession().getAttribute(PXConstants.USER);
			Enterprise enterprise = pxuser.getEnterprise(gEnterprise.getEnterpriseID());
			logger.log("LSIgl: enterprise is "+enterprise.getName());
		 	Clerk clerk = pxuser.getClerk(enterprise);
		 	logger.log("LSIgl: ledgername is"+ledgername);
		 	if (ledgername.equals(enterprise.getName())){
		 		ledgername=Ledger.ROOT_LEDGER_NAME;
		 		logger.log("LSIgl: ledgername changed to"+ledgername);
			}else if (ledgername.equals((enterprise.getName()+Ledger.DELIMITER+enterprise.getDefaultCurrency().getTLA()))){
		 		ledgername=Ledger.ROOT_LEDGER_NAME+Ledger.DELIMITER+Ledger.DEFAULT_CURRENCY_LEDGER_NAME;
		 		logger.log("LSIgl: ledgername changed to"+ledgername);
		 	}
		 	Ledger ledger = Ledger.getLedger(enterprise, ledgername);
		 	return convert(ledger, enterprise, clerk);
		}catch(Exception x){
			logger.log("LSL getLedger threw an excpeiton", x);
			return null;
			
			
		}
	}
   protected static GWTLedger convert(Ledger ledger, Enterprise enterprise, Clerk clerk) throws Exception{
	   GWTLedger gLedger = new GWTLedger();
	   gLedger.setName(fixName(enterprise,ledger.getName()));
	   if (ledger.hasParent(enterprise)){
		   gLedger.setParentName(ledger.getParentName());
	   }
	   List<Ledger> ledgers = ledger.getLedgers(enterprise);
	   Iterator<Ledger> lit = ledgers.iterator();
	   while(lit.hasNext()){
		   gLedger.addLedger(convert(lit.next(), enterprise, clerk));
	   }
	   List<Account> accounts = ledger.getAccounts(enterprise, clerk);
	   Iterator<Account> ait= accounts.iterator();
	   while(ait.hasNext()){
		   gLedger.addAccount(AccountServiceImpl.convert (ait.next(), enterprise, clerk));
	   }
	   //logger.log("LSI: ledger "+gLedger.getName()+ " has balance "+gLedger.getBalance().toSignedString());
	return  gLedger;
	   
   }
   
   /**
    * In Boox, ledger names are Root:XBX: but for public consumption it's better that they should be more readable, that is
    * enterpriseName:CurrencyTLA:
    * @param enterprise
    * @param ledgerName
    * @return
    */
   public static String fixName(Enterprise enterprise, String ledgerName) {
		String[] names = ledgerName.split(Ledger.DELIMITER);
		if (names[0].equals(Ledger.ROOT_LEDGER_NAME)){names[0]=enterprise.getName();}
		if(names.length>1){
			if (names[1].equals(Ledger.DEFAULT_CURRENCY_LEDGER_NAME)){names[1]=enterprise.getDefaultCurrency().getTLA();}
		}
		ledgerName="";
		for (String name : names) {
			ledgerName=ledgerName+name+Ledger.DELIMITER;
		}
		ledgerName=ledgerName.substring(0,ledgerName.length()-1);//chop off the last DELIMITER
		return ledgerName;
	}
}
