package com.example.himanshudhanwant.uds;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dataHelper extends SQLiteOpenHelper {

    private static final String db_name="foodPoint";
    private static final String table_name="cart";
    private static final int ver=2;


    public dataHelper(Context context) {
        super(context, db_name, null, ver);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL("create table if not exists "+table_name+" (id int, item text, cost int, quantity int, merchant text)");
    }

    public Cursor getData(){

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+table_name+"", null );
        return res;
    }

    public boolean find(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        boolean ans=false;
        Cursor res =  db.rawQuery("select * from " + table_name + " where id='"+id+"'", null);
        res.moveToFirst();
        if(res.isAfterLast()==false){
            ans=true;
        }
        return ans;
    }

    public void setId(String item, int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("update "+table_name+" set id="+id+" where item='"+item+"'");
    }

    public void update(int id, String item, int co, int q, String m){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("update "+table_name+" set cost="+co+",quantity="+q+",item='"+item+"',merchant='"+m+"' where id="+id);
    }

    public void insert(int id, String s, int c, int q, String m){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("insert into "+table_name+"(id,item,cost,quantity,merchant)"+" values("+id+",'"+s+"',"+c+","+q+",'"+m+"')");
    }

    public void delete_all(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("Delete from "+table_name+"");
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table_name);
        return numRows;
    }

    public void delete2(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("Delete from "+table_name+" where id="+id);
    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        arg0.execSQL("drop table if exists "+table_name+"");
        onCreate(arg0);
    }
}