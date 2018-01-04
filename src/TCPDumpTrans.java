import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*TODO:timeout 29m sudo tcpdump -nl not port ssh and arp > tcpdump.txt ;  java TCPDumpTrans < tcpdump.txt �Ŏg�p����
 * java TCPDumpTrans < sudo tcpdump -nl not port ssh and arp //���O�������Ȃ��A�o�b�t�@�����O����Assh��arp�͒e���A���_�C���N�g�œ���
 *Domp���e����A���M��IP�𒊏o����
 *���o����IP��HashSet�ɒǉ�����
 *(���5���ԁA�܂��͍Ō�̍s�܂ő�����)
 *�I����HashSet.size()���擾�B
 *�ׂ������Ԃł͂Ȃ�X:00�AX:30�Ƃ������悤��30�����݂̑�܂��Ȏ����\�@�Ŕr�o����K�v����
 *(���ł�JSON������ꍇ�A���݉��s���邩���ׂ�15�s���x�ɗ}����K�v����)
 *���ԂƂƂ���size��JSON�ɏo��
 */

public class TCPDumpTrans {
	private static boolean isQuit = false;
	private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) {
		while(isQuit == false){//�펞�N��
			HashSet<String> outputSet = new HashSet<String>();
			readLine(outputSet);


			printSetTest(outputSet); //HACK: test
			System.out.println("IP��:" + (outputSet.size() - 1)); //HACK: test


			JsonManager jsonMgr = new JsonManager();
			jsonMgr.createJsonFile(outputSet.size() - 1);
		}
	}


	static void readLine(HashSet<String> outputSet){
		//Scanner stdin = new Scanner(System.in);
		String dumpLine = "";
		outputSet.add(dumpLine); //��s��������ꍇ������̂ŁA��ɓ���Ă�����-1������@

		try{
			long startTimeS = CurrentDate.getCurrentTimeMili();
			System.out.println(CurrentDate.getCurrentTimeMili() - startTimeS); //HACK: test

			while((CurrentDate.getCurrentTimeMili() - startTimeS) <= (5*60*1000)){ //5m loop
				 //�W�����͂Ƀf�[�^������ꍇ�̂�readLine����
				//��������Ȃ���΃v���O������wait���Ă��܂��Ē����ԓ����Ȃ��Ȃ�
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
		}catch(NoSuchElementException e){ //EOF�΍�
			System.out.println("-->exception: EOF�ł���\��������");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("���͎�t�I��--------------------");
		}
		return;
	}


	/*�󂯎����dumpLine����IP�A�h���X�����𒊏o���ĕԂ�
	 */
	static String ipExtraction(String dumpLine){
		Pattern pattern = Pattern.compile("IP (.*?)\\.(.*?)\\.(.*?)\\.(.*?)\\."); //IP�A�h���X�̃|�[�g�ԍ�
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
