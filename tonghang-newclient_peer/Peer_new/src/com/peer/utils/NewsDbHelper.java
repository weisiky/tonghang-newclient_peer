package com.peer.utils;


import com.peer.base.Constant;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * news数据库帮助类
 * 已废弃
 * @author zhangpeng
 * @version 1.0.0
 */
public class NewsDbHelper {
    /** 数据库名称 **/
    private static String DB_NAME = "pushCarddata.db";
    /** 推送信息表 **/
    private static String TABLE_PUSH = "pushCardTable";
//    /** 异常日志表**/
//    private static String TABLE_EXCEPTION_LOG = "exceptionlogtable";
    /** 数据库版本号 **/
    private static int DB_VERSION = 1;
    /** 上下文 */
    private static Context mContext;
    /** 写数据库对象 **/
    static SQLiteDatabase sldb_w;
    /** 读数据库对象 **/
    static SQLiteDatabase sldb_r;

    /** 共享文件工具类 **/
	public static pShareFileUtils mShareFileUtils = new pShareFileUtils();
	
	
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
            NewsSqliteDB sq = new NewsSqliteDB(mContext);
            sldb_w = sq.getWritableDatabase();
        }
    }

    /**
     * 打开读数据库
     */
    private static void openRead() {
        if ((sldb_r == null) || (!sldb_r.isOpen())) {
            NewsSqliteDB sq = new NewsSqliteDB(mContext);
            sldb_r = sq.getReadableDatabase();
        }
    }

    /**
     * 读取推送的个数
     * 
     * @return 推送个数
     */
    public synchronized static int getPushCardSize() {
        String Sql = "select * from " 
        		+ TABLE_PUSH;
        open();
        Cursor cursor = sldb_r.rawQuery(Sql, null);
        int CardSize = cursor.getCount();
        cursor.close();
        return CardSize;
    }
    
    
    /**
     * 读取推送的记录
     * 
     * @return cursor对象
     */
    public synchronized static Cursor getPushCard() {
    	String Sql = "select * from " 
    			+ TABLE_PUSH;
    	open();
    	Cursor cursor = sldb_r.rawQuery(Sql, null);
//    	cursor.close();
    	return cursor;
    }

    /**
     * 插入一条最新推送数据
     * 
     * @param pushid
     *            推送id
     * @return 插入是否成功
     */
    public synchronized static boolean insertPushCard(String reqpushId,String respushId) {
//        open();
        String sql = "insert into " 
        		+ TABLE_PUSH 
        		+ " (reqpushid,respushid,date)values(?,?,date('now'))";
//        String sql = "insert into " + TABLE_PUSH + " (pushid)values(?)";
        try {
            sldb_w.execSQL(sql, new Object[] {reqpushId,respushId});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除所有push的消息
     * @return 
     */
    public synchronized static boolean deleteAllPushNews(){
    	try {
			open();
			sldb_w.execSQL("delete from "
					+TABLE_PUSH);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }
    
    
    /**
     * 删除一条reqpush的消息
     * @return 
     */
    public synchronized static boolean deleteReqPush(String reqpushId){
    	try {
    		String Sql = "delete from "
    				+TABLE_PUSH
    				+" where reqpushid =?";
    		open();
    		sldb_w.execSQL(Sql,new Object[] {reqpushId});
    		return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    
    /**
     * 删除一条respush的消息
     * @return 
     */
    public synchronized static boolean deleteResPush(String respushId){
    	try {
    		String Sql = "delete from "
    				+TABLE_PUSH
    				+" where respushid =?";
//    		open();
    		sldb_w.execSQL(Sql,new Object[] {respushId});
    		return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }

    /**
     * 根据pushid读取推送信息
     * 暂时用不到
     * @param pushid
     * @return
     */
    public synchronized static boolean getPushMsgById(String pushid) {
        String Sql = "select * from " 
        		+ TABLE_PUSH 
        		+ " where pushid =?";
        open();
        Cursor cursor = sldb_r.rawQuery(Sql, new String[] { pushid });
        boolean newsSize = cursor.moveToNext();
        cursor.close();
        return newsSize;
    }


//
//    /**
//     * 添加异常日志
//     * @param netType  网络类型
//     * @param buzType  业务类型
//     * @param expType  异常类型
//     * @return
//     */
//    public synchronized static boolean insertExpLog(String netType,String buzType,String expType,String expContent){
//    	String sql = "insert into " + TABLE_EXCEPTION_LOG + " (nettype,buztype,exptype,content)values(?,?,?,?)";
//        try {
//            open();
//            sldb_w.execSQL(sql, new Object[] { netType, buzType, expType,expContent });
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//    /**
//     * 读取异常日志个数
//     * @return
//     */
//    public synchronized static int getExpLogCount(){
//    	String Sql = "select * from " + TABLE_EXCEPTION_LOG;
//        open();
//        Cursor cursor = sldb_r.rawQuery(Sql, null);
//        int logSize = cursor.getCount();
//        cursor.close();
//        return logSize;
//    }
//    /**
//     * 读取所有异常信息
//     * @return
//     */
//    public synchronized static ArrayList<ExceptionLogBean> getAllLogException(){
//    	ArrayList<ExceptionLogBean> list = new ArrayList<ExceptionLogBean>();
//        try {
//            open();
//            Cursor cursor = null;
//            cursor = sldb_r.query(TABLE_EXCEPTION_LOG, null,null, null, null, null, null);
//            while (cursor.moveToNext()) {
//            	ExceptionLogBean exceptionLogBean = new ExceptionLogBean();
//            	exceptionLogBean.netType = cursor.getString(0);
//            	exceptionLogBean.buzType = cursor.getString(1);
//            	exceptionLogBean.expType = cursor.getString(2);
//            	exceptionLogBean.expContent = cursor.getString(3);
//                list.add(exceptionLogBean);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//    /**
//     * 删除所有异常信息
//     */
//    public synchronized static void delAllExceptionLog(){
//    	open();
//    	sldb_w.delete(TABLE_EXCEPTION_LOG,null, null);
//    }
    
    
    
    
    protected void finalize() throws Throwable {
        if ((sldb_r != null) && (sldb_r.isOpen()))
            sldb_r.close();
        if ((sldb_w != null) && (sldb_w.isOpen()))
            sldb_w.close();
        super.finalize();
    }

    /**
     * 数据库助手类
     */
    private static class NewsSqliteDB extends SQLiteOpenHelper {
        /**
         * 构造方法
         * 
         * @param context
         *            上下文对象
         */
        public NewsSqliteDB(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mShareFileUtils.initSharePre(context,
    				Constant.SHARE_NAME, 0);
        }

        /**
         * 初始化数据库
         * 
         * @param db
         *            数据库对象
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            /** 创推送表 */
            String pushSql = "CREATE TABLE IF NOT EXISTS " 
            		+ TABLE_PUSH
                    + " (pushid text,date date,type text,client_id text)";
            db.execSQL(pushSql);
//            /** 创推异常日志表 */
//            String exceptionLogSql = "CREATE TABLE IF NOT EXISTS " + TABLE_EXCEPTION_LOG
//                    + " (nettype text,buztype text,exptype text,content text)";
//            db.execSQL(exceptionLogSql);
        }

        
        /**
         * 暂时用不到
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            /** 推送表 */
            db.execSQL("drop table if exists " + TABLE_PUSH);
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PUSH
            		+ "(reqpushid text,respushid text,date date)");
            
//            /** 异常日志表 */
//            db.execSQL("drop table if exists " + TABLE_EXCEPTION_LOG);
//            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXCEPTION_LOG
//            		+ "(nettype text,buztype text,exptype text,content text)");
        }
    }

    /**
     * 设置上下文
     * 
     * @param mContext
     */
    public static void setmContext(Context mContext) {
        if (NewsDbHelper.mContext == null && mContext != null) {
            NewsDbHelper.mContext = mContext;
        }

    }
}
