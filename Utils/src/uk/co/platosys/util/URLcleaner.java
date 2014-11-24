/*
 * Created on Mar 15, 2006
 * (c) copyright 2006/7 Platosys
 *  Copyright (C) 2008  Edward Barrow

    This class is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * 
 * for more information contact edward.barrow@platosys.co.uk
 */
package uk.co.platosys.util;

public class URLcleaner {

/**
 * Substitutes "%20" for spaces
 * @param queryURL
 * @return
 */
public static String safeURL(String queryURL){
	
	String cleanString=new String();
	char[] chs = new char[1];
	for (int i=0; i<queryURL.length(); i++){
		char ch = queryURL.charAt(i);
		chs[0]=ch;
		if (ch==' '){
			cleanString=cleanString+"%20";
		}else{
			cleanString=cleanString+new String(chs);
		}
		
	}
	
	return cleanString;
	
}
/**
 * Substitutes spaces for "%20"
 * @param queryString
 * @return
 */
public static String cleanQuery(String queryString){
	String cleanString=new String();
	char[] chs = new char[1];
	for (int i=0; i<queryString.length(); i++){
		char ch = queryString.charAt(i);
		chs[0]=ch;
		if (ch=='%'){
			 if (queryString.charAt(i+1)=='2'){
				 if (queryString.charAt(i+2)=='0'){
					 cleanString=cleanString+" ";
					 i+=2;
				 }
			 }
			
		}else{
			String nes = new String(chs);
			cleanString=cleanString+nes;
		}
		
	}
	return cleanString;
	
	
}



}

