package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class NotesParser implements Parcelable {

	String guid = "";
	String message = "";
	String timestamp = "";
	String checkSender = "";
	String senderImagePath = "";
	AutherParser author;

	

	

	public NotesParser() {

	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCheckSender() {
		return checkSender;
	}

	public void setCheckSender(String checkSender) {
		this.checkSender = checkSender;
	}

	public String getSenderImagePath() {
		return senderImagePath;
	}

	public void setSenderImagePath(String senderImagePath) {
		this.senderImagePath = senderImagePath;
	}
	
	public AutherParser getAuthor() {
		return author;
	}

	public void setAuthor(AutherParser author) {
		this.author = author;
	}
	
	public NotesParser(Parcel source) {
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
		dest.writeString(message);
		dest.writeString(timestamp);
		dest.writeString(checkSender);
		dest.writeString(senderImagePath);
		dest.writeParcelable(author, arg1);// Class
	}

	public void readFromParcel(Parcel source) {
		guid = source.readString();
		message = source.readString();
		timestamp = source.readString();
		checkSender = source.readString();
		senderImagePath = source.readString();
		author = (AutherParser) source.readParcelable(AutherParser.class.getClassLoader());
	}

	public static final Parcelable.Creator<NotesParser> CREATOR = new Parcelable.Creator<NotesParser>() {

		@Override
		public NotesParser createFromParcel(Parcel source) {
			return new NotesParser(source);
		}

		@Override
		public NotesParser[] newArray(int size) {
			return new NotesParser[size];
		}
	};

}