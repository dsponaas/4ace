package com.fourace.android;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.fourace.android.Constants.LogLevel;
import com.fourace.android.data.DatabaseHelper;
import com.fourace.android.data.ErrorLog;

public class App extends Application {

	private static Context _context;
	private UncaughtExceptionHandler _defaultUncaughtExceptionHandler;
	
	public App() {
		_defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler( _customUncaughtExceptionHandler );
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		_context = getApplicationContext();
		DatabaseHelper.initialize();
		this.startService( new Intent( this, AppService.class ) );
	}
	
	public static Context getContext() {
		return _context;
	}
	
	private Thread.UncaughtExceptionHandler _customUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
	    @Override
	    public void uncaughtException( Thread thread, Throwable exception ) {
	    	ErrorLog.log( LogLevel.ERROR, "App", "uncaughtException", "UNCAUGHT EXCEPTION", exception );
	        _defaultUncaughtExceptionHandler.uncaughtException( thread, exception );
	    }
	};
	
}
