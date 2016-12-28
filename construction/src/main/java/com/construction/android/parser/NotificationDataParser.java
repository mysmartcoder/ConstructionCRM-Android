package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationDataParser implements Parcelable {

	String notificationsAllowed = "";
	String showNotificationFromAll = "";
	String showNotificationFromMeOnly = "";
	String showActivityStatusChange = "";
	String showActivityDateChange = "";
	String showActivityNotesChange = "";
	String showWhenReferred = "";
	String message = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	
	

	public NotificationDataParser() {

	}

	public String getNotificationsAllowed() {
		return notificationsAllowed;
	}




	public void setNotificationsAllowed(String notificationsAllowed) {
		this.notificationsAllowed = notificationsAllowed;
	}




	public String getShowNotificationFromAll() {
		return showNotificationFromAll;
	}




	public void setShowNotificationFromAll(String showNotificationFromAll) {
		this.showNotificationFromAll = showNotificationFromAll;
	}




	public String getShowNotificationFromMeOnly() {
		return showNotificationFromMeOnly;
	}




	public void setShowNotificationFromMeOnly(String showNotificationFromMeOnly) {
		this.showNotificationFromMeOnly = showNotificationFromMeOnly;
	}




	public String getShowActivityStatusChange() {
		return showActivityStatusChange;
	}




	public void setShowActivityStatusChange(String showActivityStatusChange) {
		this.showActivityStatusChange = showActivityStatusChange;
	}




	public String getShowActivityDateChange() {
		return showActivityDateChange;
	}




	public void setShowActivityDateChange(String showActivityDateChange) {
		this.showActivityDateChange = showActivityDateChange;
	}




	public String getShowActivityNotesChange() {
		return showActivityNotesChange;
	}




	public void setShowActivityNotesChange(String showActivityNotesChange) {
		this.showActivityNotesChange = showActivityNotesChange;
	}




	public String getShowWhenReferred() {
		return showWhenReferred;
	}




	public void setShowWhenReferred(String showWhenReferred) {
		this.showWhenReferred = showWhenReferred;
	}


	public NotificationDataParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(message);
		dest.writeString(notificationsAllowed);
		dest.writeString(showNotificationFromAll);
			dest.writeString(showNotificationFromMeOnly);
		dest.writeString(showActivityStatusChange);
		dest.writeString(showActivityDateChange);
		dest.writeString(showActivityNotesChange);
		dest.writeString(showWhenReferred);

	}

	public void readFromParcel(Parcel source) {
		notificationsAllowed = source.readString();
		showNotificationFromAll = source.readString();
		showNotificationFromMeOnly = source.readString();
		showActivityStatusChange = source.readString();
		showActivityDateChange = source.readString();
		showActivityNotesChange = source.readString();
		showWhenReferred = source.readString();
		message = source.readString();
	}

	public final Parcelable.Creator<NotificationDataParser> CREATOR = new Parcelable.Creator<NotificationDataParser>() {

		@Override
		public NotificationDataParser createFromParcel(Parcel source) {
			return new NotificationDataParser(source);
		}

		@Override
		public NotificationDataParser[] newArray(int size) {
			return new NotificationDataParser[size];
		}
	};
}