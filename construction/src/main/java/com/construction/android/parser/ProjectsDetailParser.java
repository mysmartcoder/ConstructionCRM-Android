package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectsDetailParser implements Parcelable {




	String message="";
	List<ProjectDetailsDataParser> data;


	public ProjectsDetailParser() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ProjectDetailsDataParser> getData() {
		return data;
	}

	public void setData(List<ProjectDetailsDataParser> data) {
		this.data = data;
	}


	public ProjectsDetailParser(Parcel source) {
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
		data = new ArrayList<ProjectDetailsDataParser>();
		source.readList(data, null);
	}

	public static final Parcelable.Creator<ProjectsDetailParser> CREATOR = new Parcelable.Creator<ProjectsDetailParser>() {

		@Override
		public ProjectsDetailParser createFromParcel(Parcel source) {
			return new ProjectsDetailParser(source);
		}

		@Override
		public ProjectsDetailParser[] newArray(int size) {
			return new ProjectsDetailParser[size];
		}
	};

}