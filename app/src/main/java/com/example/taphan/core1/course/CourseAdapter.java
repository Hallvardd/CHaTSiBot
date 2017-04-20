package com.example.taphan.core1.course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.taphan.core1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taphan on 23.03.2017.
 */

public class CourseAdapter extends ArrayAdapter<Course> {

    private TextView courseTextView;
    private Context context;
    private List<Course> courseList = new ArrayList<>();

    public CourseAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }
    @Override
    public void add(Course object) {
        courseList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.courseList.size();
    }

    @Override
    public Course getItem(int position) {
        return this.courseList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Course courseObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.course, parent, false);
        courseTextView = (TextView) row.findViewById(R.id.course_object);
        courseTextView.setText(courseObj.getCourseKey());
        return row;
    }
}
