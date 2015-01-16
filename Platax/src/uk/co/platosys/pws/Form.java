package uk.co.platosys.pws;

import uk.co.platosys.pws.fieldsets.FormField;



/**
 * Forms containing PWS FormFields should implement this interface.
 * 
 * The basic use case for PWS is creating simple business forms. A form consists of a series of 
 * fields. Each field has a label and an infoLabel, e.g. label: "Name", infoLabel: "Your usual name, for use in the system".
 * 
 * Fields are completed sequentially and are subject to browser-side validation against a provided regex. They are disabled until the preceding
 * field has been completed.
 * Required and optional fields are displayed identically. In general, it is poor system design to have too many optional fields; it collects
 * otiose data. Like going on a hiking trip (or a night out): take everything you need, and nothing that you don't. But there are use cases for optional fields, eg.
 * in addresses for example. Optional fields will accept blank or null entries through pressing enter or tab. 
 * 
 * You can do server-side validation field-by-field as well by attaching a ValueChangeHandler to the fields. The final object submitted on completion of the form
 * must be validated on the server side otherwise you are wide open to attack. 
 * 
 * @author edward
 *
 */
public interface Form {
	public int addField(FormField<?> field)throws Exception;
	@SuppressWarnings("rawtypes")
	public FormField getNextField(FormField currentField);
}
