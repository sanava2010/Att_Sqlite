package com.ava.att.student;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.Toast;

//import com.ava.att.DatabaseHelper;
import com.ava.att.MainActivity;
import com.ava.att.R;
import com.ava.att.importxls;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.R.attr.name;

public class StudChecklist extends AppCompatActivity {
    public static ArrayList<Student> StudList=new ArrayList<>();
    ArrayList<CheckBox>arrayCheckBox=new ArrayList<>();
     LinearLayout CheckBoxContainer;
    Button bSave;
    CheckBox cb;
    //DatabaseHelper myDB=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       ScrollView sv = new ScrollView(this);
        //ll.setOrientation(LinearLayout.VERTICAL);
        //sv.addView(CheckBoxContainer);
        bSave=(Button) findViewById(R.id.next_button);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_checklist);
        CheckBoxContainer =(LinearLayout)findViewById(R.id.checkbox_container);

        //sv.addView(bSave);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_importExcel) {

            Intent i4 = new Intent(StudChecklist.this, importxls.class);
            startActivityForResult(i4,1);
        }
        else
        {
            if(id==R.id.action_Export)
            {
                //ToDo
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        CheckBoxContainer.removeAllViews();
        Cursor dataFromTable =getData();
        if(dataFromTable.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }else{
            dataFromTable.moveToFirst();
            for (int i = 0; i < dataFromTable.getCount(); i++)
            {
                cb=new CheckBox(this);
                //cb.setId(i);
                cb.setText(dataFromTable.getString(0)+dataFromTable.getString(1));
               // Toast.makeText(this, ""+dataFromTable.getDouble(0)+dataFromTable.getString(1), Toast.LENGTH_SHORT).show();
                arrayCheckBox.add(cb);
                CheckBoxContainer.addView(cb);
                dataFromTable.moveToNext();
            }

            /*while(dataFromTable.moveToNext()){
                cb=new CheckBox(this);
                //cb.setId(j);
                cb.setText(dataFromTable.getDouble(0)+dataFromTable.getString(1));
                arrayCheckBox.add(cb);
                CheckBoxContainer.addView(cb);

            }*/
            dataFromTable.close();
        }
    }
    public void save(View v)
    {
        Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date time=cal.getTime();
        DateFormat localTime=new SimpleDateFormat("HH:mm");
        localTime.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String today2=localTime.format(time);
        String replaceString2=today2.replaceAll(":","_");


        Date today1= Calendar.getInstance().getTime();
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
       String date=formatter.format(today1);
        String replaceString=date.replaceAll("-","_");

        String finalDate=replaceString+"_"+replaceString2;
       //Toast.makeText(this, ""+finalDate, Toast.LENGTH_SHORT).show();
       String alter="ALTER TABLE "+MainActivity.Tbname+" ADD COLUMN _"+finalDate+" INTEGER;";
        MainActivity.mydatabase.execSQL(alter);
       // Toast.makeText(this, "Alter executed!", Toast.LENGTH_SHORT).show();
        Cursor dataFromTable =getData();
        dataFromTable.moveToFirst();
        for(CheckBox cb:arrayCheckBox){
            String str=dataFromTable.getString(0)+dataFromTable.getString(1);

            if(cb.isChecked()) {
                if (str.equals(cb.getText().toString()))
                {
                    String set="UPDATE "+ MainActivity.Tbname+" SET _"+finalDate+" =1 WHERE RollNo= "+dataFromTable.getString(0)+";";
                    MainActivity.mydatabase.execSQL(set);
                    //Toast.makeText(this, "Updated 1 for "+dataFromTable.getDouble(0), Toast.LENGTH_SHORT).show();
                }

                //Toast.makeText(this, ""+cb.getText().toString(), Toast.LENGTH_SHORT).show();
            }
           else
            {
                if (str.equals(cb.getText().toString()))
                {
                    String set="UPDATE "+ MainActivity.Tbname+" SET _"+finalDate+" =0 WHERE RollNo= "+dataFromTable.getString(0)+";";
                    MainActivity.mydatabase.execSQL(set);
                    //Toast.makeText(this, "Updated 0 for "+dataFromTable.getDouble(0), Toast.LENGTH_SHORT).show();
                }
            }
            dataFromTable.moveToNext();

        }
       dataFromTable.close();
       Cursor resultSet =MainActivity.mydatabase.rawQuery("Select _"+finalDate+" from "+ MainActivity.Tbname,null);
        resultSet.moveToFirst();
        for (int i = 0; i < resultSet.getCount(); i++)
        {
            Toast.makeText(this, ""+resultSet.getInt(0),Toast.LENGTH_SHORT).show();
            resultSet.moveToNext();
        }
        resultSet.close();

    }
    public Cursor getData()
    {
        Cursor resultSet =MainActivity.mydatabase.rawQuery("Select * from "+ MainActivity.Tbname,null);
        resultSet.moveToFirst();
        return resultSet;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String returnValue = data.getStringExtra("key2");
                Toast.makeText(getApplicationContext(), "" + returnValue, Toast.LENGTH_LONG).show();
                /*
                for(int j=0;j<StudList.size();j++)
                {
                    cb=new CheckBox(this);
                    cb.setId(j);
                    cb.setText(StudChecklist.StudList.get(j).getRoll()+StudChecklist.StudList.get(j).getName());
                    arrayCheckBox.add(cb);
                    CheckBoxContainer.addView(cb);
                }*/
                Cursor dataFromTable =getData();
                if(dataFromTable.getCount() == 0){
                    Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
                }else{
                    for (int i = 0; i < dataFromTable.getCount(); i++)
                    {
                        cb=new CheckBox(this);
                        cb.setId(i);
                        cb.setText(dataFromTable.getString(0)+dataFromTable.getString(1));
                        //Toast.makeText(this, ""+dataFromTable.getDouble(0)+dataFromTable.getString(1), Toast.LENGTH_SHORT).show();
                        arrayCheckBox.add(cb);
                        CheckBoxContainer.addView(cb);
                        dataFromTable.moveToNext();
                    }

                   /*
                    while(dataFromTable.moveToNext()){
                        cb=new CheckBox(this);
                        //cb.setId(j);
                        cb.setText(dataFromTable.getDouble(0)+dataFromTable.getString(1));
                        Toast.makeText(this, ""+dataFromTable.getDouble(0)+dataFromTable.getString(1), Toast.LENGTH_SHORT).show();
                        arrayCheckBox.add(cb);
                        CheckBoxContainer.addView(cb);

                    }*/

                    }

                    dataFromTable.close();
                if (resultCode == RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
        }

    }
}
