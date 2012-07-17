package caw.pd.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import caw.pd.model.LrcInfo;
import caw.pd.model.Mp3Info;

public class FileUtils {
	private final String rootPath = "file:///sdcard/";
	private String SDCardRoot;
	private Map<Integer,String> dirs;
	private int level=0;

	public FileUtils() {
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		dirs=new HashMap();
	}

	/**
	 * ��SD���ϴ����ļ�
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName, String dir) throws IOException {
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		System.out.println("file-->" + file);
		file.createNewFile();
		return file;
	}

	/**
	 * ��SD���ϴ���Ŀ¼
	 */
	public File createSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir + File.separator);
		System.out.println("create dir " + dirFile.mkdir());
		return dirFile;
	}

	/**
	 * �ж�SD���ϵ��ļ����Ƿ����
	 */
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * ��һ��InputSream��������д�뵽SD����
	 */
	public File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = (input.read(buffer))) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * ��ȡĿ¼��mp3�ļ������ֺʹ�С
	 */
	public List<Mp3Info> getMp3Files(String path,boolean isRecurse,List<String> types) {
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		File file = new File(SDCardRoot + File.separator + path);
		File[] files = file.listFiles();
		
		if (files!=null&&files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				if(isRecurse&&files[i].isDirectory()){
					mp3Infos.addAll(getMp3Files(path+"/"+files[i].getName(),isRecurse,types));
				}
				
				String name=files[i].getName();
				String type=name.substring(name.lastIndexOf(".")+1,name.length());
				if (types.contains(type)) {
					mp3Infos.add(createMp3Info(i,files[i]));
				}
			}
		}else{
			System.out.println("No files");
		}
		return mp3Infos;
	}

	private Mp3Info createMp3Info(int i,File file) {
		Mp3Info mp3Info = new Mp3Info();
		String[] tmpInfo=file.getName().split("_");
		mp3Info.setFullName(file.getName());
		mp3Info.setMp3Name(tmpInfo[0]);
		mp3Info.setMp3Size(file.length() + "");
		//mp3Info.setPosition(i);
		mp3Info.setLocation(file.getParentFile().getPath().replace(SDCardRoot, rootPath));
		if(tmpInfo.length>1){
			mp3Info.setArtist(tmpInfo[1].substring(0, tmpInfo[1].indexOf(".")));
		}else{
			mp3Info.setArtist("");
		}
		String temp[] = mp3Info.getFullName().split("\\.");
		String eLrcName = temp[0] + ".lrc";
		if (isFileExist(eLrcName, "duomi/lyric")) {
			LrcInfo lrcInfo = new LrcInfo();
			lrcInfo.setLrcName(eLrcName);
			mp3Info.setLrc(lrcInfo);
		}
		return mp3Info;
	}
	
	/** 
     * 获取图片地址列表 
     * @param file 
     * @return 
     */  
    private ArrayList<String> imagePath(File file) {  
        ArrayList<String> list = new ArrayList<String>();  
  
        File[] files = file.listFiles();  
        for (File f : files) {  
        	if(f.isDirectory()){
        		list.addAll(imagePath(f));
			}
        	if(f.getAbsolutePath().endsWith(".jpg")){
        		list.add(f.getAbsolutePath());  
        	}
        }  
        Collections.sort(list);  
        return list;  
    }  
	
	public Map<String,Bitmap> buildImgThum(String path) throws FileNotFoundException {  
		ArrayList<String> paths = new ArrayList<String>(); 
        File baseFile = new File(path);  
        // 使用TreeMap，排序问题就不需要纠结了  
        Map<String,Bitmap> maps = new TreeMap<String, Bitmap>();  
        if (baseFile != null && baseFile.exists()) {  
            paths = imagePath(baseFile);  
  
            if (!paths.isEmpty()) {  
                for (int i = 0; i < paths.size(); i++) {  
                     BitmapFactory.Options options = new BitmapFactory.Options();  
                     options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false  
                     Bitmap bitmap =BitmapFactory.decodeFile(paths.get(i),options);  
                     options.inJustDecodeBounds = false;  
                     int be = options.outHeight/40;  
                     if (be <= 0) {  
                         be = 10;  
                     }  
                     options.inSampleSize = be;  
                     bitmap = BitmapFactory.decodeFile(paths.get(i),options);  
                     maps.put(paths.get(i), bitmap);  
                }  
            }  
        }  
  
        return maps;  
    }  
}
