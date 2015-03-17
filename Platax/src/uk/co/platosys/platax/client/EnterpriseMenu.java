package uk.co.platosys.platax.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;

import uk.co.platosys.platax.client.constants.MenuText;
import uk.co.platosys.platax.client.forms.ModulesForm;
import uk.co.platosys.platax.client.forms.CashRegisterForm;
import uk.co.platosys.platax.client.forms.CashierForm;
import uk.co.platosys.platax.client.forms.CustomerForm;
import uk.co.platosys.platax.client.forms.ProductForm;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.forms.lists.*;
import uk.co.platosys.platax.client.forms.tasks.DirectorsLoan;
import uk.co.platosys.platax.client.forms.tasks.HireStaff;
import uk.co.platosys.platax.client.forms.tasks.IssueEquity;
import uk.co.platosys.platax.client.forms.tasks.CashUp;
import uk.co.platosys.platax.shared.boox.*;

/**
 * this long class defines the menu items for the menu bar on the enterprise tab.
 * 
 * It is currently a static menu listing, we need to do some work to make it dynamic.
 * 
 * @author edward
 *
 */
public class EnterpriseMenu extends MenuBar {
	//the menu displays items differently highlighted according to the urgency of the task
	static final String OK_STATUS="Ok";
	static final String PRIORITY_STATUS="Priority";
	static final String DUE_STATUS="Due";
	static final String OVERDUE_STATUS="Overdue";
	static final String DISABLED_STATUS="Disabled";
	
	final GWTEnterprise gwtEnterprise;
	//We define a String variable to refer programmatically to each menu item. There are lots!
	static final String CAPITAL_MENU="capitalMenu"; 
		static final String BALANCE_SHEET="directorsLoan";
		static final String EQUITY_MENU="equityMenu";
			static final String ISSUE_EQUITY="issueEquity";
			static final String SHARE_REGISTER="showShareRegister";
		static final String BONDS_MENU="bondsMenu";
			static final String DIRECTORS_LOANS="directorsLoans";		
	static final String INCOME_MENU="incomeMenu";
		static final String INVOICE_MENU="invoiceMenu";
			static final String ALL_INVOICES="allInvoices";
			static final String PENDING_INVOICES="pendingInvoices";
			static final String PAID_INVOICES="paidInvoices";
			static final String OVERDUE_INVOICES="overdueInvoices";
			static final String NEW_INVOICE="newInvoice";
		static final String CUSTOMER_MENU="customersMenu";
			static final String ALL_CUSTOMERS="allCustomers";
			static final String NEW_CUSTOMER="newcustomer";
		static final String PRODUCTS_MENU="productsMenu";
			static final String ALL_PRODUCTS="allProducts";
			static final String NEW_PRODUCT="newProduct";
		static final String CASHUP_MENU="cashupMenu";
			static final String REGISTER_CASH="registerCash";
			static final String NEW_REGISTER="newregister";
			static final String NEW_CASHIER="newcashier";
		static final String SPENDING_MENU="spendingMenu";
			static final String BILLS="bills";
			static final String CASH_PURCHASES="cashPurchases";
			static final String SUPPLIERS="suppliers";
			static final String MATERIALS="materials";
			static final String OVERHEADS_MENU="overheadsMenu";
				static final String UTILITIES="utilities";
				static final String RENT="rent";
				static final String INSURANCE="insurance";
				static final String GENERAL="other";
		
	static final String BANKING_MENU="bankingMenu";
		static final String PAYMENTS="payments";
			static final String PAYMENTS_IN="paymentsIn";
			static final String PAYMENTS_OUT="paymentsOut";
	static final String BANK_ACCOUNTS="bankAccounts";
		static final String PETTY_CASH="pettyCash";
		static final String CARD_ACCOUNTS="cardAccounts";
		static final String MERCHANT_ACCOUNTS="merchantAccounts";
	static final String ASSETS_MENU="assetsMenu";
	static final String STAFF_MENU="staffMenu";
		static final String HIRE_STAFF="hireStaff";
		static final String FIRE_STAFF="fireStaff";
		static final String PAYROLL="payroll";
		static final String TIMESHEETS="timesheets";
	static final String BUDGET_MENU="budgetMenu";
		static final String CASHFLOW_PLAN="cashFlowPlan";
	static final String SETTINGS_MENU="settingsMenu";
		static final String ADD_MODULES="addModules";
		static final String CONFIGURE="configure";
		static final String BACKUP="backup";
	static final String HELP_MENU="helpMenu";
	
	
	
	//the hashmap lets is get to each item by its String key
    Map<String, UIObject> menuItems=new HashMap<String, UIObject>();	

