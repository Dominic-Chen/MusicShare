package caw.pd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LrcInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4346848058024570385L;
	private String lrcName;
	private String lrcSize;
	private String title;//歌曲名  
    private String singer;//演唱者  
    private String album;//专辑  
	private Map<Long,String> infos;
	
	public String getLrcName() {
		return lrcName;
	}
	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}
	public String getLrcSize() {
		return lrcSize;
	}
	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}
	public Map<Long, String> getInfos() {
		return infos;
	}
	public void setInfos(Map<Long, String> infos) {
		this.infos = infos;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	
}
