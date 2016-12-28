package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectsParser implements Parcelable {

	
//	ProjectsListParser data_object;
	String message="";



	List<ProjectListDataParser> data;

	public ProjectsParser() {

	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

//	public ProjectsListParser getData_object() {
//		return data_object;
//	}
//
//	public void setData_object(ProjectsListParser data_object) {
//		this.data_object = data_object;
//	}

	public List<ProjectListDataParser> getData() {
		return data;
	}

	public void setData(List<ProjectListDataParser> data) {
		this.data = data;
	}
	
	public ProjectsParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
//		dest.writeParcelable(data_object, arg1);// Class
		dest.writeString(message);
		dest.writeList(data);
	}

	public void readFromParcel(Parcel source) {
		message = source.readString();
//		data_object = (ProjectsListParser) source.readParcelable(ProjectsListParser.class.getClassLoader());
		data = new ArrayList<ProjectListDataParser>();
		source.readList(data, null);
	}

	public static final Parcelable.Creator<ProjectsParser> CREATOR = new Parcelable.Creator<ProjectsParser>() {

		@Override
		public ProjectsParser createFromParcel(Parcel source) {
			return new ProjectsParser(source);
		}

		@Override
		public ProjectsParser[] newArray(int size) {
			return new ProjectsParser[size];
		}
	};

}