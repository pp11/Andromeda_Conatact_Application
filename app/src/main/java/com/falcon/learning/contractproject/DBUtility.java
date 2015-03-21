package com.falcon.learning.contractproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by PC21 on 03/21/15.
 */
public class DBUtility  {
    SQLiteDatabase db;
    Person person;

    public void CreateOrOpenDatabase(Context context){
        db=context.openOrCreateDatabase("ContactDB", Context.MODE_PRIVATE, null);

    }

    public void CreateTable(Context context){
        CreateOrOpenDatabase(context);
        db.execSQL("CREATE TABLE IF NOT EXISTS Person(name VARCHAR);");
    }
    public void insert(Person person) {

        db.execSQL("INSERT INTO Person VALUES('"+ person.getName()+"'"
                +");");
    }

    public ArrayList<Person> getPersonList(Context context){
        ArrayList<Person> persons =new ArrayList<Person>();

        Cursor c=db.rawQuery("SELECT * FROM Person", null);
        if(c.getCount()==0)
        {
          //  showMessage("Error", "No records found");
            return persons;
        }
         while(c.moveToNext())
        {
            Person person1=new Person();
            person1.setName(c.getString(0));

            persons.add(person1);
        }
      return persons;
    }
}
