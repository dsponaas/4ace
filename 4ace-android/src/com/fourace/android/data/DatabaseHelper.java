package com.fourace.android.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fourace.android.App;
import com.fourace.android.Constants;
import com.fourace.android.Constants.LogLevel;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static DatabaseHelper _instance = null;
	
	private DatabaseHelper( Context context ) {
		super( context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION );  
	}
	
	public static void initialize() {
		if ( _instance == null ) {
			_instance = new DatabaseHelper( App.getContext() );
		}
	}
	
	public static void reinitialize() {
		if( null != _instance ) {
			_instance.close();
			_instance = null;
		}
		initialize();
	}
	
	protected static DatabaseHelper getInstance() {
		return _instance;
	}
	
	@Override
	public void onCreate( SQLiteDatabase db ) {
		db.execSQL( ErrorLog.getTableDefinition() );
		db.execSQL( NewsItem.getTableDefinition() );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		db.execSQL( getDropTableString( ErrorLog.DATABASE_TABLE_NAME ) );
		db.execSQL( getDropTableString( NewsItem.DATABASE_TABLE_NAME ) );
		onCreate( db );
	}
	
	private static String getDropTableString( String tableName ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "DROP TABLE IF EXISTS " );
		sb.append( tableName );
		return sb.toString();
	}
	
	protected void insertRecord( String tableName, ContentValues cv ) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.insert( tableName, null, cv );
		}
		catch( Exception exception ) {
			ErrorLog.log( LogLevel.ERROR, "DatabaseHelper", "insertRecord", "Unhandled exception while inserting record into database", exception );
		}
	}
	
	protected int updateRecord( String tableName, String whereClause, String[] values, ContentValues cv ) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			return db.update( tableName, cv, whereClause, values );
		}
		catch( Exception exception ) {
			ErrorLog.log( LogLevel.ERROR, "DatabaseHelper", "updateRecord", "Unhandled exception while updating database record", exception );
			return -1;
		}
	}
	
	protected Cursor query( String tableName, String whereClause, String[] whereClauseValues, String sortby ) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.query( tableName, null, whereClause, whereClauseValues, null, null, sortby );
	}

	protected Cursor query( String tableName, String whereClause, String[] whereClauseValues ) {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.query( tableName, null, whereClause, whereClauseValues, null, null, null );
	}
	
	protected int delete( String tableName, String whereClause, String[] whereClauseValues ) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			return db.delete(tableName, whereClause, whereClauseValues);
		}
		catch( Exception exception ) {
			ErrorLog.log( LogLevel.ERROR, "DatabaseHelper", "delete", "Unhandled exception while deleting database record", exception );
			return -1;
		}
	}
	
	protected String[] queryDistinct( String tableName, String columnName, String sortBy ) {
		return queryDistinct( tableName, columnName, null, null, sortBy );
	}
	
	protected String[] queryDistinct( String tableName, String columnName, String whereClause, String[] whereClauseValues, String sortBy ) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query( true, tableName, new String[]{ columnName }, whereClause, whereClauseValues, null, null, sortBy, null );
		
		String[] retval = new String[ cursor.getCount() ];
		int counter = 0;
		try {
			while( cursor.moveToNext() ) {
				String str = cursor.getString( 0 );
				retval[ counter ] = str;
				++counter;
			}
		}
		catch( Exception exception ) {
			ErrorLog.log( LogLevel.ERROR, "DatabaseHelper", "queryDistinct(String,String,String,String[],String)", "Unhandled exception while querying database", exception );
		}
		
		cursor.close();
		return retval;
	}

}