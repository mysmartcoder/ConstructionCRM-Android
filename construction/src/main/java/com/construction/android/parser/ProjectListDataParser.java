package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectListDataParser implements Parcelable {

	

	

	String projectId = "";
	String guid = "";
	String name = "";
	String startDate="";
	String endDate="";
	String message="";
	List<ProjectRoleHolderDataParser> projectRoleHolderRelations;

	public ProjectListDataParser() {

	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public List<ProjectRoleHolderDataParser> getProjectRoleHolderRelations() {
		return projectRoleHolderRelations;
	}

	public void setProjectRoleHolderRelations(List<ProjectRoleHolderDataParser> projectRoleHolderRelations) {
		this.projectRoleHolderRelations = projectRoleHolderRelations;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ProjectListDataParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(projectId);
		dest.writeString(guid);
		dest.writeString(name);
		dest.writeString(startDate);
		dest.writeString(endDate);
		dest.writeString(message);
		dest.writeList(projectRoleHolderRelations);

	}

	public void readFromParcel(Parcel source) {
		projectId = source.readString();
		guid = source.readString();
		name = source.readString();
		startDate = source.readString();
		endDate = source.readString();
		message = source.readString();
		projectRoleHolderRelations = new ArrayList<ProjectRoleHolderDataParser>();
		source.readList(projectRoleHolderRelations, null);
	}

	public final Parcelable.Creator<ProjectListDataParser> CREATOR = new Parcelable.Creator<ProjectListDataParser>() {

		@Override
		public ProjectListDataParser createFromParcel(Parcel source) {
			return new ProjectListDataParser(source);
		}

		@Override
		public ProjectListDataParser[] newArray(int size) {
			return new ProjectListDataParser[size];
		}
	};
}