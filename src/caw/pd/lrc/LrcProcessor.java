package caw.pd.lrc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcProcessor {
	//获取时间信息
	private final static String REG_TAKE ="\\[\\d\\d:\\d\\d\\.\\d\\d\\].*/g";                    
	//过滤时间信息的正则
	private final static String REG_REPLACETIME ="(\\[\\d\\d:\\d\\d\\.\\d\\d\\])+/g";
	//获取时间信息的正则
	private final static String REG_GETTIMES ="\\[\\d\\d:\\d\\d\\.\\d\\d\\]";
	
	private final static String REG_READTIME ="\\[(\\d\\d):(\\d\\d\\.\\d\\d)\\]";

	//获取偏移值
	private final static String REG_OFFSET ="\\[(offset:.+?)\\]/i";
	
	/**
	 * �����ʣ��Ѹ�ʺ�ʱ���Ӧ�ŵ�ArrayList<Queue>��
	 * @param inputStream
	 * @return
	 */
	public ArrayList<Queue> process(InputStream inputStream){
		//���ʱ������
		Queue<Long> timeMills = new LinkedList<Long>();  
		//���ʱ����Ӧ�ĸ��
		Queue<String> messages = new LinkedList<String>();
		//����ʱ��͸�ʴ�ŵ�һ��
		ArrayList<Queue> queues = new ArrayList<Queue>();
		try{
			//����BufferedReader����
			InputStreamReader inputReader = new InputStreamReader(inputStream,"UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputReader);
			String temp = null;
			
			int i = 0;
			//����һ��������ʽ
			//Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
			Pattern p= Pattern.compile(REG_GETTIMES);
			String result = null;
			boolean b= true;
			while((temp = bufferedReader.readLine()) != null){
				i ++;
				Matcher m = p.matcher(temp);
				if(m.find()){
					
					if(result != null){
						messages.add(result);
					}
					String timeStr = m.group();
					Long  timeMill = time2Long(timeStr.substring(1, timeStr.length() - 1));
					if(b){
						timeMills.offer(timeMill);
					}
					String msg = temp.substring(10);
					result = "" + msg + "\n";
				}else{
					result = result + temp + "\n";
				}
			}
			messages.add(result);
			queues.add(timeMills);
			queues.add(messages);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return queues;
	}
	
	/**
	 * �����ӣ���ȫ��ת��Ϊ����
	 * @param timeStr
	 * @return
	 */
	public Long  time2Long(String timeStr){
		String s[] = timeStr.split(":");
		int min = Integer.parseInt(s[0]);
		String ss[] = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mill = Integer.parseInt(ss[1]);
		return min * 60 * 1000 + sec * 1000 + mill * 10L;
	}
}
