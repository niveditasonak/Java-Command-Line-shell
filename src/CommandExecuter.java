import java.io.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CommandExecuter {
	private Utility util; 
	private String status;
	private String baseDir;
	public CommandExecuter(String cmd) {
		status="";
		try{
			util = new Utility(cmd);
			baseDir=System.getProperty("user.dir");
			init();//Make changes here
		}catch(Exception e)
		{
			System.out.println("Command Contains Special Character");
		}

	}

	public void	 init() {

		String maincmd = util.getmaincmd();

		if(maincmd.equals("dir"))
		{
			dir();
		}
		else if(maincmd.equals("copy"))
		{
			status = copy();

		}
		else if(maincmd.equals("type"))
		{
			status = type();

		}
		else if(maincmd.equals("delete"))
		{
			status = delete();
		}
		else{

			File fd = new File(maincmd);
			System.out.println(maincmd);
			if(fd.exists())
			{
				status= runexec(maincmd);
			}
			else{
				status = "Command not supported";
			}
		}


	}
	
	/**
	 * The command "copy <filename1> <filename2>", which copies <filename1> to <filename2>. 
	 * The shell creates a file <filename2>, open <filename1>,
	 * and copy the contents of <filename1> byte-by-byte to <filename2>. 
	 * */

	public String copy() {


		int numArg = util.getParCount();

		if(numArg != 2)
		{
			//<Usage copy filename1 filename2>
			return "\nIncorrect use of command.\n<Usage>: copy filename1 filename2 \n ";		}

		ArrayList<String> args = util.getParList();

		InputStream input = null;
		OutputStream output = null;
		try{
			Path path = Paths.get(args.get(1));
			input = new FileInputStream(new File(args.get(0)));
						
			if(Files.exists(path))
			{
				System.out.println("Overwrite " + args.get(1)+" ? yes/no");
				String str;
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				str = br.readLine();
				if(str.toLowerCase().equals("no") || str.toLowerCase().equals("n")){
					return "0 File Copied";
				}


			}
			output = new FileOutputStream(new File(args.get(1)));
			byte[] buff = new byte[1024];
			int bytes;
			while ((bytes = input.read(buff)) > 0) {
				output.write(buff, 0, bytes);
			}

		} catch (FileNotFoundException e) {
			return "The file "+ args.get(0)+" is not found in the system";
		} catch (IOException e) {
			return e.getMessage();
		} 

		try {
			input.close();
			output.close();
		} catch (IOException e) {
			return e.getMessage();
		}

		return "One File Copied";

	}
	
	/**
	 * The command "type <filename>" prints out the contents of <filename> to the screen 
	 * */

	public String type() {

		int numArg = util.getParCount();
		
		if(numArg !=1)
		{
			//<Usage type filename>
			return "Incorrect use of command.\n <Usage>: type filename. ";
		}

		ArrayList<String> args = util.getParList();
		File fi = new File(args.get(0));


		try {

			if (! fi.exists()) {
				return "The file " + fi.getName() + " does not exist";
			}

			BufferedReader br = null;


			FileReader fr = new FileReader(fi);
			br = new BufferedReader(fr);

			String str;

			while( (str = br.readLine()) != null ) {
				System.out.println(str);
			}
			br.close(); // Write Exception as per this
		} catch (FileNotFoundException e) {
		  return e.getMessage() + "\nFile not found: " + fi.toString();
		} catch (IOException e) {
		  return e.getMessage()+ "\nUnable to read file: " + fi.toString();
		}

		return "\nContent of the file printed to screen";
	}
	
	
	/**
	 * The command "delete <filename>" deletes the file <filename>. 
	 * */

	public String delete() {

		int numArg = util.getParCount();
		if(numArg != 1)
		{
			//<Usage delete filename>
			return "Incorrect use of command \n <Usage> delete filename ";
		}

		ArrayList<String> args = util.getParList();

		File fi = new File(args.get(0));

		try{		
			if (! fi.exists()) {
				return "The file " + fi.getName() + " does not exist";
			}

			if(fi.delete()){
				System.out.println(fi.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}

		}catch(Exception e){
			System.out.println("Unable to delete file: " + fi.toString());

		}
		return "";
	}

	/**
	 * If a command that is not “type", “exit", "delete" ,or “copy", 	
	 * the shell assumes that the command is the name of a program located in the same directory, 
	 * and attempts to run that program.
	 * If on the command line, arguments are passed to the program, shell runs the program 
	 * and pass it the arguments.
	 * */
	
	public String runexec(String FileName) {
		File fi=new File(FileName);
		if (! fi.exists()) {
			return "The file " + FileName + " does not exist";
		}
		if(fi.canExecute()){
			System.out.println("Executable file");
			ArrayList<String> command = new ArrayList<String>();
			command.add(util.getmaincmd());
			command.addAll(util.getParList());
			ProcessBuilder pb = new ProcessBuilder(command);
			Process pr;
			try {
				pr = pb.start();//process has ID right child/parent
		        BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		        String line=null;
		        while((line=br.readLine()) != null) {
                    System.out.println(line);
                }
				//Wait to get exit value
				//int exitValue = process.waitFor();
				//process.destroy();
				//System.out.println("\n\nExit Value is " + exitValue);
			} catch (IOException e) {
				e.getMessage();
			}
		}else{
			System.out.println("Not an Executable file");
		}

		return "";
	}
	
	
	/**
	 * The command dir() lists the contents of a directory.
	 * */
	public void dir() {

		File fi = new File(System.getProperty("user.dir"));
		File[] fList = fi.listFiles();
		System.out.println("\n");
		for (File f : fList) {
			
			
			if(f.isDirectory())
				System.out.println(new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(f.lastModified())+"\t"+"<DIR>\t"+""+"\t"+f.getName());
			if (f.isFile()) {
				System.out.println(new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(f.lastModified())+"\t"+"\t"+f.length()+"\t"+f.getName());
			}
		}
		NumberFormat nf = NumberFormat.getNumberInstance();
		System.out.println("\n\nAppproximately available Space =" + nf.format(fi.getUsableSpace())+" bytes\n");
	}

	/**
	 * It returns the status of the command.
	 * */
	public String getStatus() {
		return status;
	}


}


