package com.lqzxc.db;

import com.lqzxc.modal.BikeStie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库助手
 * @author QQ472950043
 **/
public class LqzxcDB extends SQLiteOpenHelper {
	
	public static String dbname="LqzxcDB";
	public static int version=1;
	SQLiteDatabase db;
	String sql;

	public LqzxcDB(Context context) {
		this(context, dbname, null, version);//context, DATABASE_NAME, null, DATABASE_VERSION
		// TODO Auto-generated constructor stub
	}
	
	public LqzxcDB(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);//context, DATABASE_NAME, null, DATABASE_VERSION
		// TODO Auto-generated constructor stub
	}

	//首次创建的时候使用，创建好不能更改表结构，除非升级
	@Override
	public void onCreate(SQLiteDatabase db2) {
		// TODO Auto-generated method stub
		try {
			sql="create table lqzxc(_id integer primary key autoincrement," +
				"mid text," +
				"gpsx text," +
				"gpsy text," +
				"netName text," +
				"netType text," +
				"netStatus text," +
				"netLevel text," +
				"address text," +
				"trafficInfo text," +
				"bicycleCapacity text," +
				"bicycleNum text," +
				"imageAttach text," +
				"visited integer," +// default 0
				"time text)";
			db2.execSQL(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 添加数据
	public void addBike(BikeStie modal) throws Exception {
		db = this.getWritableDatabase();
		sql = "insert into lqzxc(mid,gpsx,gpsy,netName,netType," +
							"netStatus,netLevel,address,trafficInfo,bicycleCapacity," +
							"bicycleNum,imageAttach,visited,time) " +
							"values(?,?,?,?,?," +
							"?,?,?,?,?," +
							"?,?,?,?)";
		db.execSQL(sql, new String[] {
			modal.getMid(),
			modal.getGpsx(),
			modal.getGpsy(),
			modal.getNetName(),
			modal.getNetType(),
			modal.getNetStatus(),
			modal.getNetLevel(),
			modal.getAddress(),
			modal.getTrafficInfo(),
			""+modal.getBicycleCapacity(),
			""+modal.getBicycleNum(),
			modal.getImageAttach(),
			""+modal.getVisited(),
			modal.getTime()
		});
	}

	// 更新数据
	public void updateVisited(BikeStie modal) throws Exception {
		db = this.getWritableDatabase();
		sql = "update lqzxc set " +
				"visited='"+modal.getVisited()+"', " +
				"time='"+modal.getTime()+"' " +
				"where mid='"+modal.getMid()+"'";
		db.execSQL(sql);
	}
	
	// 更新数据
	public void updateBike(BikeStie modal) throws Exception {
		db = this.getWritableDatabase();
		sql = "update lqzxc set " +
				"netStatus='"+modal.getNetStatus()+"', " +
				"bicycleCapacity='"+modal.getBicycleCapacity()+"', " +
				"bicycleNum='"+modal.getBicycleNum()+"' " +
				"where mid='"+modal.getMid()+"'";
		db.execSQL(sql);
	}
		
	// 查询数据
	public Cursor selectBike() {
		db = this.getReadableDatabase();
		sql = "select * from lqzxc";
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}
	
	// 删除表中数据
	public void deleteBike() {
		db = this.getWritableDatabase();
		db.delete("lqzxc", null, null);
	}
		
	//升级，一般用不到
	@Override
	public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		sql = "DROP TABLE IF EXISTS lqzxc";
        db2.execSQL(sql);
        onCreate(db2);
	}

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}
}
