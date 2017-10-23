package com.igdtuw.technotwisters.sih_android.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.constants.Color;
import com.igdtuw.technotwisters.sih_android.constants.Constants;
import com.igdtuw.technotwisters.sih_android.model.ToDoListContents;

import java.io.Serializable;
import java.util.Date;

public class SeperateToDoActivity extends AppCompatActivity {

    EditText title, content;
    TextView toDoDate;
    Button save, cancel;
    int color;

    int position = -9;
    Intent i;
    ToDoListContents toDo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity_seperate_to_do);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("To Do");
        title = (EditText) findViewById(R.id.titleEditText);
        toDoDate = (TextView) findViewById(R.id.dateTextView);
        content = (EditText) findViewById(R.id.contentEditText);
        save = (Button) findViewById(R.id.saveButton);
        cancel = (Button) findViewById(R.id.cancelButton);

        i = getIntent();
        toDo = (ToDoListContents) i.getSerializableExtra(Constants.MainActivityToDo);
        color = toDo.getColor();
        toDoDate.setText((new Date()).toString());
        title.setText(toDo.getTitle());
        title.setBackgroundColor(color);
        content.setText(toDo.getContent());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = i.getIntExtra(Constants.MainActivityPosition, -1);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_seperate_to_do_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_dropdown_default){
            color = Color.DEFAULT;
        }
        else if(item.getItemId() == R.id.action_dropdown_red){
            color = Color.RED;
        }
        else if(item.getItemId() == R.id.action_dropdown_green){
            color = Color.GREEN;
        }
        else if(item.getItemId() == R.id.action_dropdown_blue){
            color = Color.BLUE;
        }
        else if(item.getItemId() == R.id.action_dropdown_yellow){
            color = Color.YELLOW;
        }
        else if(item.getItemId() == R.id.action_dropdown_brown){
            color = Color.BROWN;
        }
        title.setBackgroundColor(color);
        return true;
    }

    @Override
    public void finish(){
        if(position == -9){
            setResult(Activity.RESULT_CANCELED);
        }
        else{
            Intent intent = new Intent();
            ToDoListContents toDoObject = new ToDoListContents( title.getText().toString(), (new Date()).toString(), content.getText().toString(), color);
            toDoObject.setID(toDo.getId());
            intent.putExtra(Constants.SepereateToDoActivityToDo, (Serializable) toDoObject);
            intent.putExtra(Constants.SepereateToDoActivityPosition, position);
            setResult(Activity.RESULT_OK, intent);
        }
        super.finish();
    }

}
