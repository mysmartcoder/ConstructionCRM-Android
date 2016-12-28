package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectsListParser implements Parcelable {

	List<ProjectListDataParser> data;


	public ProjectsListParser() {

	}

	public List<ProjectListDataParser> getData() {
		return data;
	}

	public void setData(List<ProjectListDataParser> data) {
		this.data = data;
	}


	public ProjectsListParser(Parcel source) {
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
	}

	public void readFromParcel(Parcel source) {
		data = new ArrayList<ProjectListDataParser>();
		source.readList(data, null);
	}

	public static final Parcelable.Creator<ProjectsListParser> CREATOR = new Parcelable.Creator<ProjectsListParser>() {

		@Override
		public ProjectsListParser createFromParcel(Parcel source) {
			return new ProjectsListParser(source);
		}

		@Override
		public ProjectsListParser[] newArray(int size) {
			return new ProjectsListParser[size];
		}
	};

}