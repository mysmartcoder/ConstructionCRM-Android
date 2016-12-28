package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationParser implements Parcelable {

	

	

	String status = "";
	String filename = "";
	String timestamp = "";
	String eventType = "";
	String comment = "";
	String checkSender = "";
	String eventSuccessful = "";
	String failureException = "";
	String isUnReadNotification = "";
	AutherParser creatorActorDetails;
	AutherParser activityDetails;

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

	public String getEventSuccessful() {
		return eventSuccessful;
	}

	public void setEventSuccessful(String eventSuccessful) {
		this.eventSuccessful = eventSuccessful;
	}

	public String getFailureException() {
		return failureException;
	}

	public void setFailureException(String failureException) {
		this.failureException = failureException;
	}

	public AutherParser getCreatorActorDetails() {
		return creatorActorDetails;
	}

	public void setCreatorActorDetails(AutherParser creatorActorDetails) {
		this.creatorActorDetails = creatorActorDetails;
	}

	public AutherParser getActivityDetails() {
		return activityDetails;
	}

	public void setActivityDetails(AutherParser activityDetails) {
		this.activityDetails = activityDetails;
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsUnReadNotification() {
		return isUnReadNotification;
	}

	public void setIsUnReadNotification(String isUnReadNotification) {
		this.isUnReadNotification = isUnReadNotification;
	}

	public NotificationParser() {

	}

	public NotificationParser(Parcel source) {
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
		dest.writeString(eventSuccessful);
		dest.writeString(failureException);
		dest.writeString(filename);
		dest.writeString(status);
		dest.writeString(isUnReadNotification);

		dest.writeParcelable(creatorActorDetails, arg1);// Class
		dest.writeParcelable(activityDetails, arg1);// Class
	}

	public void readFromParcel(Parcel source) {
		timestamp = source.readString();
		eventType = source.readString();
		comment = source.readString();
		checkSender = source.readString();
		eventSuccessful = source.readString();
		failureException = source.readString();
		filename = source.readString();
		status = source.readString();
		isUnReadNotification = source.readString();
		creatorActorDetails = (AutherParser) source.readParcelable(AutherParser.class.getClassLoader());
		activityDetails = (AutherParser) source.readParcelable(AutherParser.class.getClassLoader());
	}

	public static final Parcelable.Creator<NotificationParser> CREATOR = new Parcelable.Creator<NotificationParser>() {

		@Override
		public NotificationParser createFromParcel(Parcel source) {
			return new NotificationParser(source);
		}

		@Override
		public NotificationParser[] newArray(int size) {
			return new NotificationParser[size];
		}
	};

}
