package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusData implements Parcelable {

	String status = "";
	String userid = "";
	String message = "";
	String error = "";
	String verificationcode = "";
	String token = "";

	public StatusData() {

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getVerificationcode() {
		return verificationcode;
	}

	public void setVerificationcode(String verificationcode) {
		this.verificationcode = verificationcode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public StatusData(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(status);
		dest.writeString(userid);
		dest.writeString(message);
		dest.writeString(error);
		dest.writeString(token);

	}

	public void readFromParcel(Parcel source) {
		status = source.readString();
		userid = source.readString();
		message = source.readString();
		error = source.readString();
		token = source.readString();
	}

	public final Parcelable.Creator<StatusData> CREATOR = new Parcelable.Creator<StatusData>() {

		@Override
		public StatusData createFromParcel(Parcel source) {
			return new StatusData(source);
		}

		@Override
		public StatusData[] newArray(int size) {
			return new StatusData[size];
		}
	};
}