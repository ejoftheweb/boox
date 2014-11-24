package filefix;

import java.io.*;
import java.nio.file.Files;

public class Filefix {
	public Filefix(File inputDirectory, File outputDirectory){
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
				File outfolder=outputDirectory;
				while ((line = br.readLine()) != null) {
				
				
									//now parse the line:
					
					//System.out.println(line);
					
					
					if (line.startsWith("package")){
						if (line.endsWith(";")){
							int l = line.length()-1;
							line=line.substring(0, l);
						}
						//create the package directory tree
						String packageName= line.substring(8);
						//System.out.println(packageName);
						String[] packages=packageName.split("\\.");
						int depth = packages.length;
						for (int i=0; i<depth; i++){
							String packagen = packages[i];
							File packageFolder=new File(outfolder, packagen);
							packageFolder.mkdirs();
							outfolder=packageFolder;
						}
					}else if (line.startsWith("public class")){
						String[] words = line.split(" ");
						String filename="";
						try{
							filename= words[2];
						}catch(ArrayIndexOutOfBoundsException bx){
							System.out.println("Could not parse class declaration in file "+file.getName());
							System.out.println("offending line: "+line);
							filename=file.getName();
							notrenamed++;
						}
							
							File javafile = new File(outfolder, filename+".java");
							int deduplicator=0;
							while (javafile.exists()){
								deduplicator++;
								String filenamer=filename+deduplicator;
								javafile=new File(outfolder, filenamer+".java");
							}
							Files.copy(file.toPath(), javafile.toPath());
							success=true;
							br.close();
							
						break;
					}/*else if (line.startsWith("public final class")){
						/*String[] words = line.split(" ");
						String filename="";
						try{
							filename= words[3];
						}catch(ArrayIndexOutOfBoundsException bx){
							System.out.println("Could not parse class declaration in file "+file.getName());
							System.out.println("offending line: "+line);
							
							filename=file.getName();
							notrenamed++;
						}
							
							File javafile = new File(outfolder, filename+".java");
							int deduplicator=0;
							while (javafile.exists()){
								deduplicator++;
								String filenamer=filename+deduplicator;
								javafile=new File(outfolder, filenamer+".java");
							}
							Files.copy(file.toPath(), javafile.toPath());
							success=true;
							br.close();
							
						break;
					}else if (line.startsWith("public abstract class")){
						String[] words = line.split(" ");
						String filename="";
						try{
							filename= words[3];
						}catch(ArrayIndexOutOfBoundsException bx){
							System.out.println("Could not parse abstract class declaration in file "+file.getName());
							System.out.println("offending line: "+line);
							
							filename=file.getName();
							notrenamed++;
						}
						File javafile = new File(outfolder, filename+".java");
						int deduplicator=0;
							while (javafile.exists()){
								deduplicator++;
								String filenamer=filename+deduplicator;
								javafile=new File(outfolder, filenamer+".java");
							}
							Files.copy(file.toPath(), javafile.toPath());
							success=true;
							br.close();
						
						break;
					}else if (line.startsWith("public interface")){
						String[] words = line.split(" ");
						String filename="";
						try{
							filename= words[2];
						}catch(ArrayIndexOutOfBoundsException bx){
							System.out.println("Could not parse interface declaration  in file "+file.getName());
							System.out.println("offending line: "+line);
							
							filename=file.getName();
							notrenamed++;
						}
						File javafile = new File(outfolder, filename+".java");
						int deduplicator=0;
							while (javafile.exists()){
								deduplicator++;
								String filenamer=filename+deduplicator;
								javafile=new File(outfolder, filenamer+".java");
							}
							Files.copy(file.toPath(), javafile.toPath());
							success=true;
							br.close();
							
						break; 
					}*/
				}
				if(success){copied++;}
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
			 System.out.println("copied/processed "+copied+"/"+processed+"/"+total+ "files");
		}
		System.out.println("processed "+processed+ "files");
		System.out.println("copied "+copied+" of "+total+" files");
		System.out.println(notrenamed + " files could not be renamed but are in their correct package folder");
		System.out.println(notfound+" files were not found for some reason");
		System.out.println(fails+" files failed to copy for some unknown reason");
	}
	
	
public static void main(String[] args){
	if (args.length!=2){
		System.out.println("Usage: java filefix.Filefix [INPUT_DIR] [OUTPUT_DIR]");
		System.exit(0);
	}
	File inputDirectory = new File (args[0]);
	File outputDirectory = new File (args[1]);
	System.out.println("Filefix running on "+inputDirectory.getAbsolutePath()+" to "+outputDirectory.getAbsolutePath());
	Filefix fileFix = new Filefix(inputDirectory, outputDirectory);
	
}
}
