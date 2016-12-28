package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class FilesParser implements Parcelable {



	String message="";
	List<String> data;


	public FilesParser() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}


	public FilesParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(message);
		dest.writeList(data);
	}

	public void readFromParcel(Parcel source) {
		message = source.readString();
		data = new ArrayList<String>();
		source.readList(data, null);
	}

	public static final Parcelable.Creator<FilesParser> CREATOR = new Parcelable.Creator<FilesParser>() {

		@Override
		public FilesParser createFromParcel(Parcel source) {
			return new FilesParser(source);
		}

		@Override
		public FilesParser[] newArray(int size) {
			return new FilesParser[size];
		}
	};

}