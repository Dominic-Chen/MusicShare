package caw.pd.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Mp3Info implements Serializable,Parcelable{
	private static final long serialVersionUID = 1L;
	private int position;
	private String id;
	private String fullName;
	private String mp3Name;
	private String mp3Size;
	private String artist;
	private String location;
	private LrcInfo lrc;
	
	public static final Parcelable.Creator<Mp3Info> CREATOR = new Parcelable.Creator<Mp3Info>() {   
        @Override
		public Mp3Info createFromParcel(Parcel in) {  
        	Mp3Info p = new Mp3Info();   
             p.id=in.readString();   
             p.fullName=in.readString();    
             p.mp3Name=in.readString();
             p.mp3Size=in.readString();
             p.artist=in.readString();
             p.position=in.readInt();
             p.location=in.readString();
             //p.lrc=(LrcInfo) in.readValue(LrcInfo.class.getClassLoader());
             return p;   
        }   
  
        @Override
		public Mp3Info[] newArray(int size) {   
            return new Mp3Info[size];   
        }   
    }; 
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMp3Name() {
		return mp3Name;
	}
	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}
	public String getMp3Size() {
		return mp3Size;
	}
	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public LrcInfo getLrc() {
		return lrc;
	}
	public void setLrc(LrcInfo lrc) {
		this.lrc = lrc;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(fullName);
		dest.writeString(mp3Name);
		dest.writeString(mp3Size);
		dest.writeString(artist);
		dest.writeInt(position);
		dest.writeString(location);
	}
}
