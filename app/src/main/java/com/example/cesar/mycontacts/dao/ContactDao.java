package com.example.cesar.mycontacts.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactDao {
    private DBHelper helper;
    private SQLiteDatabase database;
    private final String tableName="contact";

    public ContactDao(Context context) {
        helper=new DBHelper(context);
    }
    private SQLiteDatabase getDatabase(){
        if(database==null){
            database = helper.getWritableDatabase();
        }
        return database;
    }

    public long addContact(Map<String,String> al){
        getDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("nombres",al.get("nombres"));
        contentValues.put("apellidos",al.get("apellidos"));
        contentValues.put("numero",al.get("numero"));
        contentValues.put("email",al.get("email"));
        if(al.get("idcontact")!=null){
            return database.update(tableName,contentValues,"idcontact=?",new String[]{al.get("idcontact")});
        }
        return database.insert(tableName,null,contentValues);
    }

    public boolean deleteContact(String id){
        return getDatabase().delete(tableName,"idcontact=?",new String[]{id})>0;
    }

    public ArrayList<Map<String,String>> listAlumnos(){
        getDatabase();
        Cursor cursor=database.rawQuery("select * from contact",null);
        ArrayList<Map<String,String>> contacts=new ArrayList<>();
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    Map<String,String> contact=new HashMap<>();
                    contact.put("idcontact",cursor.getString(cursor.getColumnIndex("idcontact")));
                    contact.put("nombrescompletos",cursor.getString(cursor.getColumnIndex("nombres"))+" "+
                            cursor.getString(cursor.getColumnIndex("apellidos")));
                    contact.put("nombres",cursor.getString(cursor.getColumnIndex("nombres")));
                    contact.put("apellidos",cursor.getString(cursor.getColumnIndex("apellidos")));
                    contact.put("numero",cursor.getString(cursor.getColumnIndex("numero")));
                    contact.put("email",cursor.getString(cursor.getColumnIndex("email")));
                    contacts.add(contact);
                }while(cursor.moveToNext());
            }
        }
        return contacts;
    }
    public void cerrarDB(){
        helper.close();
        database = null;
    }
}
