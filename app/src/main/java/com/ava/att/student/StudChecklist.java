package com.ava.att.student;

import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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
                Cursor cursor =getData();
                int colCount=cursor.getColumnCount();
                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "myData1.xls";

                File directory = new File(sd.getAbsolutePath());
                String dir=directory.toString();
                Toast.makeText(this,""+dir, Toast.LENGTH_SHORT).show();
                //create directory if not exist
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }
                try {

                    //file path
                    File file = new File(directory, csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    WritableWorkbook workbook;
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    //Excel sheet name. 0 represents first sheet
                    WritableSheet sheet = workbook.createSheet("StudentsAttendance", 0);
                    // column and row
                    sheet.addCell(new Label(0, 0, "RollNo"));
                    sheet.addCell(new Label(1, 0, "Name"));
                    for(int k=1;k<colCount;k++)
                    {
                        String colName=cursor.getColumnName(k);
                        sheet.addCell(new Label(k, 0, colName));

                    }

                    if (cursor.moveToFirst()) {
                        do {
                            String RollNo = cursor.getString(0);
                            String Name = cursor.getString(1);

                            int i = cursor.getPosition() + 1;
                            sheet.addCell(new Label(0, i, RollNo));
                            sheet.addCell(new Label(1, i, Name));
                            for(int j=1;j<colCount;j++)
                            {
                                String att=cursor.getString(j);
                                sheet.addCell(new Label(j,i,att));
                            }
                        } while (cursor.moveToNext());
                    }

                    //closing cursor
                    cursor.close();
                    workbook.write();
                    workbook.close();
                    Toast.makeText(getApplication(),
                            "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
                } catch(IOException e){
                    e.printStackTrace();
                } catch (RowsExceededException e) {

                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();

                }
                cursor.close();
            }
            else
            {
                if(id==R.id.action_defaulter)
                {
                    Cursor defCursor =getData();
                    int colCount2=defCursor.getColumnCount()-2;
                    File sd = Environment.getExternalStorageDirectory();
                    String csvFile = "Defaulter_List.xls";

                    File directory = new File(sd.getAbsolutePath());
                    //create directory if not exist
                    if (!directory.isDirectory()) {
                        directory.mkdirs();
                    }
                    try {

                        //file path
                        File file = new File(directory, csvFile);
                        WorkbookSettings wbSettings = new WorkbookSettings();
                        wbSettings.setLocale(new Locale("en", "EN"));
                        WritableWorkbook workbook;
                        workbook = Workbook.createWorkbook(file, wbSettings);
                        //Excel sheet name. 0 represents first sheet
                        WritableSheet sheet = workbook.createSheet("userList", 0);
                        // column and row
                        sheet.addCell(new Label(0, 0, "RollNo"));
                        sheet.addCell(new Label(1, 0, "Name"));
                        sheet.addCell(new Label(2, 0, "Percentage"));
                        int sum=0,per;
                        float fsum=0;
                        if (defCursor.moveToFirst()) {
                            do {
                                sum=0;fsum=0;
                                String RollNo = defCursor.getString(0);
                                String Name = defCursor.getString(1);
                                for(int j=2;j<colCount2;j++)
                                {
                                    per=defCursor.getInt(j);
                                    sum=sum+per;
                                }
                                fsum=(sum/colCount2)*100;
                                if(fsum<75)
                                {
                                    String fper=Float.toString(fsum);
                                    int i = defCursor.getPosition() + 1;
                                    sheet.addCell(new Label(0, i, RollNo));
                                    sheet.addCell(new Label(1, i, Name));
                                    sheet.addCell(new Label(2, i, fper));
                                }

                            } while (defCursor.moveToNext());
                        }

                        //closing cursor
                        defCursor.close();
                        workbook.write();
                        workbook.close();
                        Toast.makeText(getApplication(),
                                "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
                    } catch(IOException e){
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();

                    }
                }
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
