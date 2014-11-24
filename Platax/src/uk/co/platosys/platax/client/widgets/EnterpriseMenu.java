package uk.co.platosys.platax.client.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.UIObject;
import uk.co.platosys.platax.client.Platax;
import uk.co.platosys.platax.client.constants.MenuText;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.forms.lists.*;
import uk.co.platosys.platax.shared.boox.*;

public class EnterpriseMenu extends MenuBar {
	//the menu displays items differently highlighted according to urgency of the task
	static final String OK_STATUS="Ok";
	static final String PRIORITY_STATUS="Priority";
	static final String DUE_STATUS="Due";
	static final String OVERDUE_STATUS="Overdue";
	static final String DISABLED_STATUS="Disabled";
	
	final GWTEnterprise gwtEnterprise;
	static final String CAPITAL_MENU="capitalMenu"; 
    Map<String, UIObject> menuItems=new HashMap<String, UIObject>();	

	public EnterpriseMenu(final GWTEnterprise enterprise, final Platax platax){
		super();
		this.gwtEnterprise=enterprise;
		MenuBar capitalMenu = new MenuBar(true);
		menuItems.put(CAPITAL_MENU, capitalMenu);
		MenuBar incomeMenu = new MenuBar(true);
		MenuBar spendingMenu = new MenuBar(true);
		MenuBar bankingMenu = new MenuBar(true);
		MenuBar assetsMenu = new MenuBar(true);
		MenuBar staffMenu = new MenuBar(true);
		MenuBar budgetMenu = new MenuBar(true);
		MenuBar helpMenu = new MenuBar(true);
		this.addItem(MenuText.CAPITAL_MENU_NAME, capitalMenu);
		this.addItem(MenuText.INCOME_MENU_NAME, incomeMenu);
		this.addItem(MenuText.SPENDING_MENU_NAME, spendingMenu);
		this.addItem(MenuText.BANKING_MENU_NAME, bankingMenu);
		this.addItem(MenuText.ASSETS_MENU_NAME, assetsMenu);
		this.addItem(MenuText.STAFF_MENU_NAME, staffMenu);
		this.addItem(MenuText.BUDGET_MENU_NAME, budgetMenu);
		this.addItem(MenuText.HELP_MENU_NAME, helpMenu);
		
		
		MenuItem equityMenu = new MenuItem(MenuText.EQUITY_LABEL, new Command(){
			@Override
			public void execute() {
				// TODO Auto-generated method stub
			}
		});
		capitalMenu.addItem(equityMenu);	
		MenuItem bondsMenu = new MenuItem(MenuText.BONDS_LABEL, new Command(){
			@Override
			public void execute() {
				//TODO add command to show loans
			}
		});
		capitalMenu.addItem(bondsMenu);
		
		MenuItem balanceMenu = new MenuItem(MenuText.BALANCE_LABEL, new Command(){
			@Override
			public void execute() {
				//TODO add command to show balance sheet
			}
		});
		///IncomeMenu and its submenus///
		//Items on the Income Menu
		MenuBar invoiceMenu = new MenuBar(true);
		MenuBar customerMenu = new MenuBar(true);
		MenuBar productsMenu = new MenuBar(true);
		incomeMenu.addItem(MenuText.INVOICES_LABEL, invoiceMenu);
		incomeMenu.addItem(MenuText.CASH_LABEL, new Command(){
			@Override
			public void execute() {
				// TODO Open Cash Sales Page Command
			}
		});
		incomeMenu.addItem(MenuText.CUSTOMERS_LABEL, customerMenu);
		incomeMenu.addItem(MenuText.PRODUCTS_LABEL, productsMenu);
		//Items on the Invoice Menu (submenu of the income menu)
		invoiceMenu.addItem(MenuText.ALL_LABEL, new Command(){
			@Override
			public void execute() {
		  		InvoiceList itab = new InvoiceList(platax.getPtp(), enterprise, GWTInvoice.SELECTION_ALL);	
				platax.addTab(itab);
				
			}
		});
		invoiceMenu.addItem(MenuText.PENDING_LABEL, new Command(){
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax.getPtp(), enterprise, GWTInvoice.SELECTION_PENDING);	
				platax.addTab(itab);
			}
		});
		invoiceMenu.addItem(MenuText.PAID_LABEL, new Command(){
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax.getPtp(), enterprise, GWTInvoice.SELECTION_PAID);	
				platax.addTab(itab);
			}
		});
		invoiceMenu.addItem(MenuText.OVERDUE_LABEL,  new Command(){
			@Override
			public void execute() {
				InvoiceList itab = new InvoiceList(platax.getPtp(), enterprise, GWTInvoice.SELECTION_OVERDUE);	
				platax.addTab(itab);
			}
		});
		invoiceMenu.addItem(MenuText.ADD_NEW_LABEL, new Command(){
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
				CustomerList ctab = new CustomerList(platax.getPtp(), enterprise, 0);
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
				ProductList ctab = new ProductList(platax.getPtp(), enterprise, 0);
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
