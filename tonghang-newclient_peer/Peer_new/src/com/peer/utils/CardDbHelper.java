package com.peer.utils;

import java.util.ArrayList;

import com.peer.bean.SqlBean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 保存交换名片记录类
 * 
 * @author weisiky
 * 
 */
public class CardDbHelper {

	/** 表名 **/
	private static String TABLE_NAME = "card_table";
	/** 数据库名称 **/
	private static String DB_NAME = "card_db";
	/** 数据库版本号 **/
	private static int DB_VERSION = 1;
	/** 上下文 */
	private static Context mContext;
	/** 写数据库对象 **/
	static SQLiteDatabase sldb_w;
	/** 读数据库对象 **/
	static SQLiteDatabase sldb_r;

	/**
	 * 打开数据库
	 * 
	 * @param incontext
	 */
	private static void open() {
		openWrite();
		openRead();
	}

	/**
	 * 打开写数据库
	 */
	private static void openWrite() {
		if ((sldb_w == null) || (!sldb_w.isOpen())) {
			CardSqliteDB sq = new CardSqliteDB(mContext);
			sldb_w = sq.getWritableDatabase();
		}
	}

	/**
	 * 打开读数据库
	 */
	private static void openRead() {
		if ((sldb_r == null) || (!sldb_r.isOpen())) {
			CardSqliteDB sq = new CardSqliteDB(mContext);
			sldb_r = sq.getReadableDatabase();
		}
	}

	/**
	 * 读取所有名片的id名
	 * 
	 * @return
	 */
	public synchronized static ArrayList<SqlBean> getAllCardId(String client_id) {
		ArrayList<SqlBean> list = new ArrayList<SqlBean>();
		try {
			String Sql = "select * from " 
	    			+ TABLE_NAME+" where client_id='"+client_id+"' order by timestamp desc";
			pLog.i("test", "sql:"+Sql);
			open();
			Cursor cursor = null;
			cursor = sldb_r.rawQuery(Sql,null);
			while (cursor.moveToNext()) {
				SqlBean sqlbean = new SqlBean();
				sqlbean.setPushid(cursor.getString(cursor.getColumnIndex("pushid")));
				sqlbean.setDate(cursor.getString(cursor.getColumnIndex("timestamp")));
				sqlbean.setName(cursor.getString(cursor.getColumnIndex("name")));
				sqlbean.setType(cursor.getString(cursor.getColumnIndex("type")));
				sqlbean.setClient_id(cursor.getString(cursor.getColumnIndex("client_id")));
				sqlbean.setImage(cursor.getString(cursor.getColumnIndex("image")));
				list.add(sqlbean);
			}
			cursor.close();
		} catch (Exception e) {
			pLog.i("test", "数据库异常:"+e.toString());
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据id删除card
	 * 
	 * @param serviceId
	 * @return
	 */
	public synchronized static boolean delCard(String serviceId ,String client_id,String date) {
		String sql = "delete from " + TABLE_NAME + " where pushid='"+serviceId+"' and client_id='"+client_id+"' and timestamp='"+date+"'";
		pLog.i("test", "sql:"+sql);
		try {
			open();
			sldb_w.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 插入一条数据
	 * 
	 * @return 插入是否成功
	 */
	public synchronized static boolean insert(String pushid,String name,String type,String client_id,String image) {
		open();
		String sql = "insert into " + TABLE_NAME + " (pushid,timestamp,name,type,client_id,image)values('"+pushid+"',datetime('now'),'"+name+"','"+type+"','"+client_id+"','"+image+"')";
		pLog.i("test", "sql:"+sql);
		try {
//			sldb_w.execSQL(sql, new Object[] { pushid,name,type,client_id,image });
			sldb_w.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			pLog.i("test", "数据库异常："+e.toString());
		}
		return false;
	}

	/**
	 * 传入id查找该id是否存在
	 * 
	 * @return
	 */

	public synchronized static boolean selectcard(String pushid ,String client_id) {

		open();
		String countSql = "select * from " + TABLE_NAME + " where pushid='" + pushid + "' and client_id='"+client_id+"'";
		Cursor countCursor = sldb_r.rawQuery(countSql, null);
		if (countCursor.getCount() > 0) {
			countCursor.close();
			return true;
		}
		return false;
	}

	/**
	 * 数据库助手类
	 */
	private static class CardSqliteDB extends SQLiteOpenHelper {
		/**
		 * 构造方法
		 * 
		 * @param context
		 *            上下文对象
		 */
		public CardSqliteDB(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/**
		 * 初始化数据库
		 * 
		 * @param db
		 *            数据库对象
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			String topicSql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME 
					+ " (pushid text,timestamp DATETIME,name text,type text,client_id text,image text)";
			db.execSQL(topicSql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TABLE_NAME);
			db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME 
					+ " (pushid text,timestamp DATETIME,name text,type text,client_id text,image text)");
		}
	}

	/**
	 * 设置上下文
	 * 
	 * @param mContext
	 */
	public static void setmContext(Context mContext) {
		if (CardDbHelper.mContext == null && mContext != null) {
			CardDbHelper.mContext = mContext;
		}

	}

}
