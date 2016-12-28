package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactInfoParser implements Parcelable {

	String guid = "";
	String emailAddress = "";
	String postalAddress = "";
	String phoneNumbers = "";
	String websites = "";
	String imaccounts = "";

	
	public ContactInfoParser() {

	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}


	public String getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getWebsites() {
		return websites;
	}

	public void setWebsites(String websites) {
		this.websites = websites;
	}

	public String getImaccounts() {
		return imaccounts;
	}

	public void setImaccounts(String imaccounts) {
		this.imaccounts = imaccounts;
	}
	
	public ContactInfoParser(Parcel source) {
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
		dest.writeString(emailAddress);
		dest.writeString(postalAddress);
		dest.writeString(phoneNumbers);
		dest.writeString(websites);
		dest.writeString(imaccounts);
	}

	public void readFromParcel(Parcel source) {
		guid = source.readString();
		emailAddress = source.readString();
		postalAddress = source.readString();
		phoneNumbers = source.readString();
		websites = source.readString();
		imaccounts = source.readString();
		
		
	}

	public static final Parcelable.Creator<ContactInfoParser> CREATOR = new Parcelable.Creator<ContactInfoParser>() {

		@Override
		public ContactInfoParser createFromParcel(Parcel source) {
			return new ContactInfoParser(source);
		}

		@Override
		public ContactInfoParser[] newArray(int size) {
			return new ContactInfoParser[size];
		}
	};

}