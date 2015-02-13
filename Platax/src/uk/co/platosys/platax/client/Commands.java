package uk.co.platosys.platax.client;

import uk.co.platosys.platax.client.forms.CashRegisterForm;
import uk.co.platosys.platax.client.forms.CashierForm;
import uk.co.platosys.platax.client.forms.CustomerForm;
import uk.co.platosys.platax.client.forms.ModulesForm;
import uk.co.platosys.platax.client.forms.ProductForm;
import uk.co.platosys.platax.client.forms.bills.InvoiceForm;
import uk.co.platosys.platax.client.forms.lists.CustomerList;
import uk.co.platosys.platax.client.forms.lists.InvoiceList;
import uk.co.platosys.platax.client.forms.lists.ProductList;
import uk.co.platosys.platax.client.forms.tasks.CashUp;
import uk.co.platosys.platax.client.forms.tasks.DirectorsLoan;
import uk.co.platosys.platax.client.forms.tasks.HireStaff;
import uk.co.platosys.platax.client.forms.tasks.IssueEquity;
import uk.co.platosys.platax.client.reports.BalanceSheet;
import uk.co.platosys.platax.shared.boox.GWTInvoice;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Window;

/**
 * This class defines all the commands used in the application.
 * They can be called from: 
 *  - a menu (usually the EnterpriseMenu);
 *  - an ActiveFieldLabel;
 *  - a Task
 *  Almost invariably, a Command instantiates a pTab in the central panel.
 *  
 *   This class must be edited and updated when we add a new module to the system.
 *   
 * @author edward
 *
 */
public class Commands {
	 
			public static Scheduler.ScheduledCommand BALANCE_SHEET = new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					BalanceSheet balanceSheet = new BalanceSheet();//platax, enterprise);
					Platax.addPTab(balanceSheet);
				}
			};
			
			public static Scheduler.ScheduledCommand ISSUE_EQUITY=new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					IssueEquity issueEquity = new IssueEquity();
					Platax.addPTab(issueEquity);
				}
			};
			public static Scheduler.ScheduledCommand SHARE_REGISTER =new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					//TODO
					Window.alert("Feature not yet implemented");
				}
			};
			public static Scheduler.ScheduledCommand DIRECTORS_LOANS=new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					DirectorsLoan directorsLoan = new DirectorsLoan();//platax, enterprise);
					Platax.addPTab(directorsLoan);
					
				}
			};
			
			
			public static Scheduler.ScheduledCommand CASH_UP= new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					CashUp scr = new CashUp();//platax, enterprise);
					Platax.addPTab(scr);
				}
			};
			public static Scheduler.ScheduledCommand NEW_REGISTER = new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					CashRegisterForm scr = new CashRegisterForm();
					Platax.addPTab(scr);
				}
			};
			/*
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
					CustomerList ctab = new CustomerList(platax, enterprise, Constants.ALL_CUSTOMERS);
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
					ProductList ctab = new ProductList(platax, enterprise, Constants.ALL_PRODUCTS);
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
			}); */
}
