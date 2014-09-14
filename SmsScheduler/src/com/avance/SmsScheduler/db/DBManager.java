package com.avance.SmsScheduler.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.avance.SmsScheduler.model.ScheduleDO;

public class DBManager {

	private final int DATABASE_VERSION = 9;
	
	public class ScheduleDBHelper extends SQLiteOpenHelper {
        public ScheduleDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            // execute the query string to the database.
        	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("create table if not exists "
    				+ TABLE_NAME
    				+ " (id long PRIMARY KEY,contactId varchar,dateTime long,interval long,noOfTimes long,msg varchar,lastEventTime long)");
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
        	// TODO Auto-generated method stub
        	super.onOpen(db);
        	onCreate(db);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade
            // policy is to simply to discard the data and start over
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
	
	final static String DATABASE_NAME = "mydb";
	final static String TABLE_NAME = "schedule_temp";
	final static AtomicLong generator = new AtomicLong(100);
	
	SQLiteDatabase database;
	
	private static  DBManager dbManager;
	
	private DBManager(Context context){
		ScheduleDBHelper helper = new ScheduleDBHelper(context);
		this.database = helper.getWritableDatabase();
	}
	
	public static final DBManager getDBManager(Context context){
		if(DBManager.dbManager == null){
			DBManager.dbManager = new DBManager(context);
		}
		return DBManager.dbManager;
	}
	
	private  void init(){
		SQLiteDatabase database = getDataBase();
		//deleteTables();
		createTable(database);
	}
	/**
	 * creates required tables to be used in the app will be called as part of
	 * app initialization and will not recreate tables if they already exist
	 */
	private  void createTable(SQLiteDatabase database) {
		
		database.execSQL("create table if not exists "
				+ TABLE_NAME
				+ " (id long PRIMARY KEY,contactId varchar,dateTime long,interval long,noOfTimes long,msg varchar,lastEventTime long)");
	}

	/**
	 * @param context
	 * @param inputDataModel
	 *            which represents schedule to be saved
	 * @return inputDataModel with Id
	 */
	public  ScheduleDO insertInputModel(ScheduleDO inputDataModel) {
		SQLiteDatabase database = getDataBase();
		database.beginTransactionNonExclusive();
		long id = System.currentTimeMillis();
		inputDataModel = inputDataModel.getInputBuilder().setId(id).build();
		SQLiteStatement stmt = createInsertStatement(database, inputDataModel);
		stmt.execute();
		stmt.clearBindings();
		database.setTransactionSuccessful();
		database.endTransaction();
		return inputDataModel;
	}

	public  List<ScheduleDO> getAllActiveSchedules(){
		Cursor cursor = getSelectQuery( -1);
		List<ScheduleDO> inputDataModelList = new ArrayList<ScheduleDO>();
		while(cursor.moveToNext()){
			inputDataModelList.add(getInputDataModel(cursor));
		}
		return inputDataModelList;
	}
	
	private  ScheduleDO getInputDataModel(Cursor cursor){
		long id = getLongColumn(cursor, "id");
		String contactId = getStringColumn(cursor, "contactId");
		long dateTime = getLongColumn(cursor, "dateTime");
		long interval = getLongColumn(cursor, "interval");
		long noOfTimes = getLongColumn(cursor, "noOfTimes");
		String msg = getStringColumn(cursor, "msg");
		long lastEventTime = getLongColumn(cursor, "lastEventTime");
		return createInputModel(id, contactId, dateTime, interval, noOfTimes, msg,lastEventTime);
	}
	/**
	 * @param context
	 * @param id
	 *            represents scheduleId
	 * @return inputDataModel if exists for that Id returns null otherwise
	 */
	public  ScheduleDO selectInputDataModel(long id) {
		ScheduleDO dataModel = null;
		Cursor cursor = getSelectQuery( id);
		if(cursor.getCount()>0){
			cursor.moveToFirst();
		    dataModel = getInputDataModel(cursor);
		}else{
		}
		return dataModel;
	}

