package uk.co.platosys.xuser;


	

	import uk.co.platosys.db.PlatosysDBException;
	import uk.co.platosys.db.Row;
	import uk.co.platosys.db.jdbc.JDBCTable;
	import uk.co.platosys.util.Logger;
	import uk.co.platosys.util.RandomString;

	/**Xcontact is a class for handling and storing contact information other than addresses 
	 * Xcontact info is about people; Xaddress is about places. 
	 * Xbizinfo is about organisations/businesses etc.
	 * 
	 * 
	 * @author edward
	 *
	 **xContactID
	 **given_name
	 **family_name
	 **title
	 **email
	 **mobile
	 *
	 * 
	 */
	public class Xcontact {
		private String xContactID;
		private Row row;
		private static String databaseName = XuserConstants.DATABASE_NAME;
		public static final String GIVEN_NAME="given_name";
		public static final String FAMILY_NAME="family_name";
		public static final String TITLE="title";
		public static final String EMAIL="email";
		public static final String MOBILE="mobile";
		public static final String[] fieldNames = {GIVEN_NAME,FAMILY_NAME,TITLE,EMAIL,MOBILE};
	 	private static Logger logger = Logger.getLogger(XuserConstants.APPLICATION_NAME);
	 	private static JDBCTable xcontactTable=initTable();

	 //TODO
	 	/**
	 	 * retrieves a contact given its ID
	 	 */
		public Xcontact(String xcontactID) throws XuserException{
			try{
				if(xcontactTable==null){
					xcontactTable=initTable();
				}
				this.row = xcontactTable.getRow(xcontactID);
						
				
			}catch(Exception x){
				throw new XuserException("Xcontact: constructor problem", x);
			}
		}
		/**
		 * creates an Xcontact from an array of parameters. The Contact1 is stored
		 * and can in future be retrieved by its XaID.
		 * @param vals
		 * @throws XuserException
		 */
		public Xcontact (String[] vals) throws XuserException {
			if(vals.length!=fieldNames.length){
				throw new XuserException("xcontact values array wrong size");
			}
			try{
			xContactID = RandomString.getRandomKey();
			for (int i=0; i<vals.length; i++){
				xcontactTable.amend(xContactID, fieldNames[i],vals[i]);
			}}catch(Exception x){
				throw new XuserException("problem creating Xcontact", x);
			}
			
		}
		 
		/**
		 * return an xContactID
		 * @return
		 */
		public String getXcontactID() {
			return xContactID;
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
				throw new XuserException("problem finding contact field", x);
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
				if (!(xcontactTable.amend(xContactID, fieldName, fieldValue))){
					throw new XuserException("problem amending contacts JDBCTable");
				}
				row=xcontactTable.getRow(xContactID);
			}catch(Exception x){
				throw new XuserException("problem finding contacts field", x);
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
			for(int i=0; i<fieldNames.length; i++){
			    String val=row.getString(fieldNames[i]);
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
			logger.log("XC database name is "+databaseName);
			JDBCTable contaxTable=null;
			if(!JDBCTable.tableExists(databaseName, "xcontacts")){
				try {
					contaxTable=JDBCTable.createTable(databaseName, "xcontacts", "xcontactid",JDBCTable.TEXT_COLUMN);
				} catch (PlatosysDBException e) {
					logger.log("problem creating xcontacts JDBCTable", e);
					
				}
				for(int i=0; i<fieldNames.length; i++){
					contaxTable.addColumn(fieldNames[i], JDBCTable.TEXT_COLUMN);
				}
				return contaxTable;
			}else{
				try {
					contaxTable=new JDBCTable(databaseName,"xcontacts", "xaddressid");
				} catch (PlatosysDBException e) {
					logger.log("XC problem initialising xContact JDBCTable",e);
				}
			   return contaxTable;	
			}
		}
}

