package com.construction.android.parser;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ProjectDetailsDataParser implements Parcelable {

	public List<ProjectDetailsDataParser> getChildren() {
		return children;
	}

	public void setChildren(List<ProjectDetailsDataParser> children) {
		this.children = children;
	}

	String guid = "";
	String error = "";
	String name = "";
	String description = "";
	String activityLocation = "";
	String status = "";
	String startDate = "";
	String endDate = "";
	String makeReadyDate = "";
	String spaceConstraint = "";
	String materialConstraint = "";
	String equipmentConstraint = "";
	String manpowerConstraint = "";
	String externalConstraint = "";
	String designConstraint = "";
	String message="";
	List<NotesParser> notes;
	List<ProjectDetailsDataParser> children;
	List<PredescessorsStartParser> constrainingActivityRelations;

	public ProjectDetailsDataParser() {

	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
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

	public String getActivityLocation() {
		return activityLocation;
	}

	public void setActivityLocation(String activityLocation) {
		this.activityLocation = activityLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getMakeReadyDate() {
		return makeReadyDate;
	}

	public void setMakeReadyDate(String makeReadyDate) {
		this.makeReadyDate = makeReadyDate;
	}

	public String getSpaceConstraint() {
		return spaceConstraint;
	}

	public void setSpaceConstraint(String spaceConstraint) {
		this.spaceConstraint = spaceConstraint;
	}

	public String getMaterialConstraint() {
		return materialConstraint;
	}

	public void setMaterialConstraint(String materialConstraint) {
		this.materialConstraint = materialConstraint;
	}

	public String getEquipmentConstraint() {
		return equipmentConstraint;
	}

	public void setEquipmentConstraint(String equipmentConstraint) {
		this.equipmentConstraint = equipmentConstraint;
	}

	public String getManpowerConstraint() {
		return manpowerConstraint;
	}

	public void setManpowerConstraint(String manpowerConstraint) {
		this.manpowerConstraint = manpowerConstraint;
	}

	public String getExternalConstraint() {
		return externalConstraint;
	}

	public void setExternalConstraint(String externalConstraint) {
		this.externalConstraint = externalConstraint;
	}

	public String getDesignConstraint() {
		return designConstraint;
	}

	public void setDesignConstraint(String designConstraint) {
		this.designConstraint = designConstraint;
	}

	public List<NotesParser> getData() {
		return notes;
	}

	public void setData(List<NotesParser> data) {
		this.notes = data;
	}

	public List<PredescessorsStartParser> getConstrainingActivityRelations() {
		return constrainingActivityRelations;
	}

	public void setConstrainingActivityRelations(List<PredescessorsStartParser> constrainingActivityRelations) {
		this.constrainingActivityRelations = constrainingActivityRelations;
	}

	public ProjectDetailsDataParser(Parcel source) {
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
		dest.writeString(error);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(activityLocation);
		dest.writeString(status);
		dest.writeString(startDate);
		dest.writeString(endDate);
		dest.writeString(makeReadyDate);
		dest.writeString(spaceConstraint);
		dest.writeString(materialConstraint);
		dest.writeString(equipmentConstraint);
		dest.writeString(manpowerConstraint);
		dest.writeString(externalConstraint);
		dest.writeString(designConstraint);
		dest.writeString(message);
		dest.writeList(notes);
		dest.writeList(constrainingActivityRelations);
		dest.writeList(children);

	}

	public void readFromParcel(Parcel source) {
		guid = source.readString();
		error = source.readString();
		name = source.readString();
		description = source.readString();
		activityLocation = source.readString();
		status = source.readString();
		startDate = source.readString();
		endDate = source.readString();
		makeReadyDate = source.readString();
		spaceConstraint = source.readString();
		materialConstraint = source.readString();
		equipmentConstraint = source.readString();
		manpowerConstraint = source.readString();
		externalConstraint = source.readString();
		designConstraint = source.readString();
		message = source.readString();
		notes = new ArrayList<NotesParser>();
		source.readList(notes, null);
		constrainingActivityRelations = new ArrayList<PredescessorsStartParser>();
		source.readList(constrainingActivityRelations, null);
		children = new ArrayList<ProjectDetailsDataParser>();
		source.readList(children, null);
	}

	public final Parcelable.Creator<ProjectDetailsDataParser> CREATOR = new Parcelable.Creator<ProjectDetailsDataParser>() {

		@Override
		public ProjectDetailsDataParser createFromParcel(Parcel source) {
			return new ProjectDetailsDataParser(source);
		}

		@Override
		public ProjectDetailsDataParser[] newArray(int size) {
			return new ProjectDetailsDataParser[size];
		}
	};
}