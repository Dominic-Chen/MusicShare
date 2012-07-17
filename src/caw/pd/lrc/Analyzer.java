package caw.pd.lrc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class Analyzer {
	public static final String LRC_TEXT_RESULT = "lrc.text.result";
	public static final String LRC_TIME_RESULT = "lrc.time.result";
	// 获取歌曲信息
	// 歌曲名
	private final static String REG_SONDNAME = "\\[ti:(.+?)\\]/i";
	// 歌手
	private final static String REG_SINGER = "\\[ar:(.+?)\\]/i";
	// 专辑
	private final static String REG_ALBULM = "\\[al:(.+?)\\]/i";
	// 歌词作者
	private final static String REG_MAKER = "\\[(by:.+?)\\]/i";

	// 获取时间信息
	//private final static String REG_TAKE = "\\[\\d\\d:\\d\\d\\.\\d\\d\\].*/g";
	// 过滤时间信息的正则
	//private final static String REG_REPLACETIME = "(\\[\\d\\d:\\d\\d\\.\\d\\d\\])+/g";
	// 获取时间信息的正则
	private final static String REG_GETTIMES = "\\[\\d\\d:\\d\\d\\.\\d\\d\\]";

	//private final static String REG_READTIME = "\\[(\\d\\d):(\\d\\d\\.\\d\\d)\\]";

	public Map<String, Pattern> initPatterns() throws IllegalArgumentException, IllegalAccessException {
		Map<String, Pattern> results=new HashMap();
		
		Field[] fields= this.getClass().getDeclaredFields();
		for(Field field:fields){
			field.setAccessible(true);
			results.put(field.getName(), Pattern.compile((String)field.get(this)));
		}
		return results;
	}

	public Map<String, Queue> analyze(InputStream inputStream) {
		Map<String, Queue> reValue = new HashMap();

		Queue<Long> timeMills = new LinkedList<Long>();
		Queue<String> messages = new LinkedList<String>();

		InputStreamReader inputReader=null;
		BufferedReader bufferedReader=null;
		try {
			inputReader = new InputStreamReader(inputStream,
					"UTF-8");
			bufferedReader = new BufferedReader(inputReader);

			// Pattern[] p= getRegularPatterns();
			Map<String, Pattern> patterns = initPatterns();
			String temp;
			String result = "";

			while ((temp = bufferedReader.readLine()) != null) {
				Set<Entry<String, Pattern>> entries = patterns.entrySet();
				for (Entry<String, Pattern> entry : entries) {
					Matcher matcher = entry.getValue().matcher(temp);
					if (matcher.find()) {
						//result = "" + process(matcher, temp, null) + "\n";
						if (!"".equals(result)) {
							messages.add(result);
						}
						String timeStr = matcher.group();
						Long timeMill = time2Long(timeStr.substring(1,
								timeStr.length() - 1));
						timeMills.offer(timeMill);
						String msg = temp.substring(temp.lastIndexOf("]") + 1);
						
						result = "" + msg + "\n";
					}
				}
				//result = result + temp + "\n";
			}
			
			messages.add(result);
			reValue.put(LRC_TIME_RESULT,timeMills);
			reValue.put(LRC_TEXT_RESULT,messages);
		} catch (Exception e) {
			Log.d("exception", e.getMessage());
		}finally{
			if(inputReader!=null){
				try {
					inputReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bufferedReader!=null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return reValue;
	}

	/**
	 * �����ӣ���ȫ��ת��Ϊ����
	 * 
	 * @param timeStr
	 * @return
	 */
	public Long time2Long(String timeStr) {
		Long reValue=0L;
		if(timeStr.contains(":")){
			String s[] = timeStr.split(":");
			int min = Integer.parseInt(s[0]);
			String ss[] = s[1].split("\\.");
			int sec = Integer.parseInt(ss[0]);
			int mill = Integer.parseInt(ss[1]);
			reValue=min * 60 * 1000 + sec * 1000 + mill * 10L;
		}
		return reValue;
	}
}
