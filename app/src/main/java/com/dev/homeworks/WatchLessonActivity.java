package com.dev.homeworks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.homeworks.adapters.UploadPhotoAdapter;
import com.dev.homeworks.database.ConstantsSQL;
import com.dev.homeworks.database.Database;
import com.dev.homeworks.model.Lesson;
import com.dev.homeworks.model.Photo;
import com.dev.homeworks.utils.HiddenStatusBar;

import java.util.Random;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class WatchLessonActivity extends AppCompatActivity {

    private EditText homework;
    private boolean enableHomework = false;
    private String id, lesson, dz;
    private RecyclerView photosList;
    private Lesson lessonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watch_lesson);
        HiddenStatusBar.hidden(getWindow());
        homework = findViewById(R.id.homework);
        ConstraintLayout layout = findViewById(R.id.watch_layout);
        infoInit();
        tapTarget();
        initPhotoRecyclerView();
        int[] backgrounds = {R.drawable.algebra, R.drawable.astronomy, R.drawable.biology, R.drawable.chemistry,
                R.drawable.geometry, R.drawable.math, R.drawable.physics};
        layout.setBackgroundResource(backgrounds[new Random().nextInt(backgrounds.length)]);
    }

    @Override
    public void onBackPressed() {
    }

    public void initPhotoRecyclerView() {
        photosList = findViewById(R.id.uploads_photo);
        if (getIntent().hasExtra(ConstantsSQL.TABLE_PHOTOS_NAME)) {
            Lesson lesson = (Lesson) getIntent().getSerializableExtra(ConstantsSQL.TABLE_PHOTOS_NAME);
            photosList.setAdapter(new UploadPhotoAdapter(lesson.getUploadedPhotos()));
            photosList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            photosList.setHasFixedSize(true);
        }
    }

    public void tapTarget() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!preferences.getBoolean("tapTarget1", false))
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(R.id.button)
                    .setPrimaryText(R.string.desc_edit)
                    .setPromptFocal(new RectanglePromptFocal())
                    .setSecondaryText(R.string.desc_edit_secondary)
                    .setPromptStateChangeListener((prom, state) -> {
                        if ((state == MaterialTapTargetPrompt.STATE_DISMISSED) || (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)) {
                            new MaterialTapTargetPrompt.Builder(this)
                                    .setTarget(R.id.homework)
                                    .setPrimaryText(R.string.desc_homework)
                                    .setSecondaryText(R.string.desc_homework_secondary)
                                    .setPromptFocal(new RectanglePromptFocal())
                                    .show();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("tapTarget1", true);
                            editor.apply();
                        }
                    })
                    .show();
    }

    public void onClickSave(View view) {
        String dz = homework.getText().toString();
        if (dz.length() > 0) {
            Lesson lesson = MainActivity.getLessons().get(Integer.parseInt(id));
            Database db = new Database(this);
            db.update(lesson.getId(), this.lesson, dz);
            db.close();
            lesson.setNameLesson(this.lesson);
            lesson.setHomeworkDescription(dz);
            finish();
        } else
            Toast.makeText(this, R.string.oops3, Toast.LENGTH_LONG).show();
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickEdit(View view) {
        if (!enableHomework) {
            homework.setEnabled(true);
            homework.requestFocus();
            enableHomework = true;
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(homework, 0);
            homework.setSelection(homework.getText().length());
        } else
            Toast.makeText(this, R.string.oops2, Toast.LENGTH_SHORT).show();
    }

    public void onClickScreen(View view) {
        if (enableHomework) {
            homework.setEnabled(false);
            enableHomework = false;
        }
    }

    public void onClickUploadPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 77723);
    }

    public void infoInit() {
        Intent intent = getIntent();
        if ((intent.hasExtra(ConstantsSQL.TABLE_LESSONS_LESSON)) && (intent.hasExtra(ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION)) && (intent.hasExtra(ConstantsSQL.TABLE_LESSONS_ID))) {
            lesson = intent.getStringExtra(ConstantsSQL.TABLE_LESSONS_LESSON);
            dz = intent.getStringExtra(ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION);
            id = intent.getStringExtra(ConstantsSQL.TABLE_LESSONS_ID);
            lessonObj = (Lesson) intent.getSerializableExtra(ConstantsSQL.TABLE_PHOTOS_NAME);
            TextView lesson = findViewById(R.id.lesson);
            lesson.setText(this.lesson);
            homework.setText(dz);
        } else {
            Toast.makeText(this, R.string.oops4, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 77723) {
            Uri uri = data.getData();
            UploadPhotoAdapter uploadPhotoAdapter = (UploadPhotoAdapter) photosList.getAdapter();
            uploadPhotoAdapter.getPhotos().add(new Photo(uri));
            uploadPhotoAdapter.notifyItemInserted(uploadPhotoAdapter.getItemCount() - 1);
            Database database = new Database(this);
            database.insertPhotoByLessonId(uri.getPath(), Integer.parseInt(lessonObj.getId()));
            database.close();
        }
    }
}