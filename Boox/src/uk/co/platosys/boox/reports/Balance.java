/*
 * This class models a report.
 *
 *
 */

package uk.co.platosys.boox.reports;

import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import uk.co.platosys.boox.core.Account;
import uk.co.platosys.boox.core.Boox;
import uk.co.platosys.boox.core.Clerk;
import uk.co.platosys.boox.core.Enterprise;
import uk.co.platosys.boox.core.Ledger;
import uk.co.platosys.boox.core.exceptions.BooxException;
import uk.co.platosys.boox.core.exceptions.PermissionsException;
import uk.co.platosys.boox.money.Money;

/**
 *
 * @author edward
 */
public abstract class Balance extends Document {
    static Namespace ns = Boox.NAMESPACE;
    public Balance(Enterprise enterprise, Ledger ledger, Clerk clerk)throws BooxException, PermissionsException{
        super();
        Element rootElement=balance(enterprise, ledger, clerk);
        setRootElement(rootElement);
    }
    public static Element balance(Enterprise enterprise, Ledger ledger, Clerk clerk) throws BooxException, PermissionsException {
        if(! clerk.canBalance(enterprise, ledger)){
            throw new PermissionsException("Clerk "+clerk.getName()+" is not authorised to generate this report");
        }else{
            Element element = new Element("Ledger", ns);
            Money balance = ledger.getBalance(enterprise, clerk);
            element.setAttribute("name", ledger.getName());
            element.setAttribute("balance", balance.toPlainString());
            element.setAttribute("currency", balance.getCurrency().getTLA());
            if(clerk.canRead(enterprise, ledger)){
                List<Ledger> ledgers = ledger.getLedgers(enterprise);
                Iterator<Ledger> lit = ledgers.iterator();
                while(lit.hasNext()){
                    Element subLedger= balance(enterprise, lit.next(), clerk);
                    element.addContent(subLedger);
                }
                List<Account> accounts=ledger.getAccounts(enterprise, clerk);
                Iterator<Account> ait = accounts.iterator();
                while(ait.hasNext()){
                    Element accountElement = new Element("Account", ns);
                    Account account = (Account) ait.next();
                    accountElement.setAttribute("name", account.getName());
                    accountElement.setAttribute("fullName", account.getFullName());
                    accountElement.setAttribute("description", account.getDescription());
                    accountElement.setAttribute("balance", account.getBalance(enterprise, clerk).toPlainString());
                    accountElement.setAttribute("currency", account.getCurrency().getTLA());
                    element.addContent(accountElement);
                }
            }
            return element;
              
            }
        }

    


}
