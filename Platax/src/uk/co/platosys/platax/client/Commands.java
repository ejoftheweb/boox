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
import uk.co.platosys.platax.client.forms.tasks.Bank;
import uk.co.platosys.platax.client.forms.tasks.CashUp;
import uk.co.platosys.platax.client.forms.tasks.DirectorsLoan;
import uk.co.platosys.platax.client.forms.tasks.HireStaff;
import uk.co.platosys.platax.client.forms.tasks.IssueEquity;
import uk.co.platosys.platax.client.forms.tasks.NewBankAccount;
import uk.co.platosys.platax.client.forms.tasks.PaymentsIn;
import uk.co.platosys.platax.client.forms.tasks.PaymentsOut;
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
 *   Note that because these Commands can be called from anywhere in the system, they cannot
 *   readily pass in parameters. Tab constructors must look up  authentication and context parameters 
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
			});*/
			//Items on the Invoice Menu (submenu of the income menu)
			public static Scheduler.ScheduledCommand INVOICES_LIST_ALL = new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					InvoiceList itab = new InvoiceList( GWTInvoice.SELECTION_ALL);	
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand INVOICES_LIST_PENDING= new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					InvoiceList itab = new InvoiceList(GWTInvoice.SELECTION_PENDING);	
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand INVOICES_LIST_PAID= new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					InvoiceList itab = new InvoiceList(GWTInvoice.SELECTION_PAID);	
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand INVOICES_LIST_DUE= new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					InvoiceList itab = new InvoiceList(GWTInvoice.SELECTION_OVERDUE);	
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand NEW_INVOICE = new Scheduler.ScheduledCommand() {
				@Override
					public void execute() {
					//Window.alert("NI command called");
					InvoiceForm itab = new InvoiceForm();	
					Platax.addPTab(itab);
				}
			};
			/////
			
			//items on the CustomerMenu, submenu of Income menu
			public static Scheduler.ScheduledCommand CUSTOMERS_LIST_ALL= new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					CustomerList ctab = new CustomerList( Constants.ALL_CUSTOMERS);
					Platax.addPTab(ctab);
				}
			};/*
			newCustomer.setScheduledCommand(new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					//Window.alert("new invoice menu item selected");
			  		CustomerForm itab = new CustomerForm(platax, enterprise);	
					platax.addTab(itab);
			}});
			/////*/
			//items on the productsmenu, submenu of Income menu
			public static Scheduler.ScheduledCommand PRODUCTS_LIST_ALL=new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					ProductList ctab = new ProductList( Constants.ALL_PRODUCTS);
					Platax.addPTab(ctab);
				}
			};/*
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
			});*/
			public static Scheduler.ScheduledCommand BANK_ACCOUNTS = new Scheduler.ScheduledCommand() {
				@Override
				public void execute() {
					Bank itab=new Bank();
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand NEW_BANK_ACCOUNT = new Scheduler.ScheduledCommand() {

				@Override
				public void execute() {
					Window.alert("command called");
					NewBankAccount itab= new NewBankAccount();
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand PAYMENTS_IN = new Scheduler.ScheduledCommand() {
				@Override
					public void execute() {
					PaymentsIn itab = new PaymentsIn();	
					Platax.addPTab(itab);
				}
			};
			public static Scheduler.ScheduledCommand PAYMENTS_OUT = new Scheduler.ScheduledCommand() {
				@Override
					public void execute() {
					PaymentsOut itab = new PaymentsOut();	
					Platax.addPTab(itab);
				}
			};
			/*
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
