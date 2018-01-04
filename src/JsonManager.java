import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonManager {
	static TreeMap<String, Integer> jsonLine;

	static void createJsonFile(int ipNum){
		jsonLine = new TreeMap<String, Integer>();

		try {
			String currentDate = CurrentDate.getCurrentDayTime();

			File file = new File("data.json");
			if(file.exists()){
				readJsonToMap(file);
			}
			jsonLine.put(currentDate, ipNum); //新しいデータの追加

			//全部入れた後、書き込み
			PrintWriter fileout = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			fileout.println("[");
			for(String key : jsonLine.keySet()){
				if(currentDate.equals(key) == true){
					fileout.println("\t[\"" + key + "\"," + jsonLine.get(key) + "]");
				}else{
					fileout.println("\t[\"" + key + "\"," + jsonLine.get(key) + "],");
				}
			}
			fileout.println("]\n");
			fileout.flush();
			fileout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	static void readJsonToMap(File file){
		try {
			BufferedReader jsonReader = new BufferedReader(new FileReader(file));
			String line = "";
			int lineNum = 0;
			while((line = jsonReader.readLine()).equals("") == false){
				System.out.println(line); //HACK: test
				if(putJsonFormatMatch(line) ==  true) lineNum++;
			}
			for(int i = lineNum; i > 14; i--){
				jsonLine.pollFirstEntry();
			}
			jsonReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("fileがない");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("入出力エラー");
			e.printStackTrace();
		} catch(NullPointerException e){

		}

		return;
	}


	static boolean putJsonFormatMatch(String line){
		Pattern dateP = Pattern.compile("\\[\"(.*?)\"");
		Matcher dateMatch = dateP.matcher(line);
		Pattern numP = Pattern.compile(",(.*?)\\]");
		Matcher numMatch = numP.matcher(line);

		if(dateMatch.find() && numMatch.find()){
			try{
				String key = line.substring(dateMatch.start() + 2, dateMatch.end() -1);
				Integer value = Integer.valueOf(line.substring(numMatch.start() + 1, numMatch.end() - 1));
				jsonLine.put(key, value);
				return true;
			}catch(NumberFormatException e){
			}
		}
		return false;
	}

}
