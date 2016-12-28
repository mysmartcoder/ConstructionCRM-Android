package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDetailParser implements Parcelable {

	String guid = "";
	

	String name="";
	String salutation="";
	String givenName="";
	String familyName="";
	String message="";
	ContactInfoParser contactInfo;
	

	public UserDetailParser() {

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

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public ContactInfoParser getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfoParser contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public UserDetailParser(Parcel source) {
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
		dest.writeString(name);
		dest.writeString(salutation);
		dest.writeString(givenName);
		dest.writeString(familyName);
		dest.writeString(message);
		dest.writeParcelable(contactInfo, arg1);// Class
	}

	public void readFromParcel(Parcel source) {
		guid = source.readString();
		name = source.readString();
		salutation = source.readString();
		givenName = source.readString();
		familyName = source.readString();
		message = source.readString();
		contactInfo = (ContactInfoParser) source.readParcelable(ContactInfoParser.class.getClassLoader());
	}

	public static final Parcelable.Creator<UserDetailParser> CREATOR = new Parcelable.Creator<UserDetailParser>() {

		@Override
		public UserDetailParser createFromParcel(Parcel source) {
			return new UserDetailParser(source);
		}

		@Override
		public UserDetailParser[] newArray(int size) {
			return new UserDetailParser[size];
		}
	};

}