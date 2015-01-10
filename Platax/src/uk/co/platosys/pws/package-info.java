/**
 * PWS is the Platosys Widget Set. 
 * 
 * It is a set of enhanced GWT widgets for use in enterprise forms, particularly in Platax. 
 * 
 * 
 * How to use pws
 * 
 * PWS is intended for the rapid development of business forms for easy data input. Each form is usually associated with a particular task.
 * A form consists of a series of fields. Each field consists of a label, an input box and an info-label with an explanation.
 * Fields are enabled in sequence and the data is verified at each step. All this is handled by the pws framework on the client side. 
 * 
 * Forms must implement uk.co.platosys.pws.Form and to make this easy we provide an abstract base class AbstractForm containing all the 
 * necessary functionality. Your forms should extend this class.
 * 
 * You then add the fields simply by declaring and instantiating them inside the form constructor. All available fieldtypes are in the fields 
 * package.
 * The field constructor takes three arguments: a String array, an integer index and a reference to the containing form (this). 
 * The array should have size 4 and is best declared in a separate constants file. It contains the text for the labels and a string regex
 * used for validation (which can be null). 
 * array[0] is the label which identifies the value being input
 * array[1] is infolabel which contains slightly more detailed explanation of the data required
 * array[2] is the validation regex
 * array[3] is the error string which replaces the infolabel if the input data fails initial validation against the regex. 
 * 
 * Retrieving the values
 * The submit button included in AbstractForm has a handler which collects the form data into a SortedList<IsFieldValue>, which is passed as an argument to 
 * the servlet on the server-side. You must extract the data on the serverside and revalidate it; you should never rely solely on client-side validation.
 * 
 * 
 * 
 */
/**
 * @author edward
 *
 */
package uk.co.platosys.pws;