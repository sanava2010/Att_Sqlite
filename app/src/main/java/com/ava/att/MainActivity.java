package com.ava.att;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.ava.att.student.StudChecklist;

import java.util.ArrayList;

//import static com.ava.att.DatabaseHelper.TABLE_NAME;


public class MainActivity extends ListActivity {

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    View v;
    String RenTable;
    EditText editName;
    PopupWindow pw;
    //DatabaseHelper myDB;
    public static SQLiteDatabase mydatabase;
    public static String Tbname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editName =(EditText)findViewById(R.id.newClassName);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);
        //myDB = new DatabaseHelper(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        mydatabase = openOrCreateDatabase("attendance.db",MODE_PRIVATE,null);
        Cursor c=mydatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' order by name",null);
        if(c.moveToFirst())
        {
            while(!c.isAfterLast())
            {
                list.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // myDB.dlRows();
                EditText  edit = (EditText) findViewById(R.id.editText);
                list.add(edit.getText().toString());
                Tbname=edit.getText().toString();
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Tbname + " (RollNo TEXT PRIMARY KEY,Name TEXT);");
                edit.setText("");
                adapter.notifyDataSetChanged();
            }
        };
        btn.setOnClickListener(listener);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.add(Menu.NONE, 0, 0, "Rename");
        menu.add(Menu.NONE, 1, 1, "Delete");



    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        int index=info.position;
        String delTable=adapter.getItem(index);
        RenTable=adapter.getItem(index);
        switch(menuItemIndex)
        {
            case 0: displayPopupWindow();
                    //
                break;
            case 1:list.remove(index);
                    adapter.notifyDataSetChanged();
                    mydatabase.execSQL("DROP TABLE " + delTable + ";");
                    Toast.makeText(getApplicationContext(), "Deleted " +delTable, Toast.LENGTH_LONG).show();

        }
        return true;
    }
    public void displayPopupWindow()
    {
        View layout = getLayoutInflater().inflate(R.layout.popup, null);
        editName =(EditText)layout.findViewById(R.id.newClassName);
        pw = new PopupWindow(this);
        pw.setContentView(layout);
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.setFocusable(true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }


    public void doneB(View v)
    {
       // editName =(EditText)layout.findViewById(R.id.newClassName);
        String newTbName=editName.getText().toString();
        mydatabase.execSQL("ALTER TABLE " + RenTable + " RENAME TO "+newTbName+";");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id){

        //String del="delete from "+Tbname;
       // mydatabase.execSQL(del);
        Tbname=(String)listView.getItemAtPosition(position);
        //myDB.getWritableDatabase();
       // myDB.createTable(Tbname);
        Intent intent=new Intent(MainActivity.this,StudChecklist.class);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnValue = data.getStringExtra("key1");
                Toast.makeText(getApplicationContext(), "" + returnValue, Toast.LENGTH_LONG).show();
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
