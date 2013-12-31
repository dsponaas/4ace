package com.fourace.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeGen {
	
	private static final String OUTPUT_PATH = "_OUTPUT/";

	public static void performAllCodeGen() {
		generateJavaCode( "NewsItem", newsFields, true );
	}
	
	private static void generateJavaCode( String className, String[][] fieldArray, boolean hasPrimaryKey ) {
        StringBuilder builder = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        builder.append( "	//**************************************************************************************************************************************************\n" );
        builder.append( "	//**************************************************************************************************************************************************\n" );
        builder.append( "	//****** BEGIN AUTOGENERATED CODE - " );
        builder.append( format.format( new Date() ) );
        builder.append( "*********************************************************************************************\n" );
        builder.append( "	//**************************************************************************************************************************************************\n" );
        builder.append( "	//**************************************************************************************************************************************************\n" );

        builder.append( "\n" );
        builder.append( "    protected static final String DATABASE_TABLE_NAME = \"dbo_" );
        builder.append( className );
        builder.append( "\";\n" );
        builder.append( "\n" );

        builder = generateConstructor( className, builder, fieldArray );
        builder = generateFields( builder, fieldArray );
        builder = generateTableDefinition( builder, fieldArray, hasPrimaryKey );
        builder = generateCreateContentValues( builder, fieldArray );
        builder = generateLoadFromCursor( className, builder, fieldArray );
        builder = generateConvertToJson( className, builder, fieldArray );

        builder.append( "	//**************************************************************************************************************************************************\n" );
        builder.append( "	//**************************************************************************************************************************************************\n" );
        builder.append( "	//****** END AUTOGENERATED CODE ********************************************************************************************************************\n" );
        builder.append( "	//**************************************************************************************************************************************************\n" );
        builder.append( "	//**************************************************************************************************************************************************\n" );

		StringBuilder pathBuilder = new StringBuilder( OUTPUT_PATH );
		pathBuilder.append( className );
		pathBuilder.append( "_java.txt" );
        
        writeToFile( builder.toString(), pathBuilder.toString() );
    }

    private static StringBuilder generateConstructor( String className, StringBuilder builder, String[][] fieldArray ) {
        builder.append( "	public " );
        builder.append( className );
        builder.append( "() {\n" );

        for( String[] row : fieldArray ) {
            if( row[ 4 ] != "1" ) {
                builder.append( "		" );
                builder.append( row[ 0 ] );
                builder.append( " = " );
                if( ( row[ 2 ] == "int" ) || ( row[ 2 ] == "Integer" ) )
                    builder.append( "0;\n" );
                else if( row[ 2 ] == "boolean" )
                    builder.append( "false;\n" );
                else if( row[ 2 ] == "String" )
                    builder.append( "\"\";\n" );
                else if( row[ 2 ] == "Date" )
                    builder.append( "new Date();\n" );
                else if( row[ 2 ] == "float" )
                    builder.append( "0f;\n" );
            }
        }
        builder.append( "	}\n" );
        builder.append( "\n" );

        return builder;
    }

    private static StringBuilder generateFields( StringBuilder builder, String[][] fieldArray ) {
        for( String[] row : fieldArray ) {
            builder.append( "	private " );
            builder.append( row[ 2 ] );
            builder.append( " " );
            builder.append( row[ 0 ] );
            builder.append( ";\n" );

            builder.append( "	public " );
            builder.append( row[ 2 ] );
            builder.append( " get" );
            builder.append( row[ 1 ] );
            builder.append( "()                { return " );
            builder.append( row[ 0 ] );
            builder.append( "; }\n" );

            builder.append( "	public void set" );
            builder.append( row[ 1 ] );
            builder.append( "( " );
            builder.append( row[ 2 ] );
            builder.append( " val )      { " );
            builder.append( row[ 0 ] );
            builder.append( " = val; }\n\n" );
        }
        return builder;
    }

    private static StringBuilder generateTableDefinition( StringBuilder builder, String[][] fieldArray, boolean hasPrimaryKey ) {
        builder.append( "	protected static String getTableDefinition() {\n" );
        builder.append( "		StringBuilder tableBuilder = new StringBuilder();\n" );
        builder.append( "		tableBuilder.append( \"CREATE TABLE \" );\n" );
        builder.append( "		tableBuilder.append( DATABASE_TABLE_NAME );\n" );
        builder.append( "		tableBuilder.append( \"( \" );\n" );
        for( int i = 0; i < fieldArray.length; ++i ) {
        	String[] row = fieldArray[ i ];
            builder.append( "		tableBuilder.append( \"" );
            builder.append( row[ 0 ].substring( 1, row[ 0 ].length() ) );
            builder.append( " " );
            builder.append( row[ 3 ] );
            if( hasPrimaryKey && ( i == 0 ) )
                builder.append( " PRIMARY KEY" );
            if( row[ 4 ] == "false" )
                builder.append( " NOT NULL" );
            if( ( i + 1 ) < fieldArray.length )
                builder.append( "," );
            builder.append( "\" );\n" );
        }
        builder.append( "		tableBuilder.append( \" );\" );\n" );
        builder.append( "		return tableBuilder.toString();\n" );
        builder.append( "	}\n" );
        builder.append( "\n" );

        return builder;
    }

    private static StringBuilder generateCreateContentValues( StringBuilder builder, String[][] fieldArray ) {
        builder.append( "	private ContentValues createContentValues() {\n" );
        builder.append( "		ContentValues cv = new ContentValues();\n" );
        builder.append( "\n" );
        for( String[] row : fieldArray ) {
            builder.append( "		cv.put( \"" );
            builder.append( row[ 0 ].substring( 1, row[ 0 ].length() ) );
            builder.append( "\", this.get" );
            builder.append( row[ 1 ] );
            builder.append( "()" );
            if( row[ 3 ] == "date" ) {
                builder.append( " == null ? null : Utils.formatDate( this.get" );
                builder.append( row[ 1 ] );
                builder.append( "() )" );
            }
            if( row[ 3 ] == "datetime" ) {
                builder.append( " == null ? null : Utils.formatDateTime( this.get" );
                builder.append( row[ 1 ] );
                builder.append( "() )" );
            }
            builder.append( " );\n" );
        }
        builder.append( "\n" );
        builder.append( "		return cv;\n" );
        builder.append( "	}\n" );
        builder.append( "\n" );
        return builder;
    }

    private static StringBuilder generateLoadFromCursor( String className, StringBuilder builder, String[][] fieldArray ) {
        builder.append( "	private static " );
        builder.append( className );
        builder.append( " loadFromCursor( Cursor cursor ) {\n" );
        builder.append( "\n" );
        builder.append( "		try {\n" );
        builder.append( "			" );
        builder.append( className );
        builder.append( " retval = new " );
        builder.append( className );
        builder.append( "();\n" );
        builder.append( "			int i = 0;\n" );
        for( String[] row : fieldArray ) {
            builder.append( "			retval.set" );
            builder.append( row[ 1 ] );
            builder.append( "( " );
            if( row[ 4 ] == "1" )
                builder.append( "cursor.isNull( i ) ? null : " );
            if( ( row[ 2 ] == "int" ) || ( row[ 2 ] == "Integer" ) )
                builder.append( "cursor.getInt( i ) );\n" );
            else if( row[ 2 ] == "String" )
                builder.append( "cursor.getString( i ) );\n" );
            else if( row[ 3 ] == "date" )
                builder.append( "Utils.parseDate( cursor.getString( i ) ) );\n" );
            else if( row[ 3 ] == "datetime" )
                builder.append( "Utils.parseDateTime( cursor.getString( i ) ) );\n" );
            else if( row[ 3 ] == "boolean" )
                builder.append( "Integer.parseInt( cursor.getString( i ) ) == 1 );\n" );
            else if( row[ 3 ] == "real" )
                builder.append( "cursor.getFloat( i ) );\n" );
            builder.append( "			++i;\n" );
        }
        builder.append( "\n" );
        builder.append( "			return retval;\n" );
        builder.append( "		}\n" );
        builder.append( "		catch ( Exception exception ) {\n" );
        builder.append( "			EventLog.log( LogLevel.ERROR, \"" );
        builder.append( className );
        builder.append( "\", \"loadFromCursor\", \"Unhandled exception loading from cursor\", exception );\n" );
        builder.append( "		}\n" );
        builder.append( "		return null;\n" );
        builder.append( "	}\n" );
        builder.append( "\n" );
        return builder;
    }

    private static StringBuilder generateConvertToJson( String className, StringBuilder builder, String[][] fieldArray ) {
        boolean shortCircuit = true;
        for( String[] row : fieldArray ) {
            if( row[ 5 ] == "1" ) {
                shortCircuit = false;
                break;
            }
        }
        if( shortCircuit )
            return builder;

        builder.append( "	public JSONObject toJSONObject() {\n" );
        builder.append( "    	JSONObject retval = new JSONObject();\n" );
        builder.append( "    	try {\n" );

        for( String[] row : fieldArray ) {
            if( row[ 5 ] == "1" ) {
                if( row[ 2 ] == "String" ) {
                    builder.append( "    		if( !Utils.isNullOrEmpty( " );
                    builder.append( row[ 0 ] );
                    builder.append( " ) ) " );
                }
                else if( row[ 4 ] == "1" ) {
                    builder.append( "    		if( null != " );
                    builder.append( row[ 0 ] );
                    builder.append( " ) " );
                }
                else
                    builder.append( "    		" );
                builder.append( "retval.put( \"" );
                builder.append( row[ 1 ] );
                builder.append( "\", " );
                if( "date" == row[ 3 ] ) {
                    builder.append( "Utils.formatDate( " );
                    builder.append( row[ 0 ] );
                    builder.append( " )" );
                }
                else if( "datetime" == row[ 3 ] ) {
                    builder.append( "Utils.formatDateTime( " );
                    builder.append( row[ 0 ] );
                    builder.append( " )" );
                }
                else
                    builder.append( row[ 0 ] );
                builder.append( " );\n" );
            }
        }
        builder.append( "    	}\n" );
        builder.append( "    	catch( Exception exception ) {\n" );
        builder.append( "			EventLog.log( LogLevel.ERROR, \"" );
        builder.append( className );
        builder.append( "\", \"toJSONObject\", \"Unhandled exception converting object to JSON\", exception );\n" );
        builder.append( "    	}\n" );
        builder.append( "    	return retval;\n" );
        builder.append( "    }\n" );
        builder.append( "\n" );
        return builder;
    }
	
	private static void writeToFile( String val, String filename ) {
		Writer writer = null;
		try {
		    writer = new BufferedWriter( new OutputStreamWriter(
		          new FileOutputStream( filename ), "utf-8" ) );
		    writer.write( val );
		} catch (IOException ex) {}
		finally {
		   try {
			   writer.close();
		   } catch( Exception ex ) {}
		}
	}

	//*****************************************************************************************************************************************************************************
    //*****************************************************************************************************************************************************************************
    //***** [ 0 ]field name *******************************************************************************************************************************************************
    //***** [ 1 ]uppercase name ***************************************************************************************************************************************************
    //***** [ 2 ]java type ********************************************************************************************************************************************************
    //***** [ 3 ]sqlite type ******************************************************************************************************************************************************
    //***** [ 4 ] is nullable? ****************************************************************************************************************************************************
    //******[ 5 ]convert to json? *************************************************************************************************************************************************
    //*****************************************************************************************************************************************************************************
    //*****************************************************************************************************************************************************************************
	private static final String[][] newsFields = new String[][] {
		{ "_newsItemID", "NewsItemID", "int", "integer", "false", "false" },
		{ "_title", "Title", "String", "varchar(32)", "false", "false" },
		{ "_content", "Content", "String", "varchar(1024)", "false", "false" },
		{ "_timestamp", "Timestamp", "Date", "datetime", "false", "true" }
	};
	
}