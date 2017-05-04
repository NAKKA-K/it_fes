import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*TODO:timeout 29m sudo tcpdump -nl not port ssh and arp > tcpdump.txt ;  java TCPDumpTrans < tcpdump.txt で使用する
 * java TCPDumpTrans < sudo tcpdump -nl not port ssh and arp //名前解決しない、バッファリングする、sshとarpは弾く、リダイレクトで入力
 *Domp内容から、送信元IPを抽出する
 *抽出したIPをHashSetに追加する
 *(上を5分間、または最後の行まで続ける)
 *終わればHashSet.size()を取得。
 *細かい時間ではなくX:00、X:30といったように30分刻みの大まかな時刻表機で排出する必要あり
 *(すでにJSONがある場合、現在何行あるか調べて15行程度に抑える必要あり)
 *時間とともにsizeをJSONに出力
 */

public class TCPDumpTrans {
	private static boolean isQuit = false;
	private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) {
		while(isQuit == false){//常時起動
			HashSet<String> outputSet = new HashSet<String>();
			readLine(outputSet);


			printSetTest(outputSet); //HACK: test
			System.out.println("IP数:" + (outputSet.size() - 1)); //HACK: test


			JsonManager jsonMgr = new JsonManager();
			jsonMgr.createJsonFile(outputSet.size() - 1);
		}
	}


	static void readLine(HashSet<String> outputSet){
		//Scanner stdin = new Scanner(System.in);
		String dumpLine = "";
		outputSet.add(dumpLine); //空行が混じる場合があるので、先に入れておいて-1する方法

		try{
			long startTimeS = CurrentDate.getCurrentTimeMili();
			System.out.println(CurrentDate.getCurrentTimeMili() - startTimeS); //HACK: test

			while((CurrentDate.getCurrentTimeMili() - startTimeS) <= (5*60*1000)){ //5m loop
				 //標準入力にデータがある場合のみreadLineする
				//これをしなければプログラムがwaitしてしまって長時間動かなくなる
				if(System.in.available() != 0){
					dumpLine = stdin.readLine(); //bufferedReader
				}

				String ip = ipExtraction(dumpLine);
				if(isFinPacketIp(dumpLine)){
					outputSet.remove(ip);
				}else{
					outputSet.add(ip);
				}
			}

			System.out.println(CurrentDate.getCurrentTimeMili() - startTimeS);
		}catch(NoSuchElementException e){ //EOF対策
			System.out.println("-->exception: EOFである可能性が高い");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("入力受付終了--------------------");
		}
		return;
	}


	/*受け取ったdumpLineからIPアドレスだけを抽出して返す
	 */
	static String ipExtraction(String dumpLine){
		Pattern pattern = Pattern.compile("IP (.*?)\\.(.*?)\\.(.*?)\\.(.*?)\\."); //IPアドレスのポート番号
		Matcher matcher = pattern.matcher(dumpLine);

		String ip = "";
		if(matcher.find()){
			ip = dumpLine.substring(matcher.start() + 3, matcher.end() - 1);
		}
		return ip;
	}


	static boolean isFinPacketIp(String dumpLine){
		Pattern pattern = Pattern.compile("\\[F.*\\]");
		Matcher matcher = pattern.matcher(dumpLine);
		if(matcher.find()){
			System.out.println("isFinPacketIp:" + dumpLine.substring(matcher.start(), matcher.end()));
			return true;
		}
		return false;
	}


	static void printSetTest(HashSet<String> outputSet){
		for(String ip : outputSet){
			System.out.println(ip);
		}
	}

}
