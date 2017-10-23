package com.igdtuw.technotwisters.sih_android.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.constants.Constants;
import com.igdtuw.technotwisters.sih_android.database.SQLHelper;
import com.igdtuw.technotwisters.sih_android.model.ToDoListContents;

import java.util.ArrayList;

public class ToDo extends AppCompatActivity {
    ArrayList<ToDoListContents> listToDos;
    ToDoListAdapter adapter;
    final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listToDos = getToDos();
        adapter = new ToDoListAdapter(this, listToDos);
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.setClass(ToDo.this, SeperateToDoActivity.class);
                ToDoListContents toDo = listToDos.get(position);
                i.putExtra(Constants.MainActivityToDo, toDo);
                i.putExtra(Constants.MainActivityPosition, position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
                final int position = pos;
                AlertDialog.Builder builder = new AlertDialog.Builder(ToDo.this);
                builder.setTitle("Confirm");
                final ToDoListContents toDo = listToDos.get(position);
                builder.setMessage("Do you really want to delete \'" + toDo.getTitle() + "\' item");
                //LayoutInflater inflater = getLayoutInflater();
                RelativeLayout inflater = (RelativeLayout)ToDo.this.getLayoutInflater().inflate(R.layout.activity_to_do, null);
          /*  View v = inflater.inflate(R.layout.dialog_confirm_all_deletion, null);
            builder.setView(v);*/
              //  LayoutInflater inflater =ToDo.this.getLayoutInflater();
              //  View v = inflater.inflate(R.layout.dialog_confirm_all_deletion, null);
              //  builder.setView(v);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listToDos.remove(position);
                        adapter.notifyDataSetChanged();
                        SQLHelper sqlHelper = new SQLHelper(ToDo.this, 1);
                        SQLiteDatabase db = sqlHelper.getWritableDatabase();
                        db.delete(SQLHelper.TABLE_NAME, SQLHelper._ID + "=" + toDo.getId(), null);
                        Toast.makeText(ToDo.this, toDo.getTitle() + " ToDo deleted", Toast.LENGTH_SHORT);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.create().show();
                return true;
            }
        });

    }

    private ArrayList<ToDoListContents> getToDos() {
        ArrayList<ToDoListContents> list_to_dos = new ArrayList<>();
        SQLHelper sqlHelper = new SQLHelper(ToDo.this, 1);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        String columns[] = {SQLHelper.TITLE, SQLHelper.DATE, SQLHelper.CONTENT, SQLHelper._ID, SQLHelper.COLOR};
        Cursor c = db.query(false, SQLHelper.TABLE_NAME, columns, null, null, null, null, SQLHelper.DATE + " DESC", null);
        while (c.moveToNext()) {
            String title = c.getString(c.getColumnIndex(SQLHelper.TITLE));
            String date = c.getString(c.getColumnIndex(SQLHelper.DATE));
            String content = c.getString(c.getColumnIndex(SQLHelper.CONTENT));
            int colour = c.getInt(c.getColumnIndex(SQLHelper.COLOR));
            ToDoListContents todo = new ToDoListContents(title, date, content, colour);
            list_to_dos.add(todo);
        }
        return list_to_dos;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_to_do_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addItem) {
            Intent i = new Intent();
            i.setClass(ToDo.this, SeperateToDoActivity.class);
            i.putExtra(Constants.MainActivityToDo, new ToDoListContents("", "", "", 0xFF4f5051));
            startActivityForResult(i, REQUEST_CODE);
        }
        else if(item.getItemId() == R.id.deleteAllItem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ToDo.this);
            builder.setTitle("Confirm");
            builder.setMessage("Do you really want to delete all items");
            //LayoutInflater inflater = getLayoutInflater();
            RelativeLayout inflater = (RelativeLayout)ToDo.this.getLayoutInflater().inflate(R.layout.activity_to_do, null);
          /*  View v = inflater.inflate(R.layout.dialog_confirm_all_deletion, null);
            builder.setView(v);*/
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int size = listToDos.size();
                    for(int i =0; i<size; i++)
                        listToDos.remove(0);
                    adapter.notifyDataSetChanged();
                    SQLHelper sqlHelper = new SQLHelper(ToDo.this, 1);
                    SQLiteDatabase db = sqlHelper.getWritableDatabase();
                    db.delete(SQLHelper.TABLE_NAME, null, null);
                    Toast.makeText(ToDo.this, "All ToDos deleted", Toast.LENGTH_SHORT);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });
            builder.create().show();
        }
       /* }
        else if(item.getItemId() == R.id.searchItem){
            //To Do later on
        }*/
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra(Constants.SepereateToDoActivityPosition, -1);
            SQLHelper sqlHelper = new SQLHelper(ToDo.this, 1);
            SQLiteDatabase db = sqlHelper.getWritableDatabase();
            ToDoListContents toDo = (ToDoListContents) data.getSerializableExtra(Constants.SepereateToDoActivityToDo);
            if (position == -1) {
                listToDos.add(toDo);
                ContentValues cv = new ContentValues();
                cv.put(SQLHelper._ID, toDo.getId());
                cv.put(SQLHelper.CONTENT, toDo.getContent());
                cv.put(SQLHelper.DATE, toDo.getDate());
                cv.put(SQLHelper.TITLE, toDo.getTitle());
                cv.put(SQLHelper.COLOR, toDo.getColor());
                db.insert(SQLHelper.TABLE_NAME, null, cv);
                adapter.notifyDataSetChanged();
            } else {
                listToDos.set(position, toDo);
                String query = "UPDATE " + SQLHelper.TABLE_NAME + " SET " + SQLHelper.DATE + "= \"" + toDo.getDate() +
                        "\" , " + SQLHelper.TITLE + "= \"" + toDo.getTitle() +"\" , " + SQLHelper.COLOR + "= \"" + toDo.getColor() +
                        "\" , " + SQLHelper.CONTENT + "= \"" + toDo.getContent() + "\"  WHERE " + SQLHelper._ID + " = " +
                        toDo.getId() + ";";
                db.execSQL(query);
                adapter.notifyDataSetChanged();
            }
        }
    }
}