	public EnterpriseMenu(final GWTEnterprise enterprise, final Platax platax){
		super();
		this.gwtEnterprise=enterprise;
		//this little lot defines menu items. Break with all good coding style guides for readability.
		MenuBar capitalMenu = new MenuBar(true); menuItems.put(CAPITAL_MENU, capitalMenu);
		MenuItem balanceSheet = new MenuItem(MenuText.BALANCE_LABEL);capitalMenu.addItem(balanceSheet);menuItems.put(BALANCE_SHEET, balanceSheet);
			MenuBar equityMenu = new MenuBar(true);capitalMenu.addItem(MenuText.EQUITY_LABEL, equityMenu);menuItems.put(EQUITY_MENU, equityMenu);
				MenuItem issueEquity = new MenuItem(MenuText.ISSUE_EQUITY_LABEL);equityMenu.addItem(issueEquity);menuItems.put(ISSUE_EQUITY, issueEquity);
				MenuItem showShareRegister=new MenuItem(MenuText.SHARE_REGISTER_LABEL);equityMenu.addItem(showShareRegister);menuItems.put(SHARE_REGISTER, showShareRegister);
			MenuBar bondsMenu = new MenuBar(true);capitalMenu.addItem(MenuText.BONDS_LABEL,bondsMenu);menuItems.put(BONDS_MENU, bondsMenu);
				MenuItem directorsLoans = new MenuItem(MenuText.DIRECTORS_LOANS_LABEL);bondsMenu.addItem(directorsLoans);menuItems.put(DIRECTORS_LOANS, directorsLoans);
			
		MenuBar incomeMenu = new MenuBar(true);	menuItems.put(INCOME_MENU, incomeMenu);
			MenuBar invoiceMenu = new MenuBar(true);incomeMenu.addItem(MenuText.INVOICES_LABEL, invoiceMenu);menuItems.put(INVOICE_MENU, invoiceMenu);
				MenuItem allInvoices = new MenuItem(MenuText.ALL_LABEL);invoiceMenu.addItem(allInvoices);menuItems.put(ALL_INVOICES, allInvoices);
				MenuItem pendingInvoices=new MenuItem(MenuText.PENDING_LABEL);invoiceMenu.addItem(pendingInvoices);menuItems.put(PENDING_INVOICES, pendingInvoices);
				MenuItem paidInvoices=new MenuItem(MenuText.PAID_LABEL);invoiceMenu.addItem(paidInvoices);menuItems.put(PAID_INVOICES, paidInvoices);
				MenuItem overdueInvoices=new MenuItem(MenuText.OVERDUE_LABEL);invoiceMenu.addItem(overdueInvoices);menuItems.put(OVERDUE_INVOICES, overdueInvoices);
				MenuItem newInvoice=new MenuItem(MenuText.ADD_NEW_LABEL);invoiceMenu.addItem(newInvoice);menuItems.put(NEW_INVOICE, newInvoice);
			MenuBar customerMenu = new MenuBar(true);incomeMenu.addItem(MenuText.CUSTOMERS_LABEL,customerMenu);menuItems.put(CUSTOMER_MENU,customerMenu);
				MenuItem allCustomers = new MenuItem(MenuText.ALL_LABEL);customerMenu.addItem(allCustomers);menuItems.put(ALL_CUSTOMERS, allCustomers);
				MenuItem newCustomer=new MenuItem(MenuText.ADD_NEW_LABEL);customerMenu.addItem(newCustomer);menuItems.put(NEW_CUSTOMER, newCustomer);
		    MenuBar productsMenu = new MenuBar(true);incomeMenu.addItem(MenuText.PRODUCTS_LABEL,productsMenu);menuItems.put(PRODUCTS_MENU, productsMenu);
		   		MenuItem allProducts = new MenuItem(MenuText.ALL_LABEL);productsMenu.addItem(allProducts);menuItems.put(ALL_PRODUCTS, allProducts);
		   		MenuItem newProduct=new MenuItem(MenuText.ADD_NEW_LABEL);productsMenu.addItem(newProduct);menuItems.put(NEW_CUSTOMER, newProduct);
			MenuBar cashupMenu=new MenuBar(true);incomeMenu.addItem(MenuText.CASH_LABEL, cashupMenu);menuItems.put(CASHUP_MENU, cashupMenu);
				MenuItem registerCash = new MenuItem(MenuText.CASHUP);cashupMenu.addItem(registerCash);menuItems.put(REGISTER_CASH, registerCash);
				MenuItem newRegister = new MenuItem(MenuText.NEW_REGISTER);cashupMenu.addItem(newRegister);menuItems.put(NEW_REGISTER, newRegister);
				MenuItem newCashier=new MenuItem(MenuText.NEW_CASHIER);cashupMenu.addItem(newCashier);menuItems.put(NEW_CUSTOMER, newCashier);
		MenuBar spendingMenu = new MenuBar(true); menuItems.put(SPENDING_MENU,spendingMenu);
				//MenuItem {}  = new MenuItem(MenuText.{});spendingMenu.addItem({});menuItems.put({}, {});
			MenuItem bills  = new MenuItem(MenuText.BILLS_LABEL);spendingMenu.addItem(bills);menuItems.put(BILLS, bills);
			MenuItem cashPurchases  = new MenuItem(MenuText.CASH_PURCHASES_LABEL);spendingMenu.addItem(cashPurchases);menuItems.put(CASH_PURCHASES,cashPurchases);
			MenuItem suppliers  = new MenuItem(MenuText.SUPPLIERS_LABEL);spendingMenu.addItem(suppliers);menuItems.put(SUPPLIERS, suppliers);
			MenuItem materials  = new MenuItem(MenuText.MATERIALS_LABEL);spendingMenu.addItem(materials);menuItems.put(MATERIALS, materials);
				MenuBar overheadsMenu = new MenuBar(true);spendingMenu.addItem(MenuText.OVERHEADS_LABEL, overheadsMenu);menuItems.put(OVERHEADS_MENU, overheadsMenu);
				//MenuItem {}  = new MenuItem(MenuText.{});overheadsMenu.addItem({});menuItems.put({}, {});
				MenuItem utilities  = new MenuItem(MenuText.UTILITIES_LABEL);overheadsMenu.addItem(utilities);menuItems.put(UTILITIES, utilities);
				MenuItem rent  = new MenuItem(MenuText.RENT_LABEL);overheadsMenu.addItem(rent);menuItems.put(RENT, rent);
				MenuItem insurance  = new MenuItem(MenuText.INSURANCE_LABEL);overheadsMenu.addItem(insurance);menuItems.put(INSURANCE, insurance);
				MenuItem general = new MenuItem(MenuText.GENERAL_LABEL);overheadsMenu.addItem(general);menuItems.put(GENERAL, general);
				
		MenuBar bankingMenu = new MenuBar(true); menuItems.put(BANKING_MENU, bankingMenu);
			MenuBar paymentMenu = new MenuBar(true); bankingMenu.addItem(MenuText.PAYMENTS_LABEL, paymentMenu);menuItems.put(PAYMENTS, paymentMenu);
			MenuItem paymentsIn = new MenuItem(MenuText.PAYMENTSIN_LABEL);paymentMenu.addItem(paymentsIn);menuItems.put(PAYMENTS_IN, paymentsIn);
			MenuItem paymentsOut = new MenuItem(MenuText.PAYMENTSOUT_LABEL);paymentMenu.addItem(paymentsOut);menuItems.put(PAYMENTS_OUT, paymentsOut);
			
			MenuItem bankAccount = new MenuItem(MenuText.BANKACCOUNTS_LABEL);bankingMenu.addItem(bankAccount);menuItems.put(BANK_ACCOUNTS, bankAccount);
			MenuItem pettyCash=new MenuItem(MenuText.PETTYCASH_LABEL);bankingMenu.addItem(pettyCash);menuItems.put(PETTY_CASH, pettyCash);
			MenuItem chargeCard=new MenuItem(MenuText.CARD_LABEL);bankingMenu.addItem(chargeCard);menuItems.put(CARD_ACCOUNTS, chargeCard);
			MenuItem merchantCard=new MenuItem(MenuText.MERCHANT_LABEL);bankingMenu.addItem(merchantCard);menuItems.put(MERCHANT_ACCOUNTS, merchantCard);
		
		MenuBar assetsMenu = new MenuBar(true);  menuItems.put(ASSETS_MENU,assetsMenu);
		MenuBar staffMenu = new MenuBar(true);  menuItems.put(STAFF_MENU, staffMenu);
			MenuItem hireStaff = new MenuItem(MenuText.HIRE_LABEL);staffMenu.addItem(hireStaff);menuItems.put(HIRE_STAFF, hireStaff);
			MenuItem fireStaff = new MenuItem(MenuText.FIRE_LABEL);staffMenu.addItem(fireStaff);menuItems.put(FIRE_STAFF, fireStaff);
			MenuItem payroll = new MenuItem(MenuText.PAYROLL_LABEL);staffMenu.addItem(payroll);menuItems.put(PAYROLL, payroll);
			MenuItem timesheets = new MenuItem(MenuText.TIMESHEETS_LABEL);staffMenu.addItem(timesheets);menuItems.put(TIMESHEETS, timesheets);
			
		MenuBar budgetMenu = new MenuBar(true);  menuItems.put(BUDGET_MENU, budgetMenu);
			MenuItem cashFlowPlan = new MenuItem(MenuText.HIRE_LABEL);budgetMenu.addItem(cashFlowPlan);menuItems.put(HIRE_STAFF, hireStaff);
		
		MenuBar settingsMenu = new MenuBar(true); menuItems.put(SETTINGS_MENU, settingsMenu);
			MenuItem addModules=new MenuItem(MenuText.ADD_MODULES);settingsMenu.addItem(addModules);menuItems.put(ADD_MODULES, addModules);
			MenuItem configure=new MenuItem(MenuText.CONFIGURE);settingsMenu.addItem(configure);menuItems.put(CONFIGURE, configure);
			MenuItem backup  = new MenuItem(MenuText.BACK_UP);settingsMenu.addItem(backup);menuItems.put(BACKUP, backup);
			
		MenuBar helpMenu = new MenuBar(true); menuItems.put(HELP_MENU, helpMenu);
		
		//so here we add all the top-level menus.
		this.addItem(MenuText.CAPITAL_MENU_NAME, capitalMenu);
		this.addItem(MenuText.INCOME_MENU_NAME, incomeMenu);
		this.addItem(MenuText.SPENDING_MENU_NAME, spendingMenu);
		this.addItem(MenuText.BANKING_MENU_NAME, bankingMenu);
		this.addItem(MenuText.ASSETS_MENU_NAME, assetsMenu);
		this.addItem(MenuText.STAFF_MENU_NAME, staffMenu);
		this.addItem(MenuText.BUDGET_MENU_NAME, budgetMenu);
		this.addItem(MenuText.SETTINGS_MENU_NAME, settingsMenu);
		this.addItem(MenuText.HELP_MENU_NAME, helpMenu);
		
		//now we add the commands
		balanceSheet.setScheduledCommand(Commands.BALANCE_SHEET);
		issueEquity.setScheduledCommand(Commands.ISSUE_EQUITY);
		showShareRegister.setScheduledCommand(Commands.SHARE_REGISTER);
		directorsLoans.setScheduledCommand(Commands.DIRECTORS_LOANS);
		registerCash.setScheduledCommand(Commands.CASH_UP);
		newRegister.setScheduledCommand(Commands.NEW_REGISTER);
		newCashier.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				CashierForm scr = new CashierForm();
				platax.addTab(scr);
			}
		});
		//Items on the Invoice Menu (submenu of the income menu)
		allInvoices.setScheduledCommand(Commands.INVOICES_LIST_ALL);
		pendingInvoices.setScheduledCommand(Commands.INVOICES_LIST_PENDING);
		paidInvoices.setScheduledCommand(Commands.INVOICES_LIST_PAID);
		overdueInvoices.setScheduledCommand(Commands.INVOICES_LIST_DUE);
		newInvoice.setScheduledCommand(Commands.NEW_INVOICE);
		/////
		//items on the CustomerMenu, submenu of Income menu
		allCustomers.setScheduledCommand(Commands.CUSTOMERS_LIST_ALL);
		newCustomer.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//Window.alert("new invoice menu item selected");
		  		CustomerForm itab = new CustomerForm(platax, enterprise);	
				platax.addTab(itab);
		}});
		/////
		//items on the productsmenu, submenu of Income menu
		allProducts.setScheduledCommand(Commands.PRODUCTS_LIST_ALL);
		newProduct.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//Window.alert("new invoice menu item selected");
		  		ProductForm itab = new ProductForm(platax, enterprise);	
				platax.addTab(itab);
			}
		});
		bills.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		cashPurchases.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		suppliers.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		materials.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		utilities.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		rent.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		insurance.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		bankAccount.setScheduledCommand(Commands.BANK_ACCOUNTS);
		paymentsIn.setScheduledCommand(Commands.PAYMENTS_IN);
		paymentsOut.setScheduledCommand(Commands.PAYMENTS_OUT);
		
		general.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});
		hireStaff.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				HireStaff hs = new HireStaff(platax);
				platax.addTab(hs);
			}
		});		
		addModules.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//Window.alert("adding modules form");
				ModulesForm mf = new ModulesForm(platax, enterprise);
				platax.addTab(mf);
			}
		});
		configure.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		});		
		backup.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO
				Window.alert("Feature not yet implemented");
			}
		}); 		 
	}
	public void setItemStatus(String item, String status){
		 UIObject mitem = menuItems.get(item);
		 if(status==OK_STATUS){
			 mitem.setStyleName("px_ok_menu");
			 
		 }else if(status==PRIORITY_STATUS){
			 mitem.setStyleName("px_priority_menu");
		 }else{
			 //do nothing, it means nothing.
		 }
	} 
	
	public void addModule(GWTModule module){
		//TODO
		//this method adds a module to the menu. We have to:
		//1: Add a menu bar to the right part of the top menu
		//2: Add one or more menu items to the menu bar
		//3: Add the menu items to the menuItems map
		//4: Add a ScheduledCommand to each menuItem
		//5: Set the status of the menuItem according to the task priority
		
	}
	
}
