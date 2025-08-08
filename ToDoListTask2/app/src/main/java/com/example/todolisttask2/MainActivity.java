package com.example.todolisttask2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cursoradapter.widget.ResourceCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

public static ArrayList<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void onClickAdd(View v) {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        ListView lv = findViewById(R.id.lvTask);
        TaskAdapter adapter = new TaskAdapter(this, taskList);
        lv.setAdapter(adapter);
    }



    public class TaskAdapter extends ArrayAdapter<Task> {
        public TaskAdapter(Context context, ArrayList<Task> tasks) {
            super(context, 0, tasks);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Task t = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
            }
            TextView tvTaskName = convertView.findViewById(R.id.tvTaskName);
            TextView tvDue = convertView.findViewById(R.id.inputDueOn);
            TextView tvDuration = convertView.findViewById(R.id.inputDuration);
            TextView tvDetail = convertView.findViewById(R.id.inputDetail);

            tvTaskName.setText(t.name);
            tvDue.setText(t.deadline.toString().substring(0,10));
            tvDuration.setText(String.valueOf(t.duration));
            tvDetail.setText(t.description);
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), DetailTask.class);
                intent.putExtra("id", position);
                getContext().startActivity(intent);
            });
            return convertView;
        }
    }
}


