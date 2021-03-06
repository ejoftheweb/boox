package uk.co.platosys.platax.client.constants;

import uk.co.platosys.platax.client.widgets.html.StringHTML;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * This class contains constants for the menu labels.
 * At present they are hard-coded but it is relatively straightforward to reconfigure this
 * class to read directly from a local-specific file.
 * 
 * Note. We probably should implement this from a module file. In fact, let's do it. 
 * 
 * @author edward
 *
 */

public class MenuText {

//top menu	
public static final SafeHtml CAPITAL_MENU_NAME= new StringHTML("Capital");	
public static final SafeHtml INCOME_MENU_NAME= new StringHTML("Income");
public static final SafeHtml SPENDING_MENU_NAME= new StringHTML("Expenditure");
public static final SafeHtml BANKING_MENU_NAME= new StringHTML("Money");
public static final SafeHtml ASSETS_MENU_NAME= new StringHTML("Assets");
public static final SafeHtml BUDGET_MENU_NAME= new StringHTML("Budget");
public static final SafeHtml STAFF_MENU_NAME= new StringHTML("Staff");
public static final SafeHtml SETTINGS_MENU_NAME= new StringHTML("Admin");
public static final SafeHtml HELP_MENU_NAME= new StringHTML("Help");

//capital menu item labels
public static final SafeHtml EQUITY_LABEL= new StringHTML("Equity");
public static final SafeHtml BONDS_LABEL= new StringHTML("Loans");
public static final SafeHtml BALANCE_LABEL= new StringHTML("Balance Sheet");
public static final SafeHtml ISSUE_EQUITY_LABEL= new StringHTML("Issue Shares");
public static final SafeHtml SHARE_REGISTER_LABEL= new StringHTML("Share Register");
public static final SafeHtml DIRECTORS_LOANS_LABEL= new StringHTML("Directors Loans");


//income menu item labels
public static final SafeHtml INVOICES_LABEL= new StringHTML("Invoices");
public static final SafeHtml CASH_LABEL= new StringHTML("Cash Sales");
public static final SafeHtml CUSTOMERS_LABEL= new StringHTML("Customers");
public static final SafeHtml PRODUCTS_LABEL= new StringHTML("Products");

public static final SafeHtml CASHUP= new StringHTML("Cash Up");
public static final SafeHtml NEW_REGISTER= new StringHTML("New Cash Register");
public static final SafeHtml NEW_CASHIER= new StringHTML("New Cashier");


//spending menu item labels
public static final SafeHtml BILLS_LABEL= new StringHTML("Bills");
public static final SafeHtml CASH_PURCHASES_LABEL= new StringHTML("Cash Purchases");
public static final SafeHtml SUPPLIERS_LABEL= new StringHTML("Suppliers");
public static final SafeHtml MATERIALS_LABEL= new StringHTML("Materials");
public static final SafeHtml OVERHEADS_LABEL= new StringHTML("Overheads");
//overhead sub-menu item labels
public static final SafeHtml UTILITIES_LABEL= new StringHTML("Utilities");
public static final SafeHtml RENT_LABEL= new StringHTML("Rent and Rates");
public static final SafeHtml INSURANCE_LABEL= new StringHTML("Insurance");
public static final SafeHtml GENERAL_LABEL= new StringHTML("Other");

//money menu item labels
public static final SafeHtml PAYMENTS_LABEL=new StringHTML("Payments");
public static final SafeHtml PAYMENTSIN_LABEL=new StringHTML("Payments In");
public static final SafeHtml PAYMENTSOUT_LABEL=new StringHTML("Payments Out");

public static final SafeHtml BANKACCOUNTS_LABEL= new StringHTML("Bank Accounts");
public static final SafeHtml ADD_BANKACCOUNT_LABEL= new StringHTML("Add Bank Account...");
public static final SafeHtml PETTYCASH_LABEL= new StringHTML("Petty Cash");
public static final SafeHtml ADD_PETTYCASH_LABEL= new StringHTML("Add Petty Cash Account...");
public static final SafeHtml CARD_LABEL= new StringHTML("Charge Cards");
public static final SafeHtml ADD_CARD_LABEL= new StringHTML("Add Charge Card Account...");
public static final SafeHtml MERCHANT_LABEL= new StringHTML("Cards Accepted");
public static final SafeHtml ADD_MERCHANT_LABEL= new StringHTML("Add Card Merchant Account...");

//assets menu item labels
public static final SafeHtml ASSET_REGISTER_LABEL= new StringHTML("List");
public static final SafeHtml NEW_ASSET_LABEL= new StringHTML("New asset...");

//staff menu item labels
public static final SafeHtml PAYROLL_LABEL= new StringHTML("Payroll");
public static final SafeHtml HIRE_LABEL= new StringHTML("Hire");
public static final SafeHtml FIRE_LABEL= new StringHTML("Fire");
public static final SafeHtml TIMESHEETS_LABEL=new StringHTML("Timesheets");
//budget menu item labels
public static final SafeHtml CASHFLOW_PLAN= new StringHTML("Cash Flow");
public static final SafeHtml CURRENT_BUDGET= new StringHTML("Current");
public static final SafeHtml NEXT_BUDGET= new StringHTML("Next");
//settings menu item labels
public static final SafeHtml ADD_MODULES= new StringHTML("Add Modules");
public static final SafeHtml CONFIGURE = new StringHTML("Configure");
public static final SafeHtml BACK_UP = new StringHTML("Back_Up");

//help menu item labels
public static final SafeHtml ONLINE_HELP= new StringHTML("Online");
public static final SafeHtml ABOUT_LABEL= new StringHTML("About");
//sub-menu item labels
public static final SafeHtml PENDING_LABEL= new StringHTML("pending");
public static final SafeHtml OVERDUE_LABEL= new StringHTML("overdue");
public static final SafeHtml PAID_LABEL= new StringHTML("paid");
public static final SafeHtml ALL_LABEL= new StringHTML("all");
public static final SafeHtml ADD_NEW_LABEL= new StringHTML("New...");

}
