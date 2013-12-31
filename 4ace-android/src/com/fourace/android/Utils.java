package com.fourace.android;

import java.text.ParseException;
import java.util.Date;

import com.fourace.android.Constants.PreferenceType;

public class Utils {

	public static int getDIP( int px ) {
		float scale = App.getContext().getResources().getDisplayMetrics().density;
		return ( int )( px * scale + .5f );
	}
	
	public static String[] getStringArrayResource( int id ) {
		return App.getContext().getResources().getStringArray( id );
	}
	
	public static String formatDate( Date date ) {
		return Constants.DATE_FORMAT.format( date );
	}
	
	public static String formatDateTime( Date date ) {
		return Constants.DATE_TIME_FORMAT.format( date );
	}
	
	public static Date parseDate( String dateStr ) throws ParseException {
		return Constants.DATE_FORMAT.parse( dateStr );
	}
	
	public static Date parseDateTime( String dateStr ) throws ParseException {
		return Constants.DATE_TIME_FORMAT.parse( dateStr );
	}
	
	public static String getPreferenceDefaultString( PreferenceType type ) {
		switch( type ) {
		}
		return "";
	}
	
	public static boolean getPreferenceDefaultBoolean( PreferenceType type ) {
		switch( type ) {
		}
		return false;
	}
	
	public static int getPreferenceDefaultInt( PreferenceType type ) {
		switch( type ) {
		}
		return 0;
	}
	
	public static boolean isNullOrEmpty( String str ) {
		if( ( str == null) || ( str.length() == 0 ) )
			return true;
		return false;
	}
	
}
