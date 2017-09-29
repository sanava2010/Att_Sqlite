package com.ava.att;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.ava.att.student.StudChecklist;

import java.util.ArrayList;

//import static com.ava.att.DatabaseHelper.TABLE_NAME;


public class MainActivity extends ListActivity {

    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button);
        myDB = new DatabaseHelper(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // myDB.dlRows();
                EditText  edit = (EditText) findViewById(R.id.editText);
                list.add(edit.getText().toString());
                edit.setText("");
                adapter.notifyDataSetChanged();
            }
        };
        btn.setOnClickListener(listener);
        setListAdapter(adapter);

    }
    @Override
    public void onListItemClick(ListView listView, View itemView, int position, long id){
        String Tbname=(String)listView.getItemAtPosition(position);
        // //To be checked!!!!!!!!


        myDB.getTable(Tbname);
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
