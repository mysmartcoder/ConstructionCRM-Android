package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class AuthenticateParser implements Parcelable {

	String userId = "";
	String username = "";
	String accountNonExpired = "";
	String accountNonLocked = "";
	String credentialsNonExpired = "";
	String enabled = "";



	String message = "";

	public AuthenticateParser() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(String accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public String getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(String accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public String getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(String credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public AuthenticateParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(userId);
		dest.writeString(username);
		dest.writeString(accountNonExpired);
		dest.writeString(accountNonLocked);
		dest.writeString(enabled);
		dest.writeString(message);

	}

	public void readFromParcel(Parcel source) {
		userId = source.readString();
		username = source.readString();
		accountNonExpired = source.readString();
		accountNonLocked = source.readString();
		enabled = source.readString();
		message = source.readString();
	}

	public final Parcelable.Creator<AuthenticateParser> CREATOR = new Parcelable.Creator<AuthenticateParser>() {

		@Override
		public AuthenticateParser createFromParcel(Parcel source) {
			return new AuthenticateParser(source);
		}

		@Override
		public AuthenticateParser[] newArray(int size) {
			return new AuthenticateParser[size];
		}
	};
}