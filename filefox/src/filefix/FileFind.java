package filefix;

import java.io.*;
import java.nio.file.Files;

public class FileFind {
	public FileFind(File inputDirectory, File outputDirectory, String searchTerm){
		File[] inputList = inputDirectory.listFiles();
		System.out.println("processing"+ inputList.length+"files" );
		int total = inputList.length;
		int copied=0;
		int processed=0;
		int fails = 0;
		int notrenamed=0;
		int notfound=0;
		BufferedReader br = null;
		for (File file : inputList){
			boolean success=false;
			try{
				//System.out.println(file.getAbsolutePath());
				br = new BufferedReader(new FileReader(file));
				String line;
				File outfolder=new File(outputDirectory, searchTerm);
				if(outfolder.mkdir()||outfolder.exists()){
					while ((line = br.readLine()) != null) {
						if (line.contains(searchTerm)){
							File outfile = new File(outfolder, file.getName());
							Files.copy(file.toPath(), outfile.toPath());
							copied++;
							br.close();
							break;
						}
					}
				}else{
					throw new java.io.IOException("failed to create output folder");
				}
			}catch(java.io.FileNotFoundException x){	
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(x.getClass().getName()+":"+x.getMessage());
				notfound++;
			}catch(Exception e){
				try {
					br.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(e.getClass().getName());
			
				fails++;
			}finally{
				try{br.close();}catch(Exception x){
					System.out.println("error in finally block closing buffered reader "+ x.getClass().getName());
				}
			}
			 processed++;
		}
		System.out.println("processed "+processed+ "files");
		System.out.println("copied "+copied+" of "+total+" files");
		
	}
	
	
public static void main(String[] args){
	if (args.length!=3){
		System.out.println("Usage: java filefix.FileFind [INPUT_DIR] [OUTPUT_DIR] [SEARCH_TERM]");
		System.exit(0);
	}
	File inputDirectory = new File (args[0]);
	File outputDirectory = new File (args[1]);
	String searchTerm = args[2];
	System.out.println("Filefind running on "+inputDirectory.getAbsolutePath()+" to "+outputDirectory.getAbsolutePath());
	FileFind fileFix = new FileFind(inputDirectory, outputDirectory, searchTerm);
	
}
}
