package com.ava.att;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewCourse extends AppCompatActivity {

    EditText e;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);
        b = (Button) findViewById(R.id.done);
        e=(EditText)findViewById(R.id.CourseName);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CourseName=e.getText().toString();
                Intent resultIntent = getIntent();
// TODO Add extras or a data URI to this intent as appropriate.
                resultIntent.putExtra("key1", CourseName);
                setResult(MainActivity.RESULT_OK, resultIntent);
                finish();

            }
        });
    }
   /* void onClic(View v)
    {


    }*/


}
