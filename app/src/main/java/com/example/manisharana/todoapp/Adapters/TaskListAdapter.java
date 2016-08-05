package com.example.manisharana.todoapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manisharana.todoapp.Data.TaskEntry;
import com.example.manisharana.todoapp.Fragments.TaskListFragment;
import com.example.manisharana.todoapp.Models.Section;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.twotoasters.sectioncursoradapter.adapter.SectionCursorAdapter;
import com.twotoasters.sectioncursoradapter.adapter.viewholder.ViewHolder;

import java.util.SortedMap;
import java.util.TreeMap;

public class TaskListAdapter extends SectionCursorAdapter<Section,TaskListAdapter.SectionViewHolder,TaskListAdapter.ItemViewHolder>{

    public TaskListAdapter(Context context, Cursor c, int flags) {
        super(context,c,flags,R.layout.section_view,R.layout.list_item_task);
    }

    @Override
    protected Section getSectionFromCursor(Cursor cursor) {
        long dateInMillis = cursor.getLong(TaskListFragment.COL_TASK_DATE);
        Section section = new Section();
        section.setDayString(Utility.getFriendlyDayString(dateInMillis));
        return section;
    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//        View view = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
//        final ViewHolder viewHolder = new ViewHolder(view);
//        view.setTag(viewHolder);
//
//
//        viewHolder.taskStatusButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LinearLayout parent = (LinearLayout) view.getParent();
//                TextView textIdView = (TextView) parent.findViewById(R.id.text_view_task_id);
//                taskId = textIdView.getText().toString();
//                viewHolder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
//                viewHolder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//                updateTaskStatusToDone(taskId);
//            }
//        });
//
//        return view;
//    }




    private void updateTaskStatusToDone(String taskId) {
        mContext.getContentResolver().update(TaskEntry.CONTENT_URI, getUpdateContentValues(), TaskEntry.TABLE_NAME + "." + TaskEntry._ID + " = ? ", new String[]{taskId});
    }

    private ContentValues getUpdateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskEntry.COLUMN_STATUS, "done");
        return contentValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        boolean isSection = isSection(position);
        Cursor cursor = getCursor();
        View view;

        if (!isSection) {
            int newPosition = getCursorPositionWithoutSections(position);
            if (!cursor.moveToPosition(newPosition)) {
                throw new IllegalStateException("couldn't move cursor to position " + newPosition);
            }
        }

        if (convertView == null) {
            view = isSection ? newSectionView(parent, (Section) getItem(position)) : newItemView(cursor, parent);
        } else {
            view = convertView;
        }

        if (isSection) {
            SectionViewHolder sectionViewHolder = new SectionViewHolder(view);
            view.setTag(R.string.section_view_holder,sectionViewHolder);
            bindSectionViewHolder(position,sectionViewHolder,parent,(Section) getItem(position));
        } else {
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            view.setTag(R.string.item_view_holder,itemViewHolder);
            bindItemViewHolder(itemViewHolder,cursor,parent);
        }

        return view;
    }

    @Override
    protected SectionViewHolder createSectionViewHolder(View sectionView, Section section) {
        return new SectionViewHolder(sectionView);
    }

    @Override
    protected void bindSectionViewHolder(int position, SectionViewHolder sectionViewHolder, ViewGroup parent, Section section) {
        ListView view = ((ListView)parent);
        sectionViewHolder.titleView.setText(((Section)view.getItemAtPosition(0)).getDayString());
    }

    @Override
    protected ItemViewHolder createItemViewHolder(Cursor cursor, View itemView) {
        return new ItemViewHolder(itemView);
    }

    @Override
    protected void bindItemViewHolder(ItemViewHolder itemViewHolder, Cursor cursor, ViewGroup parent) {
            itemViewHolder.taskIdView.setText(String.valueOf(cursor.getInt(TaskListFragment.COL_TASK_ID)));

            long dateInMillis = cursor.getLong(TaskListFragment.COL_TASK_DATE);
            itemViewHolder.dateView.setText(Utility.getFormattedTime(dateInMillis));

            String title = cursor.getString(TaskListFragment.COL_TASK_TITLE);
            itemViewHolder.titleView.setText(title);


            if (cursor.getString(TaskListFragment.COL_TASK_STATUS).equals("done")) {
                itemViewHolder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
                itemViewHolder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                itemViewHolder.taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
            }

    }

    @Override
    protected SortedMap<Integer, Section> buildSections(Cursor cursor) {
        TreeMap<Integer, Section> sections = new TreeMap<>();
        int cursorPosition = 0;

        while (cursor.moveToNext()) {
            Section section = new Section();
            long dateInMillis = cursor.getLong(TaskListFragment.COL_TASK_DATE);
            section.setDayString(Utility.getFriendlyDayString(dateInMillis));
            if (!sections.containsValue(section)) {
                sections.put(cursorPosition + sections.size(), section);
            }
            cursorPosition++;
        }
        return sections;
    }


    public class SectionViewHolder extends ViewHolder {

        private final TextView titleView;

        public SectionViewHolder(View rootView) {
            super(rootView);
            titleView = (TextView) rootView.findViewById(R.id.titleTextView);
        }
    }

    public class ItemViewHolder extends ViewHolder {
        public final TextView dateView;
        public final TextView titleView;
        private final TextView taskIdView;
        private final ImageButton taskStatusButton;


        public ItemViewHolder(View view) {
            super(view);
            taskIdView = (TextView) view.findViewById(R.id.text_view_task_id);
            taskStatusButton = (ImageButton) view.findViewById(R.id.image_view_task_status);
            titleView = (TextView) view.findViewById(R.id.text_view_task_title);
            dateView = (TextView) view.findViewById(R.id.text_view_task_time);
            //     userImage = (ImageView) view.findViewById(R.id.image_view_user_image);
        }
    }




    private abstract class ViewAnimationListener implements Animation.AnimationListener {

        private final View view;

        protected ViewAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            onAnimationStart(this.view, animation);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onAnimationEnd(this.view, animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        protected abstract void onAnimationStart(View view, Animation animation);

        protected abstract void onAnimationEnd(View view, Animation animation);
    }



}
