import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ShellMain {


	public static void main(String[] args) throws java.io.IOException {


		String str;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {

			// Read what the user entered

			System.out.print("Shell>");

			str = br.readLine(); {
				
				// If user enters a return, just loop again

				if (str.equals("")) {
					continue;
				}
				/**
				 * 
				 * Command “exit" terminates the shell* */
				
				else if(str.equalsIgnoreCase("exit"))  {
					System.out.println("Exit Shell");
					System.exit(0);
				}
				else{
					CommandExecuter cmdexe = new CommandExecuter(str);
					if(!cmdexe.getStatus().isEmpty()){
						System.out.println(cmdexe.getStatus());
					}
					}
			}
		}
	}
}

