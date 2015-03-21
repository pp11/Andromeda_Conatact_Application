package com.falcon.learning.contractproject;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by PC21 on 03/20/15.
 */
public class PhoneContactFragment extends Fragment  implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    DBUtility dbUtility;
    ListView lvPersons, lvNewPersonList;
    ArrayAdapter<Person> adapter;
    ArrayAdapter<Person> newPersonAdapter;
    ArrayList<Person> phonePersonList;
    ArrayList<Person> databasePersonsList;
    ArrayList<Person> newPersonList;
    Button btnYes, btnNo;
    TextView tvMsg;

    boolean agreeSyncing = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.phone_contact_fragment, null);
        init(view);
        return  view;
    }

//    public void showMessage(String title,String message)
//    {
//        Builder builder=new Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.show();
//    }

    private void syncPhoneContact(){
        dbUtility.CreateOrOpenDatabase(getActivity());
        for (Person newPerson : newPersonList){
            dbUtility.insert(newPerson);
                    }
    }

    private void init(View view) {
        dbUtility=new DBUtility();
        dbUtility.CreateTable(getActivity());
        btnYes=(Button) view.findViewById(R.id.btnYes);
        btnNo=(Button) view.findViewById(R.id.btnNo);

        tvMsg=(TextView) view.findViewById(R.id.tvMsg);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            syncPhoneContact();
                Toast.makeText(getActivity(), "Contacts Synchronized successfully", Toast.LENGTH_LONG).show();

                setVisibilityOfSyncAsk(false);

                databasePersonsList=dbUtility.getPersonList(getActivity());
                adapter.clear();
                adapter.addAll(databasePersonsList);

            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You have chosen not to sync!", Toast.LENGTH_LONG).show();
                agreeSyncing = false;
                setVisibilityOfSyncAsk(false);

            }
        });
        lvPersons=(ListView) view.findViewById(R.id.lvPersons);
        lvNewPersonList=(ListView) view.findViewById(R.id.lvNewPersonList);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        databasePersonsList=dbUtility.getPersonList(getActivity());
        newPersonList=new ArrayList<Person>();
        adapter =new ArrayAdapter<Person>(getActivity(), android.R.layout.simple_list_item_1, databasePersonsList);

        lvPersons.setAdapter(adapter);

        setVisibilityOfSyncAsk(false);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        dbUtility.CreateOrOpenDatabase(getActivity());

        databasePersonsList=dbUtility.getPersonList(getActivity());
        phonePersonList=new ArrayList<Person>();
        newPersonList =new ArrayList<Person>();

        cursor.moveToFirst();
        StringBuilder res = new StringBuilder();
        while (!cursor.isAfterLast()) {
            res.append("\n" + cursor.getString(21) + "-" + cursor.getString(22));
            Person person=new Person();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
           // Double number = cursor.getDouble(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            person.setName(name);
            phonePersonList.add(person);

//            for (Person dbperson : databasePersonsList){
//                if(dbperson.getName()==null){
//                    dbUtility.insert(person);
//                    cursor.moveToNext();
//                }
//            }


         //   dbUtility.insert(person);


           cursor.moveToNext();
        }


        for (Person phonePerson :  phonePersonList) {
            boolean match=false;
            for (Person databasePerson : databasePersonsList){
                if (phonePerson.getName().equals(databasePerson.getName())){
                    match=true;
                    break;
                }
            }
            if(match==false){
                Person newPerson=new Person();
                newPerson.setName(phonePerson.getName());
                newPersonList.add(newPerson);
            }
        }

        if (newPersonList.size()>0){
            newPersonAdapter =new ArrayAdapter<Person>(getActivity(), android.R.layout.simple_list_item_1, newPersonList);
            lvNewPersonList.setAdapter(newPersonAdapter);


            if (agreeSyncing == false)
            {
                //user has decided not to sync
                setVisibilityOfSyncAsk(false);
            }
            //otherwise always show
            setVisibilityOfSyncAsk(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.btnNo:
//                agreeSyncing = false;
//                setVisibilityOfSyncAsk(false);
//                break;
//            case R.id.btnYes:
//
//                break;
//        }
//    }


    // If new person list is not aval then hide lvperson and msg+button
    private void setVisibilityOfSyncAsk(boolean visible) {
        if (visible) {

            //set visible
            lvNewPersonList.setVisibility(View.VISIBLE);
            btnNo.setVisibility(View.VISIBLE);
            btnYes.setVisibility(View.VISIBLE);
            tvMsg.setVisibility(View.VISIBLE);

            //set person list invisible
            lvPersons.setVisibility(View.GONE);
        }
        else {
            //set invisible
            lvNewPersonList.setVisibility(View.GONE);
            btnNo.setVisibility(View.GONE);
            btnYes.setVisibility(View.GONE);
            tvMsg.setVisibility(View.GONE);

            //set person list visible
            lvPersons.setVisibility(View.VISIBLE);
        }
    }
}
