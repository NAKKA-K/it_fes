import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class CurrentDate {
	public static String getCurrentDayTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dataFormat = new SimpleDateFormat("MM/dd");
		int hour = cal.get(cal.HOUR_OF_DAY);
		int minutes = cal.get(cal.MINUTE);

		String date = dataFormat.format(cal.getTime());
		//細かい時間を表示させないために設定し直している
		if(minutes <= 15){
			minutes = 0;
		}else if(minutes > 15 && minutes < 45){
			minutes = 30;
		}else if(minutes >= 45){
			minutes = 0;
			hour += 1;
		}

		Formatter formatToString = new Formatter();
		formatToString.format("%s %02d:%02d", date, hour, minutes);

		return formatToString.toString();
	}

	public static float getCurrentTimeSecond(){
		return System.currentTimeMillis()*1000;
	}

}
