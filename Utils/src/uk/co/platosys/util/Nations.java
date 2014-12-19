package uk.co.platosys.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

public class Nations {
 private static File dataFile= new File("/etc/platosys/nations.xml");
 private static Document natdoc = DocMan.build(dataFile);
 
 public static Map<String, String> getNations(){
	 Map<String, String> natmap = new HashMap<String, String>();
	 for(Element nation:natdoc.getRootElement().getChildren("nation")){
		 natmap.put(nation.getAttributeValue("code"), nation.getAttributeValue("name"));
	 }
	 return natmap;
 }
 public static Map<String, String> getNationalities(){
	 Map<String, String> natmap = new HashMap<String, String>();
	 for(Element nation:natdoc.getRootElement().getChildren("nation")){
		 natmap.put(nation.getAttributeValue("code"), nation.getAttributeValue("nationality"));
	 }
	 return natmap;
 }
 public static Map<String, String> getEUNations(){
	 Map<String, String> natmap = new HashMap<String, String>();
	 for(Element nation:natdoc.getRootElement().getChildren("nation")){
		 if(nation.getAttributeValue("eu").equals("true")){
			 natmap.put(nation.getAttributeValue("code"), nation.getAttributeValue("name"));
		 }
	 }
	 return natmap;
 }
 public static Map<String, String> getEUNationalities(){
	 Map<String, String> natmap = new HashMap<String, String>();
	 for(Element nation:natdoc.getRootElement().getChildren("nation")){
		 if(nation.getAttributeValue("eu").equals("true")){
			 natmap.put(nation.getAttributeValue("code"), nation.getAttributeValue("nationality"));
		 }
	 }
	 return natmap;
 }
 public boolean isEU (String code){
	 return getEUNations().containsKey(code);
 }
}
