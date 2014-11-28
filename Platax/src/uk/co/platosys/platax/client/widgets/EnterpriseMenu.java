package uk.co.platosys.platax.client.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;

import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.MenuText;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.forms.lists.*;
import uk.co.platosys.platax.shared.boox.*;

/**
 * this long class defines the menu items for the menu bar on the enterprise tab.
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
	static final String SPENDING_MENU="spendingMenu";
	static final String BANKING_MENU="bankingMenu";
	static final String ASSETS_MENU="assetsMenu";
	static final String STAFF_MENU="staffMenu";
	static final String BUDGET_MENU="budgetMenu";
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
			MenuBar productsMenu = new MenuBar(true);incomeMenu.addItem(MenuText.PRODUCTS_LABEL,productsMenu);menuItems.put(PRODUCTS_MENU, productsMenu);
		MenuBar spendingMenu = new MenuBar(true); menuItems.put(SPENDING_MENU,spendingMenu);
		MenuBar bankingMenu = new MenuBar(true); menuItems.put(BANKING_MENU, bankingMenu);
		MenuBar assetsMenu = new MenuBar(true);  menuItems.put(ASSETS_MENU,assetsMenu);
		MenuBar staffMenu = new MenuBar(true);  menuItems.put(STAFF_MENU, staffMenu);
		MenuBar budgetMenu = new MenuBar(true);  menuItems.put(BUDGET_MENU, budgetMenu);
		MenuBar helpMenu = new MenuBar(true); menuItems.put(HELP_MENU, helpMenu);
		
		//so here w
		this.addItem(MenuText.CAPITAL_MENU_NAME, capitalMenu);
		this.addItem(MenuText.INCOME_MENU_NAME, incomeMenu);
		this.addItem(MenuText.SPENDING_MENU_NAME, spendingMenu);
		this.addItem(MenuText.BANKING_MENU_NAME, bankingMenu);
		this.addItem(MenuText.ASSETS_MENU_NAME, assetsMenu);
		this.addItem(MenuText.STAFF_MENU_NAME, staffMenu);
		this.addItem(MenuText.BUDGET_MENU_NAME, budgetMenu);
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
		
		
		/*balanceMenu.setScheduledCommand(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				//TODO add command to show balance sheet
			}
		});*/
		///IncomeMenu and its submenus///
		//Items on the Income Menu
		
		incomeMenu.addItem(MenuText.CASH_LABEL, new Command(){
			@Override
			public void execute() {
				// TODO Open Cash Sales Page Command
			}
		});
		//Items on the Invoice Menu (submenu of the income menu)
		allInvoices.setScheduledCommand(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				// TODO Auto-generated method stub
				
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
		customerMenu.addItem(MenuText.ALL_LABEL,  new Command(){
			@Override
			public void execute() {
				CustomerList ctab = new CustomerList(platax, enterprise, 0);
				platax.addTab(ctab);
			}
		});
		customerMenu.addItem(MenuText.ADD_NEW_LABEL,  new Command(){
			@Override
			public void execute() {
				// TODO Auto-generated method stub
			}
		});
		/////
		//items on the productsmenu, submenu of Income menu
		productsMenu.addItem(MenuText.ALL_LABEL, new Command(){
			@Override
			public void execute() {
				ProductList ctab = new ProductList(platax, enterprise, 0);
				platax.addTab(ctab);
			}
		});
		productsMenu.addItem(MenuText.ADD_NEW_LABEL,  new Command(){
			@Override
			public void execute() {
				// TODO Auto-generated method stub
			}
		});
		////
		///SpendingMenu and its submenus///
			//Items on the Expenditure Menu
			MenuBar billMenu = new MenuBar(true);
			MenuBar supplierMenu = new MenuBar(true);
			MenuBar materialsMenu = new MenuBar(true);
			MenuBar overheadsMenu = new MenuBar(true);
			spendingMenu.addItem(MenuText.BILLS_LABEL, billMenu);
			spendingMenu.addItem(MenuText.CASH_PURCHASES_LABEL, new Command(){
				@Override
				public void execute() {
					// TODO Open Cash purchases Page Command
				}
			});
			spendingMenu.addItem(MenuText.SUPPLIERS_LABEL, customerMenu);
			spendingMenu.addItem(MenuText.MATERIALS_LABEL, materialsMenu);
			spendingMenu.addItem(MenuText.OVERHEADS_LABEL, overheadsMenu);
				//Items on the Bill Menu (submenu of the spending menu)
				billMenu.addItem(MenuText.ALL_LABEL, new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				billMenu.addItem(MenuText.PENDING_LABEL, new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				billMenu.addItem(MenuText.PAID_LABEL, new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				billMenu.addItem(MenuText.OVERDUE_LABEL,  new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				billMenu.addItem(MenuText.ADD_NEW_LABEL, new Command(){
					@Override
					public void execute() {
				  		InvoiceForm itab = new InvoiceForm(platax, enterprise);	
						platax.addTab(itab);
						//int etabindex = platax.getWidgetIndex(itab);
						//ptp.selectTab(etabindex);
					}
				});
			/////
			//items on the SupplierMenu, submenu of Spending menu
				supplierMenu.addItem(MenuText.ALL_LABEL,  new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				supplierMenu.addItem(MenuText.ADD_NEW_LABEL,  new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
			/////
			//items on the materialssmenu, submenu of Spending menu
				materialsMenu.addItem(MenuText.ALL_LABEL, new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				materialsMenu.addItem(MenuText.ADD_NEW_LABEL,  new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
			////
			//Items on the overheadsMenu, submenu of Spending menu
				overheadsMenu.addItem(MenuText.UTILITIES_LABEL, new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				overheadsMenu.addItem(MenuText.RENT_LABEL,  new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				overheadsMenu.addItem(MenuText.INSURANCE_LABEL, new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
				overheadsMenu.addItem(MenuText.GENERAL_LABEL,  new Command(){
					@Override
					public void execute() {
						// TODO Auto-generated method stub
					}
				});
		  //Banking Menu and its submenus
				//banking menu item labels
				bankingMenu.addItem(MenuText.ACCOUNTS_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to list bank accounts
					}
				});
				bankingMenu.addItem(MenuText.ADD_ACCOUNT_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to add a new bank account
					}
				});
		  //Assets Menu and its sumbenus
				assetsMenu.addItem(MenuText.ASSET_REGISTER_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to show the asset register
					}
				});
				assetsMenu.addItem(MenuText.NEW_ASSET_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to add a new asset
					}
				});
	      //Staff Menu and its submenus
				staffMenu.addItem(MenuText.PAYROLL_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to show payroll
					}
				});
				staffMenu.addItem(MenuText.HIRE_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command for hiring staff
					}
				});
				staffMenu.addItem(MenuText.FIRE_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to terminate staff
					}
				});
		 //Budget Menu and its submenus
				budgetMenu.addItem(MenuText.CASHFLOW_PLAN, new Command(){
					@Override
					public void execute() {
						//TODO add command to show online help
					}
				});
				budgetMenu.addItem(MenuText.CURRENT_BUDGET, new Command(){
					@Override
					public void execute() {
						//TODO add command to show about
					}
				});
				budgetMenu.addItem(MenuText.NEXT_BUDGET, new Command(){
					@Override
					public void execute() {
						//TODO add command to show about
					}
				});
		 //Help Menu and its submenus
				//help menu item labels
				helpMenu.addItem(MenuText.ONLINE_HELP, new Command(){
					@Override
					public void execute() {
						//TODO add command to show online help
					}
				});
				helpMenu.addItem(MenuText.ABOUT_LABEL, new Command(){
					@Override
					public void execute() {
						//TODO add command to show about
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
