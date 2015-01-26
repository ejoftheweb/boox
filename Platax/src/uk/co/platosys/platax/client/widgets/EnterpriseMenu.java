package uk.co.platosys.platax.client.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.LabelText;
import uk.co.platosys.platax.client.constants.MenuText;
import uk.co.platosys.platax.client.forms.ModulesForm;
import uk.co.platosys.platax.client.forms.CashRegisterForm;
import uk.co.platosys.platax.client.forms.CashierForm;
import uk.co.platosys.platax.client.forms.CustomerForm;
import uk.co.platosys.platax.client.forms.ProductForm;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.forms.lists.*;
import uk.co.platosys.platax.client.forms.tasks.HireStaff;
import uk.co.platosys.platax.client.forms.tasks.PettyCash;
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
		static final String EQUITY_MENU="equityMenu";
		static final String BONDS_MENU="bondsMenu";
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
	static final String BANKING_MENU="bankingMenu";
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
	static final String HELP_MENU="helpMenu";
	
	
	
	//the hashmap lets is get to each item by its String key
    Map<String, UIObject> menuItems=new HashMap<String, UIObject>();	

	public EnterpriseMenu(final GWTEnterprise enterprise, final Platax platax){
		super();
		this.gwtEnterprise=enterprise;
		//this little lot defines menu items. Break with all good coding style guides for readability.
		MenuBar capitalMenu = new MenuBar(true); menuItems.put(CAPITAL_MENU, capitalMenu);
			MenuItem equityMenu = new MenuItem(MenuText.EQUITY_LABEL);capitalMenu.addItem(equityMenu);menuItems.put(EQUITY_MENU, equityMenu);
			MenuItem bondsMenu = new MenuItem(MenuText.BONDS_LABEL);capitalMenu.addItem(bondsMenu);menuItems.put(BONDS_MENU, bondsMenu);
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
		MenuBar bankingMenu = new MenuBar(true); menuItems.put(BANKING_MENU, bankingMenu);
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
		equityMenu.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				
			}
		});
		bondsMenu.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO add command to show loans
			}
		});
		
		
		
		registerCash.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				CashUp scr = new CashUp(platax, enterprise);
				platax.addTab(scr);
			}
		});
		newRegister.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				CashRegisterForm scr = new CashRegisterForm(platax, enterprise);
				platax.addTab(scr);
			}
		});
		newCashier.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				CashierForm scr = new CashierForm(platax, enterprise);
				platax.addTab(scr);
			}
		});
		//Items on the Invoice Menu (submenu of the income menu)
		allInvoices.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax, enterprise, GWTInvoice.SELECTION_ALL);	
				platax.addTab(itab);
			}
		});
		pendingInvoices.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax, enterprise, GWTInvoice.SELECTION_PENDING);	
				platax.addTab(itab);
			}
		});
		paidInvoices.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax, enterprise, GWTInvoice.SELECTION_PAID);	
				platax.addTab(itab);
			}
		});
		overdueInvoices.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax, enterprise, GWTInvoice.SELECTION_OVERDUE);	
				platax.addTab(itab);
			}
		});
		newInvoice.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
				public void execute() {
				InvoiceForm itab = new InvoiceForm(platax, enterprise);	
				platax.addTab(itab);
			}
		});
		/////
		//items on the CustomerMenu, submenu of Income menu
		allCustomers.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				CustomerList ctab = new CustomerList(platax, enterprise, 0);
				platax.addTab(ctab);
			}
		});
		newCustomer.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//Window.alert("new invoice menu item selected");
		  		CustomerForm itab = new CustomerForm(platax, enterprise);	
				platax.addTab(itab);
		}});
		/////
		//items on the productsmenu, submenu of Income menu
		allProducts.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				ProductList ctab = new ProductList(platax, enterprise, 0);
				platax.addTab(ctab);
			}
		});
		newProduct.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//Window.alert("new invoice menu item selected");
		  		ProductForm itab = new ProductForm(platax, enterprise);	
				platax.addTab(itab);
			}
		});
		hireStaff.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				// TODO Auto-generated method stub
				
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
	
}
