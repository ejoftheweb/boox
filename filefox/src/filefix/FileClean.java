package filefix;

import java.io.*;

public class FileClean {
	public FileClean(File inputFile, File outputDirectory){
		BufferedReader br = null;
		BufferedWriter bw=null;
			try{
				br = new BufferedReader(new FileReader(inputFile));
				File outputFile=new File(outputDirectory, inputFile.getName());
				bw = new BufferedWriter(new FileWriter(outputFile));
				String headerLine=br.readLine();
				bw.append(headerLine);
				String line;
				int linno=0;
				int nc=0;
				int lc=0;
				while ((line = br.readLine()) != null) {
					linno++;
					String [] fields = line.split(",");
					if ((fields[17]!=null)&&(Integer.parseInt(fields[17])>0)){
						bw.append(line);
						bw.newLine();
						lc++;
					}else{
						System.out.println("line "+linno +" was zero, not copied");
						nc++;
					}
				}
				System.out.println(lc+ " lines copied");
				System.out.println(nc+ " zero lines not copied");
				bw.flush();
				bw.close();
			}catch(java.io.FileNotFoundException x){	
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(x.getClass().getName()+":"+x.getMessage());
				
			}catch(Exception e){
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
				System.out.println(e.getClass().getName());
			
				 
			}finally{
				try{
					bw.flush();
					bw.close();
					br.close();
				}catch(Exception x){
					//If it gets here things will normally be closed anyway.
					
				}
			}
			 
		}
		
		
	
	
	
public static void main(String[] args){
	if (args.length!=2){
		System.out.println("Usage: java filefix.FileClean [INPUT_FILE] [OUTPUT_DIR]");
		System.exit(0);
	}
	File inputFile = new File (args[0]);
	File outputDirectory = new File (args[1]);
	
	System.out.println("FileClean running on "+inputFile.getAbsolutePath()+" to "+outputDirectory.getAbsolutePath());
	FileClean fileFix = new FileClean( inputFile, outputDirectory);
	
}
}
