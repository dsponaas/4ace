package com.fourace.android;

import java.text.SimpleDateFormat;

public class Constants {

	public static final String DATABASE_NAME = "4ACE_ANDROID_DB";
	public static final int DATABASE_VERSION = 1;
	
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat( "yyyyMMdd" );
	protected static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat( "yyyyMMddHHmmss" );

	public enum PreferenceType {
	}
	
	public enum LogLevel {
		INFO,
		WARNING,
		ERROR
	}
	
}
