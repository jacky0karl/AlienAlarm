package com.jk.alienalarm;

import java.util.List;

import com.jk.alienalarm.R;
import com.jk.alienalarm.db.AlarmInfo;
import com.jk.alienalarm.db.DBHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AlarmListAdapter extends BaseAdapter {

    private Context mContext;
    private DBHelper mDBHelper;
    private List<AlarmInfo> mAlarmList;

    public AlarmListAdapter(Context context, DBHelper dbHelper) {
        mContext = context;
        mDBHelper = dbHelper;
        mAlarmList = mDBHelper.getAlarmList(false);
    }

    /**
     * @param alarmList the mAlarmList to set
     */
    public void setAlarmList(List<AlarmInfo> alarmList) {
        mAlarmList = alarmList;
    }

    /**
     * @return the mAlarmList
     */
    public List<AlarmInfo> getAlarmList() {
        return mAlarmList;
    }

    @Override
    public int getCount() {
        return mAlarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_alarm, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.switcher = (Switch) convertView.findViewById(R.id.switcher);
            viewHolder.switcher.setOnCheckedChangeListener(mListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mAlarmList.get(position).name);
        viewHolder.switcher.setChecked(mAlarmList.get(position).isEnable);
        viewHolder.switcher.setTag(position);
        return convertView;
    }

    static final class ViewHolder {
        TextView name;
        Switch switcher;
    }

    OnCheckedChangeListener mListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Object obj = buttonView.getTag();
            if (obj != null) {
                int position = (Integer) obj;
                int ret = mDBHelper.setAlarmEnable(mAlarmList.get(position).id, isChecked);
                if (ret < 1) {
                    Toast.makeText(mContext, R.string.saving_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
