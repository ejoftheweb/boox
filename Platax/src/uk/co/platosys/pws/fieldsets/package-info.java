/**
 * this package contains field sets, they are triples of label/input-field/info-label
 * 
 * Fields mostly extend one of the Abstract fields - AbstractFormField or one of its abstract subclasses.
 * The concrete fields are declared final and each must call the start() method as the last item in the constructor. Extending them breaks
 * this rule as the start() method will be called in the superclass constructor. 
 * 
 * 
 */
/**
 * @author edward
 *
 */
package uk.co.platosys.pws.fieldsets;