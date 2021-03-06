package com.fourace.android.data;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.fourace.android.Constants.LogLevel;
import com.fourace.android.Utils;

public class ErrorLog {
	
	public static void log( LogLevel logLevel, String className, String methodName, String message ) {
		// TODO: implement me
	}
	
	public static void log( LogLevel logLevel, String className, String methodName, String message, Throwable exception ) {
		// TODO: implement me
	}
	
	//**************************************************************************************************************************************************
	//**************************************************************************************************************************************************
	//****** BEGIN AUTOGENERATED CODE - 2013-12-30 21:49:46*********************************************************************************************
	//**************************************************************************************************************************************************
	//**************************************************************************************************************************************************

    protected static final String DATABASE_TABLE_NAME = "dbo_ErrorLog";

	public ErrorLog() {
		_logLevel = 0;
		_sourceClass = "";
		_sourceMethod = "";
		_message = "";
		_timestamp = new Date();
		_dirty = false;
	}

	private int _logLevel;
	public int getLogLevel()                { return _logLevel; }
	public void setLogLevel( int val )      { _logLevel = val; }

	private String _sourceClass;
	public String getSourceClass()                { return _sourceClass; }
	public void setSourceClass( String val )      { _sourceClass = val; }

	private String _sourceMethod;
	public String getSourceMethod()                { return _sourceMethod; }
	public void setSourceMethod( String val )      { _sourceMethod = val; }

	private String _message;
	public String getMessage()                { return _message; }
	public void setMessage( String val )      { _message = val; }

	private Date _timestamp;
	public Date getTimestamp()                { return _timestamp; }
	public void setTimestamp( Date val )      { _timestamp = val; }

	private boolean _dirty;
	public boolean getDirty()                { return _dirty; }
	public void setDirty( boolean val )      { _dirty = val; }

	protected static String getTableDefinition() {
		StringBuilder tableBuilder = new StringBuilder();
		tableBuilder.append( "CREATE TABLE " );
		tableBuilder.append( DATABASE_TABLE_NAME );
		tableBuilder.append( "( " );
		tableBuilder.append( "logLevel integer NOT NULL," );
		tableBuilder.append( "sourceClass varchar(32)," );
		tableBuilder.append( "sourceMethod varchar(64)," );
		tableBuilder.append( "message varchar(256)," );
		tableBuilder.append( "timestamp datetime NOT NULL," );
		tableBuilder.append( "dirty boolean NOT NULL" );
		tableBuilder.append( " );" );
		return tableBuilder.toString();
	}

	private ContentValues createContentValues() {
		ContentValues cv = new ContentValues();

		cv.put( "logLevel", this.getLogLevel() );
		cv.put( "sourceClass", this.getSourceClass() );
		cv.put( "sourceMethod", this.getSourceMethod() );
		cv.put( "message", this.getMessage() );
		cv.put( "timestamp", this.getTimestamp() == null ? null : Utils.formatDateTime( this.getTimestamp() ) );
		cv.put( "dirty", this.getDirty() );

		return cv;
	}

	private static ErrorLog loadFromCursor( Cursor cursor ) {

		try {
			ErrorLog retval = new ErrorLog();
			int i = 0;
			retval.setLogLevel( cursor.getInt( i ) );
			++i;
			retval.setSourceClass( cursor.getString( i ) );
			++i;
			retval.setSourceMethod( cursor.getString( i ) );
			++i;
			retval.setMessage( cursor.getString( i ) );
			++i;
			retval.setTimestamp( Utils.parseDateTime( cursor.getString( i ) ) );
			++i;
			retval.setDirty( Integer.parseInt( cursor.getString( i ) ) == 1 );
			++i;

			return retval;
		}
		catch ( Exception exception ) {
			ErrorLog.log( LogLevel.ERROR, "ErrorLog", "loadFromCursor", "Unhandled exception loading from cursor", exception );
		}
		return null;
	}

	//**************************************************************************************************************************************************
	//**************************************************************************************************************************************************
	//****** END AUTOGENERATED CODE ********************************************************************************************************************
	//**************************************************************************************************************************************************
	//**************************************************************************************************************************************************
}
