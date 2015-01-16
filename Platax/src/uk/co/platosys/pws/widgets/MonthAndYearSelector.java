package uk.co.platosys.pws.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.MonthSelector;    



public class MonthAndYearSelector extends MonthSelector {

    private static String BASE_NAME = "datePicker";
    private PushButton backwards;
    private PushButton forwards;
    private PushButton backwardsYear;
    private PushButton forwardsYear;
    private PushButton backwardsDecade;
    private PushButton forwardsDecade;
    private Grid grid;
    private int previousDecadeColumn = 0;
    
    private int previousYearColumn = 1;
    private int previousMonthColumn = 2;
  
    private int nextMonthColumn = 4;
    private int nextYearColumn = 5;
    private int nextDecadeColumn = 6;
    
    private CalendarModel model;
    private YearDatePicker picker;
    private Label monthYearLabel=new Label();

    public MonthAndYearSelector() {

        
       
    }

    public void setModel(CalendarModel model) {
        this.model = model;
    }

    public void setPicker(YearDatePicker picker) {
        this.picker = picker;
    }

   

    @Override
    protected void setup() {
        // Set up backwards.
        backwards = new PushButton();
        backwards.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                addMonths(-1);
            }
        });

        backwards.getUpFace().setHTML("&lsaquo;");
        backwards.setStyleName(BASE_NAME + "PreviousButton");

       

        // Set up backwards year
        backwardsYear = new PushButton();
        backwardsYear.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {

                addMonths(-12);
                picker.refreshComponents();
            }
        });

        backwardsYear.getUpFace().setHTML("&laquo;");
        backwardsYear.setStyleName(BASE_NAME + "PreviousButton");
     // Set up backwards decade
        backwardsDecade = new PushButton();
        backwardsDecade.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {

                addMonths(-120);
                picker.refreshComponents();
            }
        });

        backwardsDecade.getUpFace().setHTML("&laquo;&lsaquo;");
        backwardsDecade.setStyleName(BASE_NAME + "PreviousButton");
        
        
        forwards = new PushButton();
        forwards.getUpFace().setHTML("&rsaquo;");
        forwards.setStyleName(BASE_NAME + "NextButton");
        forwards.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            		addMonths(1);
            }
        });
        
        forwardsYear = new PushButton();
        forwardsYear.getUpFace().setHTML("&raquo;");
        forwardsYear.setStyleName(BASE_NAME + "NextButton");
        forwardsYear.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            		addMonths(+12);
                    picker.refreshComponents();
            }
        });
        forwardsDecade = new PushButton();
        forwardsDecade.getUpFace().setHTML("&rsaquo;&raquo;");
        forwardsDecade.setStyleName(BASE_NAME + "NextButton");
        forwardsDecade.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                   addMonths(+120);
                   picker.refreshComponents();
            }
        });

        

        // Set up grid.
        grid = new Grid(1, 7);
        grid.setWidget(0, previousDecadeColumn, backwardsDecade);
        
        grid.setWidget(0, previousYearColumn, backwardsYear);
        grid.setWidget(0, previousMonthColumn, backwards);
        grid.setWidget(0, 3, monthYearLabel);
        grid.setWidget(0, nextMonthColumn, forwards);
        grid.setWidget(0, nextYearColumn, forwardsYear);
        grid.setWidget(0, nextDecadeColumn, forwardsDecade);

        CellFormatter formatter = grid.getCellFormatter();

        formatter.setWidth(0, previousYearColumn, "1");
        formatter.setWidth(0, previousMonthColumn, "1");

        formatter.setWidth(0, nextMonthColumn, "1");
        formatter.setWidth(0, nextYearColumn, "1");
        grid.setStyleName(BASE_NAME + "MonthSelector");
        initWidget(grid);
    }

    public void addMonths(int numMonths) {
        model.shiftCurrentMonth(numMonths);
        picker.refreshComponents();
    }

	@Override
	protected void refresh() {
		if(model!=null){
			monthYearLabel.setText(model.formatCurrentMonthAndYear());
		}
	}

   
}     