package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryParser implements Parcelable {



	String message="";
	List<HistoryDataParser> data;


	public HistoryParser() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<HistoryDataParser> getData() {
		return data;
	}

	public void setData(List<HistoryDataParser> data) {
		this.data = data;
	}


	public HistoryParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeList(data);
		dest.writeString(message);
	}

	public void readFromParcel(Parcel source) {
		data = new ArrayList<HistoryDataParser>();
		source.readList(data, null);
		message = source.readString();
	}

	public static final Parcelable.Creator<HistoryParser> CREATOR = new Parcelable.Creator<HistoryParser>() {

		@Override
		public HistoryParser createFromParcel(Parcel source) {
			return new HistoryParser(source);
		}

		@Override
		public HistoryParser[] newArray(int size) {
			return new HistoryParser[size];
		}
	};

}