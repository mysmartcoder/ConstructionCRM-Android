package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectRoleHolderDataParser implements Parcelable {

	String guid = "";
	AutherParser end;

	public ProjectRoleHolderDataParser() {

	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public AutherParser getEnd() {
		return end;
	}

	public void setEnd(AutherParser end) {
		this.end = end;
	}

	public ProjectRoleHolderDataParser(Parcel source) {
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
		dest.writeParcelable(end, arg1);// Class
	}

	public void readFromParcel(Parcel source) {

		guid = source.readString();
		end = (AutherParser) source.readParcelable(AutherParser.class.getClassLoader());
	}

	public final Parcelable.Creator<ProjectRoleHolderDataParser> CREATOR = new Parcelable.Creator<ProjectRoleHolderDataParser>() {

		@Override
		public ProjectRoleHolderDataParser createFromParcel(Parcel source) {
			return new ProjectRoleHolderDataParser(source);
		}

		@Override
		public ProjectRoleHolderDataParser[] newArray(int size) {
			return new ProjectRoleHolderDataParser[size];
		}
	};
}