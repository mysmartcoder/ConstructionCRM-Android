package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryDataParser implements Parcelable {

	String timestamp = "";
	String eventType = "";
	String comment = "";
	String checkSender = "";
	String senderImagePath = "";
	AutherParser creatorActorDetails;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public AutherParser getCreatorActorDetails() {
		return creatorActorDetails;
	}

	public void setCreatorActorDetails(AutherParser creatorActorDetails) {
		this.creatorActorDetails = creatorActorDetails;
	}

	public HistoryDataParser() {

	}

	public HistoryDataParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(timestamp);
		dest.writeString(eventType);
		dest.writeString(comment);
		dest.writeString(checkSender);
		dest.writeString(senderImagePath);
		dest.writeParcelable(creatorActorDetails, arg1);// Class
	}

	public void readFromParcel(Parcel source) {
		timestamp = source.readString();
		eventType = source.readString();
		comment = source.readString();
		checkSender = source.readString();
		senderImagePath = source.readString();
		creatorActorDetails = (AutherParser) source.readParcelable(AutherParser.class.getClassLoader());
	}

	public static final Parcelable.Creator<HistoryDataParser> CREATOR = new Parcelable.Creator<HistoryDataParser>() {

		@Override
		public HistoryDataParser createFromParcel(Parcel source) {
			return new HistoryDataParser(source);
		}

		@Override
		public HistoryDataParser[] newArray(int size) {
			return new HistoryDataParser[size];
		}
	};

}