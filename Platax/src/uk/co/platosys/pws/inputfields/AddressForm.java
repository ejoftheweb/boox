package uk.co.platosys.pws.inputfields;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import uk.co.platosys.pws.constants.FieldText;
import uk.co.platosys.pws.fieldsets.SubmitField;
import uk.co.platosys.pws.fieldsets.TextField;
import uk.co.platosys.pws.values.PWSAddress;

public class AddressForm extends AbstractPopupForm {
   public AddressForm(String header, final AddressBox source, PWSAddress address){
	   super(header);
	   final TextField building = new TextField(FieldText.BUILDING, 1000, this, false);
	   building.setValue(address.getBuilding());
	   final TextField street = new TextField(FieldText.STREET, 2000, this, true);
	   street.setValue(address.getStreet());
	   final TextField district = new TextField(FieldText.DISTRICT, 3000, this, false);
	   district.setValue(address.getDistrict());
	   final TextField town = new TextField(FieldText.TOWN, 4000, this, true);
	   town.setValue(address.getTown());
	   final TextField postcode = new TextField(FieldText.POSTCODE, 5300, this, true);
	   postcode.setValue(address.getPostcode());
	   final TextField county = new TextField(FieldText.COUNTY, 6000, this,false );
	   county.setValue(address.getCounty());
	   final TextField country = new TextField(FieldText.COUNTRY, 7000, this, false);
	   country.setValue(address.getCountry());
	   SubmitField sub=new SubmitField(12000, this);
	   render();
	   sub.addClickHandler(new ClickHandler(){
		   @Override
			public void onClick(ClickEvent event) {
				PWSAddress address=new PWSAddress();
				address.setBuilding(building.getValue());
				address.setStreet(street.getValue());
				address.setDistrict(district.getValue());
				address.setTown(town.getValue());
				address.setPostcode(postcode.getValue());
				address.setCounty(county.getValue());
				address.setCountry(country.getValue());
				source.setValue(address, true);
				AddressForm.this.hide();
			}
	   	});
   }
}
