package uk.co.platosys.platax.shared.boox;

import com.google.gwt.view.client.ProvidesKey;

public class GWTProduct extends GWTItem implements Comparable<GWTProduct> {

	@Override
	public int compareTo(GWTProduct arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	  /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<GWTProduct> KEY_PROVIDER = new ProvidesKey<GWTProduct>() {
      @Override
      public Object getKey(GWTProduct item) {
    	  return item == null ? null : item.getItemID();
      }
    };
}
