package filefix;

import java.io.File;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import uk.co.platosys.util.DocMan;

public class Natfix {
 public Natfix(){
	 File infile=new File("/home/edward/nations.xml");
	 File outfile=new File("/home/edward/nations2.xml");
	 Document indoc=DocMan.build(infile);
	 Document outDoc=new Document();
	 Element nations=new Element("nations");
	 outDoc.setRootElement(nations);
	 Element array = indoc.getRootElement().getChild("array");
	 List<Element> countries = indoc.getRootElement().getChild("countries").getChildren("country");
	 List<Element> items = array.getChildren("item");
	 for (Element item:items){
		 String key=item.getChildText("key");
		 for(Element country:countries){
			 if(key.equals(country.getAttributeValue("countryCode"))){
				 Element nation=new Element("nation");
				 nation.setAttribute("code",key);
				 nation.setAttribute("name",country.getAttributeValue("countryName"));
				 nation.setAttribute("nationality", item.getChildText("value"));
				 nation.setAttribute("currency", country.getAttributeValue("currencyCode"));
				 nation.setAttribute("eu", "false");
				 nations.addContent(nation);
			 }
		 }
	 }
	 DocMan.write(outfile, outDoc);		 
}
 public static void main (String args[]){
	 new Natfix();
 }
}
