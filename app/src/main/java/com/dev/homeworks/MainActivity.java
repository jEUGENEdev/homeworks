package com.dev.homeworks;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.homeworks.adapters.LessonAdapter;
import com.dev.homeworks.database.Database;
import com.dev.homeworks.model.Lesson;
import com.dev.homeworks.utils.HiddenStatusBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class MainActivity extends AppCompatActivity {

    private static List<Lesson> lessons = new ArrayList<>();
    private LessonAdapter lessonAdapter;
    private FloatingActionButton button;
    private boolean plus = true;
    private boolean name = true;
    private Button filter;
    boolean activeFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HiddenStatusBar.hidden(getWindow());
        button = findViewById(R.id.main_button);
        filter = findViewById(R.id.close_filter);
        recyclerViewInit();
        lessonsListInit();
        onClickPlus();
        onClickButtonLong();
        tapTarget();
    }

    public void tapTarget() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("tapTarget", false))
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(button)
                    .setPrimaryText(R.string.desc_button)
                    .setSecondaryText(R.string.desc_button_secondary)
                    .setPromptStateChangeListener((prom, state) -> {
                        if ((state == MaterialTapTargetPrompt.STATE_DISMISSED) || (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)) {
                            new MaterialTapTargetPrompt.Builder(this)
                                    .setTarget(R.id.list_layout)
                                    .setPrimaryText(R.string.desc_item)
                                    .setSecondaryText(R.string.desc_item_secondary)
                                    .setPromptFocal(new RectanglePromptFocal())
                                    .show();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("tapTarget", true);
                            editor.apply();
                        }
                    })
                    .show();
    }

    public void lessonsListInit() {
        Database database = new Database(this);
        lessons = database.selectAll(null, null, null);
        database.close();
    }

    public void recyclerViewInit() {
        lessonAdapter = new LessonAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(lessonAdapter);
    }

    public void onClickButtonLong() {
        button.setOnLongClickListener(v -> {
            if (!plus) {
                button.setImageResource(android.R.drawable.ic_menu_add);
                plus = true;
            } else {
                button.setImageResource(android.R.drawable.ic_menu_search);
                plus = false;
            }
            return true;
        });
    }

    public void onClickFilter(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.filter_close_button_close));
        view.setVisibility(View.INVISIBLE);
        Database db = new Database(this);
        lessons = db.selectAll(null, null, null);
        db.close();
        lessonAdapter.notifyItemRangeRemoved(0, lessonAdapter.getItemCount());
        lessonAdapter.notifyItemRangeInserted(0, getLessons().size());
        activeFilter = false;
    }

    public void onClickPlus() {
        button.setOnClickListener(vw -> {
            if (plus) {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.dialog_add_lesson);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                Button save = dialog.findViewById(R.id.save);
                EditText nameLesson = dialog.findViewById(R.id.name_lesson);
                save.setOnClickListener(v -> {
                    String lesson = nameLesson.getText().toString().replaceAll(" +$", "");
                    if (lesson.length() > 0) {
                        boolean[] days = {((CheckBox) dialog.findViewById(R.id.checkBox)).isChecked(), ((CheckBox) dialog.findViewById(R.id.checkBox2)).isChecked(),
                                ((CheckBox) dialog.findViewById(R.id.checkBox3)).isChecked(), ((CheckBox) dialog.findViewById(R.id.checkBox4)).isChecked(),
                                ((CheckBox) dialog.findViewById(R.id.checkBox5)).isChecked(), ((CheckBox) dialog.findViewById(R.id.checkBox6)).isChecked()};
                        String[] daysString = {"пн", "вт", "ср", "чт", "пт", "сб"};
                        StringBuilder timetable = new StringBuilder();
                        for (int i = 0; i < days.length; i++)
                            if (days[i])
                                timetable.append(daysString[i]).append(",");
                        String tableTime;
                        if (timetable.length() > 1)
                            tableTime = timetable.substring(0, timetable.length() - 1);
                        else
                            tableTime = "";
                        Database db = new Database(MainActivity.this);
                        List<Lesson> lessons = db.selectAll(null, null, null);
                        db.close();
                        boolean check = true;
                        for (Lesson lesson_ : lessons)
                            if (lesson_.getNameLesson().equalsIgnoreCase(lesson)) {
                                check = false;
                                break;
                            }
                        if (check) {
                            db = new Database(MainActivity.this);
                            Lesson lesson1 = db.insert(lesson, "", tableTime, "0");
                            db.close();
                            getLessons().add(new Lesson(lesson1.getId(), lesson, "", tableTime, "0"));
                            lessonAdapter.notifyItemInserted(lessonAdapter.getItemCount() - 1);
                            dialog.dismiss();
                        } else
                            Toast.makeText(MainActivity.this, R.string.oops1, Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(MainActivity.this, R.string.oops, Toast.LENGTH_LONG).show();
                });
                dialog.show();
            } else {
                AtomicBoolean check = new AtomicBoolean(true);
                Database db = new Database(MainActivity.this);
                List<Lesson> lessons = db.selectAll(null, null, null);
                db.close();
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.search_window);
                LinearLayout layout = dialog.findViewById(R.id.search_layout);
                Window window = dialog.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setGravity(Gravity.BOTTOM | Gravity.END);
                window.setWindowAnimations(R.style.search);
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText search = dialog.findViewById(R.id.search);
                RadioButton name = dialog.findViewById(R.id.search_name), day = dialog.findViewById(R.id.search_day);
                Spinner spinner = new Spinner(this, Spinner.MODE_DIALOG);
                spinner.setPrompt(getResources().getString(R.string.days_filter));
                day.setOnClickListener(v -> {
                    if (this.name || check.get()) {
                        dialog.dismiss();
                        check.set(false);
                        this.name = false;
                        search.setEnabled(false);
                        layout.addView(spinner);
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, R.layout.days_spinner, R.id.textView, getResources().getStringArray(R.array.days));
                        spinner.setAdapter(adapter);
                        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String item = (String) parent.getItemAtPosition(position);
                                lessonAdapter.notifyItemRangeRemoved(0, lessonAdapter.getItemCount());
                                MainActivity.lessons = lessons.stream().filter(l -> l.getDays().matches(".*" + item + ".*")).collect(Collectors.toList());
                                lessonAdapter.notifyItemRangeChanged(0, lessonAdapter.getItemCount());
                                activeFilter = true;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        };
                        spinner.setOnItemSelectedListener(selectedListener);
                        dialog.show();
                    }
                });
                name.setOnClickListener(v -> {
                    if (!this.name || check.get()) {
                        dialog.dismiss();
                        this.name = true;
                        check.set(false);
                        search.setEnabled(true);
                        layout.removeView(spinner);
                        dialog.show();
                        search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (s.length() == 0) {
                                    lessonAdapter.notifyItemRangeRemoved(0, lessonAdapter.getItemCount());
                                    MainActivity.lessons = lessons;
                                    lessonAdapter.notifyItemRangeChanged(0, lessonAdapter.getItemCount());
                                    activeFilter = false;
                                } else {
                                    lessonAdapter.notifyItemRangeRemoved(0, lessonAdapter.getItemCount());
                                    MainActivity.lessons = lessons.stream().filter(l -> l.getNameLesson().toLowerCase().matches(".*" + s.toString().toLowerCase() + ".*")).collect(Collectors.toList());
                                    lessonAdapter.notifyItemRangeChanged(0, lessonAdapter.getItemCount());
                                    activeFilter = true;
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    }
                });
                dialog.setOnDismissListener(v -> {
                    if (activeFilter) {
                        filter.setVisibility(View.VISIBLE);
                        filter.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.filter_close_button_show));
                    }
                });
            }
        });
    }

    public static List<Lesson> getLessons() {
        return lessons;
    }
}