	/**
	 * This method currently update only noOfTimes nothing else.
	 * @param context
	 * @param inputModel
	 *            to be updated,id must be present will have no effect otherwise
	 * @return updateInputModel if id exists returns null otherwise
	 */
	public  ScheduleDO updateInputDataModel(
			ScheduleDO inputDataModel) {
		database.beginTransactionNonExclusive();
		ContentValues values = new ContentValues();
        values.put("noOfTimes", inputDataModel.getNoOfTimes());
        values.put("lastEventTime", inputDataModel.getLastEventTime());

        // ask the database object to update the database row of given rowID
        try {
        	ScheduleDO sdo = selectInputDataModel(inputDataModel.getId());
            int count = database.update(TABLE_NAME, values, " id = "+inputDataModel.getId(),null);
            printDBSnapShot("update query");
        } catch (Exception e) {
            e.printStackTrace();
        }
		database.setTransactionSuccessful();
		database.endTransaction();
		
		return inputDataModel;
	}

	/**
	 * @param context
	 * @param id
	 *            represents scheduleId
	 * @return inputDataModel if id exists returns null otherwise
	 */
	public  ScheduleDO deleteInputDataModel(long id) {
		ScheduleDO inputDataModel = selectInputDataModel(id);
		SQLiteDatabase database = getDataBase();
		String[] args = { String.valueOf(id) };
		database.delete(TABLE_NAME, "id=?", args);
		return inputDataModel;
	}
	
	public int deleteAllFromModel()
	{
		SQLiteDatabase database = getDataBase();
		return database.delete(TABLE_NAME, "1", null);
	
	}

	/**
	 * this deletes all the tables used by this app. will be called when app is
	 * uninstalled
	 */
	public  void deleteTables() {
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		SQLiteDatabase database = getDataBase();
		database.execSQL(sql);
	}
	
	
	public  SQLiteDatabase getDataBase() {
		return this.database;
	}

	private  SQLiteStatement createInsertStatement(SQLiteDatabase database,ScheduleDO inputDataModel) {
		String sql = "INSERT INTO "
				+ TABLE_NAME
				+ "(id,contactId, dateTime,interval,noOfTimes,msg,lastEventTime) VALUES (?,?, ?,?,?,?,?)";
		SQLiteStatement stmt = database.compileStatement(sql);
		stmt.bindLong(1, inputDataModel.getId());
		stmt.bindString(2,inputDataModel.getContactId());
		stmt.bindLong(3, inputDataModel.getDateTime());
		stmt.bindLong(4, inputDataModel.getInterval());
		stmt.bindLong(5, inputDataModel.getNoOfTimes());
		
		if(inputDataModel.getMessage() == null){
			stmt.bindString(6, "");
		}else{
			stmt.bindString(6, inputDataModel.getMessage());
		}
		
		stmt.bindLong(7, inputDataModel.getLastEventTime());
		return stmt;
	}

	private  void printDBSnapShot(String tag){
		Cursor cursor = database.rawQuery("select * from " + TABLE_NAME  , null);
		StringBuilder builder = new StringBuilder();
		while(cursor.moveToNext()){
			ScheduleDO scheduleDO = getInputDataModel(cursor);
			builder.append(scheduleDO.toString()).append("\n");
		}
	}
	
	private Cursor getSelectQuery(long id){
		printDBSnapShot("select query");
		Cursor cursor = null;
	  
		if(id==-1){
			cursor = database.rawQuery("select * from "+TABLE_NAME, null);
		}else{
			cursor = database.rawQuery("select * from "+ TABLE_NAME +" where id=" + id, null);
		}
		return cursor;
	}
	
	private String getStringColumn(Cursor cursor,String columnName){
		return cursor.getString(cursor.getColumnIndex(columnName));
	}
	
	private Long getLongColumn(Cursor cursor,String columnName){
		return cursor.getLong(cursor.getColumnIndex(columnName));
	}
	
	


	private static ScheduleDO createInputModel(long id, String contactId,
			long dateTime, long interval, long noOfTimes, String message,long lastEventTime) {
		ScheduleDO.InputBuilder inputModelBuiler = new ScheduleDO.InputBuilder();
		return inputModelBuiler.setId(id).setContactId(contactId).setDateTime(dateTime)
				.setInterval(interval).setNoOfTimes(noOfTimes)
				.setMessage(message).setLastEventTime(lastEventTime).build();
	}

}
