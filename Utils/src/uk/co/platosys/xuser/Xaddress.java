package uk.co.platosys.xuser;

import uk.co.platosys.db.PlatosysDBException;
import uk.co.platosys.db.Row;
import uk.co.platosys.db.jdbc.JDBCTable;
import uk.co.platosys.util.Logger;
import uk.co.platosys.util.RandomString;

/**Xaddress is a class for handling and storing geographical {postal} addresses 
 *  The field names and structures are geared towards UK-style addresses should work for most other address styles.
 *  all fields can be null but we can set postcode to be not null
 * @author edward
 *
 **XaddressID
 **building: including number in building 23 International House
 **street: including number in street 52 Rodean Road
 **district Blickarly
 **town LITTLEHAMPTON - maps to us City
 **county snants
 **postcode LH34 0BD
 **country UK
 **
 * usage: collect data and create an XaID; store XaID in a JDBCTable with another ID and an XaddressType
 *to retrieve an address, instantiate an Xaddress with the XaID constructor then call a suitable method.
 */
public class Xaddress {
	private String XaddressID;
	private Row row;
	private static String databaseName = XuserConstants.DATABASE_NAME;
	public static final String[] FIELD_NAMES = {"building","street","district","town","county","postcode"};
	public static final String TABLENAME="xaddress";
	public static final String XAID_COLNAME="xaddressid";
 	private String[] fieldValues=new String[FIELD_NAMES.length];
	private static Logger logger = Logger.getLogger(XuserConstants.APPLICATION_NAME);
 	private static JDBCTable xaddressTable=initTable();

 //TODO
 	/**
 	 * retrieves an address given its ID
 	 */
	public Xaddress(String xaddressID) throws XuserException{
		try{
			if(xaddressTable==null){
				xaddressTable=initTable();
			}
			this.row = xaddressTable.getRow(xaddressID);
			for(int i=0; i<FIELD_NAMES.length; i++){
				fieldValues[i]=row.getString(FIELD_NAMES[i]);
			}
			
		}catch(Exception x){
			throw new XuserException("xaddress problem", x);
		}
	}
	/**
	 * creates an Xaddress from an array of parameters. The address is stored
	 * and can in future be retrieved by its XaID.
	 * @param vals
	 * @throws XuserException
	 */
	public Xaddress (String[] vals) throws XuserException {
		logger.log("creating a new xaddress");
		if(vals.length!=FIELD_NAMES.length){
			throw new XuserException("xaddress values array wrong size");
		}
		this.fieldValues=vals;
		try{
		XaddressID = RandomString.getRandomKey();
		for (int i=0; i<vals.length; i++){
			xaddressTable.amend(XaddressID, FIELD_NAMES[i],vals[i]);
		}
		this.row=xaddressTable.getRow(XaddressID);
		}catch(Exception x){
			throw new XuserException("problem creating Xaddress", x);
		}
		
	}
	 
	/**
	 * return an XaddressID
	 * @return
	 */
	public String getXaddressID() {
		return XaddressID;
	}
	
	/**
	 * returns the value of a particular field, given its fieldname.
	 * @param fieldName
	 * @return
	 * @throws XuserException
	 */
	public String getFieldValue(String fieldName)throws XuserException{
		try{
			return row.getString(fieldName);
		}catch(Exception x){
			throw new XuserException("problem finding address field", x);
		}
	}
	/**
	 * sets the value of a particular fiele
	 * @param fieldName
	 * @param fieldValue
	 * @throws XuserException
	 */
	
	public void setFieldValue(String fieldName, String fieldValue) throws XuserException{
		try{
			if (!(xaddressTable.amend(XaddressID, fieldName, fieldValue))){
				throw new XuserException("problem amending addresses JDBCTable");
			}
		}catch(Exception x){
			throw new XuserException("problem finding address field", x);
		}
	}
	/**
	 * Returns the address as a string.
	 * @param oneline if true, it will be formatted on one line, otherwise with natural linebreaks.
	 * @return
	 * @throws XuserException
	 */
	public String getAddress(boolean oneline) throws XuserException{
		String address="";
		try{
		for(int i=0; i<FIELD_NAMES.length; i++){
		    String val=row.getString(FIELD_NAMES[i]);
		    if(!val.equals("")||val==null){
		    	address=address+val;
		        if(oneline){
		        	address=address+",";
		        }else{
		        	address=address+"\n";
		        }
		    }
		}
		return address;
		}catch(Exception e){
			throw new XuserException("issue getting address", e);
		}
	}
	
	
	private static JDBCTable initTable(){
		logger.log("database name is "+databaseName);
		JDBCTable addressTable=null;
		if(!JDBCTable.tableExists(databaseName, TABLENAME)){
			try {
				addressTable=JDBCTable.createTable(databaseName, TABLENAME, XAID_COLNAME,JDBCTable.TEXT_COLUMN);
			} catch (PlatosysDBException e) {
				logger.log("problem creating xaddress JDBCTable", e);
				
			}
			for(int i=0; i<FIELD_NAMES.length; i++){
				addressTable.addColumn(FIELD_NAMES[i], JDBCTable.TEXT_COLUMN);
			}
			return addressTable;
		}else{
			try {
				addressTable=new JDBCTable(databaseName,TABLENAME, XAID_COLNAME);
			} catch (PlatosysDBException e) {
				logger.log("problem initialising xaddress JDBCTable",e);
			}
		   return addressTable;	
		}
	}
	
	public String[] getFieldValues() {
		return fieldValues;
	}
}
