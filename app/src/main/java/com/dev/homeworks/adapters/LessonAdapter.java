package com.dev.homeworks.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.homeworks.MainActivity;
import com.dev.homeworks.R;
import com.dev.homeworks.WatchLessonActivity;
import com.dev.homeworks.database.ConstantsSQL;
import com.dev.homeworks.database.Database;
import com.dev.homeworks.model.Lesson;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.Holder> {

    private final Context context;

    public LessonAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lesson, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.Holder holder, int position) {
        holder.setText(MainActivity.getLessons().get(position).getNameLesson(),
                MainActivity.getLessons().get(position).getDays());
        holder.startAnim();
    }

    @Override
    public int getItemCount() {
        return MainActivity.getLessons().size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private final TextView textView, textView1;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView2);
            textView1 = itemView.findViewById(R.id.textView3);
            intentInit();
            deleteInit();
        }

        public void deleteInit() {
            itemView.setOnLongClickListener(v -> {
                Database db = new Database(context);
                db.delete(String.valueOf(MainActivity.getLessons().get(getAdapterPosition()).getId()));
                db.close();
                MainActivity.getLessons().remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                return true;
            });
        }

        public void intentInit() {
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, WatchLessonActivity.class);
                Lesson lesson = MainActivity.getLessons().get(getAdapterPosition());
                intent.putExtra(ConstantsSQL.TABLE_LESSONS_LESSON, lesson.getNameLesson());
                intent.putExtra(ConstantsSQL.TABLE_LESSONS_HOMEWORK_DESCRIPTION, lesson.getHomeworkDescription());
                intent.putExtra(ConstantsSQL.TABLE_LESSONS_ID, String.valueOf(getAdapterPosition()));
                Database database = new Database(itemView.getContext());
                lesson.setUploadedPhotos(database.selectPhotoByLessonId(Integer.parseInt(lesson.getId())));
                intent.putExtra(ConstantsSQL.TABLE_PHOTOS_NAME, lesson);
                database.close();
                context.startActivity(intent);
            });
        }

        public void setText(String lesson, String timetable) {
            textView.setText(lesson);
            textView1.setText(R.string.days);
            textView1.append(" " + timetable);
        }

        public void startAnim() {
            itemView.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.list_lessons_anim));
        }
    }
}
