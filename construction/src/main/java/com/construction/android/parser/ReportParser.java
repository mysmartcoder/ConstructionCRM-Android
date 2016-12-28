package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class ReportParser implements Parcelable {

	String spaceConstraint = "";
	String materialConstraint = "";
	String equipmentConstraint = "";
	String manpowerConstraint = "";
	String externalConstraint = "";
	String designConstraint = "";
	String total = "";
	String message="";

	public ReportParser() {

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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReportParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(spaceConstraint);
		dest.writeString(materialConstraint);
		dest.writeString(equipmentConstraint);
		dest.writeString(manpowerConstraint);
		dest.writeString(externalConstraint);
		dest.writeString(designConstraint);
		dest.writeString(total);
		dest.writeString(message);

	}

	public void readFromParcel(Parcel source) {
		spaceConstraint = source.readString();
		materialConstraint = source.readString();
		equipmentConstraint = source.readString();
		manpowerConstraint = source.readString();
		externalConstraint = source.readString();
		designConstraint = source.readString();
		total = source.readString();
		message= source.readString();
	}

	public final Parcelable.Creator<ReportParser> CREATOR = new Parcelable.Creator<ReportParser>() {

		@Override
		public ReportParser createFromParcel(Parcel source) {
			return new ReportParser(source);
		}

		@Override
		public ReportParser[] newArray(int size) {
			return new ReportParser[size];
		}
	};
}