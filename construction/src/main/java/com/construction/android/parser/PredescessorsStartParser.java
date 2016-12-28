package com.construction.android.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class PredescessorsStartParser implements Parcelable {

	PredescessorsParser start;
	

	public PredescessorsParser getStart() {
		return start;
	}


	public void setStart(PredescessorsParser start) {
		this.start = start;
	}


	public PredescessorsStartParser() {

	}


	public PredescessorsStartParser(Parcel source) {
		readFromParcel(source);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeParcelable(start, arg1);
	}

	public void readFromParcel(Parcel source) {
		start = (PredescessorsParser) source.readParcelable(PredescessorsParser.class.getClassLoader());
	}

	public static final Parcelable.Creator<PredescessorsStartParser> CREATOR = new Parcelable.Creator<PredescessorsStartParser>() {

		@Override
		public PredescessorsStartParser createFromParcel(Parcel source) {
			return new PredescessorsStartParser(source);
		}

		@Override
		public PredescessorsStartParser[] newArray(int size) {
			return new PredescessorsStartParser[size];
		}
	};

}