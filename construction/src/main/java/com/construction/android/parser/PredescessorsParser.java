package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class PredescessorsParser implements Parcelable {

	String name = "";
	String description = "";
	String startDate = "";
	String endDate = "";
	String status = "";

	public PredescessorsParser() {

	}

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public PredescessorsParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(startDate);
		dest.writeString(endDate);
		dest.writeString(status);
	}

	public void readFromParcel(Parcel source) {
		name = source.readString();
		description = source.readString();
		startDate = source.readString();
		endDate = source.readString();
		status = source.readString();
	}

	public static final Parcelable.Creator<PredescessorsParser> CREATOR = new Parcelable.Creator<PredescessorsParser>() {

		@Override
		public PredescessorsParser createFromParcel(Parcel source) {
			return new PredescessorsParser(source);
		}

		@Override
		public PredescessorsParser[] newArray(int size) {
			return new PredescessorsParser[size];
		}
	};

}