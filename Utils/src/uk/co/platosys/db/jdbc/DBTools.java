/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.platosys.db.jdbc;

/**
 *
 * @author edward
 */
public class DBTools {
     /**this removes  spaces, punctuation and funny characters from supplied strings
     * and ensures that the first character is a letter.
     * It makes things work as sql database, table and column names.
     * 
     * @param string
     * @return
     */
  	public static String removeFunnyCharacters(String string){
  		StringBuffer buffer = new StringBuffer();
  		char a = string.charAt(0);
                if (Character.isLetter(a)){
                    buffer.append(a);
                }else {
                    buffer.append('q');
                }
  		for (int i=1; i<string.length(); i++){
   		char x = string.charAt(i);
   		if (Character.isLetterOrDigit(x)){
   			buffer.append(x);
   		}
   		
   	}
   	return new String(buffer);
   }
}
