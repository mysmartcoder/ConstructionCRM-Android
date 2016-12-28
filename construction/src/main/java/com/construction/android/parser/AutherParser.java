package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class AutherParser implements Parcelable {

	String guid = "";
	String name = "";

	

	
	public AutherParser() {

	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public AutherParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(guid);
		dest.writeString(name);
	}

	public void readFromParcel(Parcel source) {
		guid = source.readString();
		name = source.readString();
		
	}

	public static final Parcelable.Creator<AutherParser> CREATOR = new Parcelable.Creator<AutherParser>() {

		@Override
		public AutherParser createFromParcel(Parcel source) {
			return new AutherParser(source);
		}

		@Override
		public AutherParser[] newArray(int size) {
			return new AutherParser[size];
		}
	};

}