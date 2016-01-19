import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	private String cmdStr;
	private String maincmd;
	private ArrayList<String> parList = new ArrayList<String>();

	public Utility(String command) {
		this.cmdStr = cleanText(command);
		init();
	}

	public String getCmdStr() {
		return cmdStr;
	}

	public boolean hasBalancedQuotes() {
		return getNumQuotes() % 2 == 0;
	}

	private int getNumQuotes() {
		if (!this.cmdStr.contains("\"")) {
			return 0;
		}
		char ch = '"';
		HashMap<Character, Integer> hmap = new HashMap<Character, Integer>();
		for (int i = 0; i < this.cmdStr.length(); i++) {
			char c = this.cmdStr.charAt(i);
			Integer val = hmap.get(new Character(c));
			if (val != null) {
				hmap.put(c, new Integer(val + 1));
			} else {
				hmap.put(c, 1);
			}
		}
		return hmap.get(ch);
	}

	/**
	 * Parse the command and move the params to param-list
	 */
	
	private void init() {
		// load the string without maincmd
		
		if (!cmdStr.contains(" ")) {
			this.maincmd = cmdStr;
			return;
		}
		
	
		String temp="";
		HashMap<Integer, String> paramPosMap = new HashMap<Integer, String>();
		Pattern ParPattern = Pattern.compile("\\s*(\"[^\"]+\"|[^\\s\"]+)");
		Matcher mat;
		
		if(cmdStr.trim().startsWith("\"")){
			mat= ParPattern.matcher(cmdStr);
			mat.find();
			this.maincmd = mat.group();
			if (!this.maincmd.replaceAll("\"", "").trim().equals("")) {
				this.maincmd = this.maincmd.replaceAll("\"", "").trim();
			}
		/* if(this.maincmd.contains("\\") || this.maincmd.contains("/")){
			 System.out.println(this.maincmd);
			 this.maincmd=Paths.get(this.maincmd).toString();
			 System.out.println(this.maincmd);
			 this.maincmd = this.maincmd.replace("\\", File.separator);
		 }	*/
			
		}else{
		
			this.maincmd = cmdStr.substring(0, cmdStr.indexOf(" ")).trim();
			//System.out.println(this.maincmd);
			temp = cmdStr.substring(cmdStr.indexOf(" "), cmdStr.length())
					.trim();
			mat = ParPattern.matcher(temp);
			
		}
		// Fetch all params within double quotes
		
		while (mat.find()) {
			String str = mat.group();
			if (!str.replaceAll("\"", "").trim().equals("")) {
				str = str.replaceAll("\"", "").trim();
				// prevMatch += tmp.indexOf(str);
				paramPosMap.put(mat.start(), str);
			}
			temp = temp.replaceFirst(str, "").replaceAll("\\s+", " ");
		}
		parList = sortMapByIntegerKey(paramPosMap);
		
	}

	private ArrayList<String> sortMapByIntegerKey(HashMap<Integer, String> map) {
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(map);
		ArrayList<String> arrList = new ArrayList<String>();
		for (Entry<Integer, String> e : treeMap.entrySet()) {
			arrList.add((String) e.getValue());
		}
		return arrList;
	}

	private String cleanText(String txt) {
		return txt.replaceAll("\\s+", " ").replaceAll("\\t+", " ").trim();
	}

	public int getParCount() {
		return parList.size();
	}

	/**
	 * Get the command context. The first word of the command 
	 * 
	 * @return The command issued in the shell.
	 */
	public String getmaincmd() {
		return maincmd;
	}

	public ArrayList<String> getParList() {
		return parList;
	}

	public boolean hasParams() {
		return parList.size() > 0;
	}
}