package uk.co.platosys.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;

public class Nations {
 private static File dataFile= new File("/etc/platosys/nations.xml");
 private static Document natdoc = DocMan.build(dataFile);

 public Nations(String outputFilename) throws IOException{
	 File outfile = new File(outputFilename);
	 if (outfile.createNewFile()){
		Writer outwrite = new FileWriter(outfile);
		String headstring="package uk.co.platosys.gwutils; \n\n"
				+ "import java.util.* ;\n"
				+ "import java.io.Serializable ;\n"
				+ "\n"
				+ "/** Class to list nations and nationalities*/\n"
				+ "public class Nations implements Serializable {\n";
		outwrite.append(headstring);
		String nationsmapdefstring="static Map<String, String> nations = new HashMap<String, String>();\n";
		String nationalitiesmapdefstring="static Map<String, String> nationalities = new HashMap<String, String>();\n";
		outwrite.append(nationsmapdefstring);
		outwrite.append(nationalitiesmapdefstring);
		String codesArrayDef="public static final String [] CODES = {";
		String natsArrayDef = "public static final String [] NATIONS = {";
		String natysArrayDef = "public static final String [] NATIONALITIES = {";
		for (Entry<String, String> entry:getNations().entrySet()){
			//"public static final String gb="United Kingdom";"
			String root="public static final String ";
			String nationvarname=entry.getKey();
			codesArrayDef=codesArrayDef+"\""+nationvarname+"\", ";
			String nationname=entry.getValue();
			natsArrayDef=natsArrayDef+nationvarname+", ";
			String nationality=getNationality(nationvarname);
			
			
			String nation=root+nationvarname;
			nation=nation+" = \""+nationname+"\";\n";
			outwrite.append(nation);
			//"public static final String gby="British";"
			String nyvar=nationvarname+"Y";
			nation=root+nyvar;
			nation=nation+" = \""+nationality+"\";\n";
			natysArrayDef=natysArrayDef+nyvar+", ";
			outwrite.append(nation);
		}
		//add blank trailing entry to array defs to avoid final comma:
		String blank="  ";
		codesArrayDef=codesArrayDef+ blank +"};\n";
		natsArrayDef=natsArrayDef+ blank +"};\n";
		natysArrayDef=natysArrayDef+ blank +"};\n";
		outwrite.append(codesArrayDef);
		outwrite.append(natsArrayDef);
		outwrite.append(natysArrayDef);
		
		outwrite.append("public static List<String> getNationCodes(){\n "
				+ "List<String> codes = new ArrayList<String>();\n"
				+ "for(String code:CODES){\n"
				+ "codes.add(code);\n"
				+ "}\n"
				+ "return codes;"
				+ "}\n");
		outwrite.append("public static List<String> getNationNames(){  "
				+ "List<String> codes = new ArrayList<String>();\n"
				+ "for(String code:NATIONS){\n"
				+ "codes.add(code);\n"
				+ "}\n"
				+ "return codes;"
				+ "}\n"); 
		
		outwrite.append("public static List<String> getNationalityNames(){  "
				+ "List<String> codes = new ArrayList<String>();\n"
				+ "for(String code:NATIONALITIES){\n"
				+ "codes.add(code);\n"
				+ "}\n"
				+ "return codes;"
				+ "}\n"); 
		outwrite.append("public static Map<String, String> getNations(){  "
				+ "Map<String, String> nations = new TreeMap<String, String>();\n"
				+ "int i=0;\n"
				+ "for(String code:CODES){\n"
				+ "nations.put(code, NATIONS[i]);\n"
				+ "i++;\n"
				+ "}\n"
				+ "return nations;"
				+ "}\n"); 
		outwrite.append("public static Map<String, String> getNationalities(){  "
				+ "Map<String, String> nations = new TreeMap<String, String>();\n"
				+ "int i=0;\n"
				+ "for(String code:CODES){\n"
				+ "nations.put(code, NATIONALITIES[i]);\n"
				+ "i++;\n"
				+ "}\n"
				+ "return nations;"
				+ "}\n"); 
		outwrite.append("\n }");
		outwrite.flush();
		outwrite.close();
	 }
	 
 }
 
 
 public static Map<String, String> getNations(){
	 Map<String, String> natmap = new HashMap<String, String>();
	 for(Element nation:natdoc.getRootElement().getChildren("nation")){
		 natmap.put(nation.getAttributeValue("code"), nation.getAttributeValue("name"));
	 }
	 return natmap;
 }
 public static String getNationality(String code){
	 return getNationalities().get(code);
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
 
 public static void main (String[] args){
	// String outputFilen = args[0];
	try {
		new Nations("/home/edward/Nations.java");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		//logger.log("error:",e);
	}
 }
}
