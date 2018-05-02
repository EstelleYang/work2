package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.amap.api.maps2d.model.Text;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.DoctorAcceptAmount;




import java.util.List;

public class AcceptAmountAdapter extends ArrayAdapter<DoctorAcceptAmount> {
    private int resourceId;

    public AcceptAmountAdapter(Context context, int resource, List<DoctorAcceptAmount>objects) {
        super(context, resource,objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前项的实体
        DoctorAcceptAmount doctorAcceptAmount = getItem(position);
        View view ;
        ViewHolder viewHolder;

        if (convertView == null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.amount = (TextView)view.findViewById(R.id.accept_amount);
            viewHolder.docName = (TextView)view.findViewById(R.id.doctor_name);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.docName.setText(doctorAcceptAmount.getDocName());
        viewHolder.amount.setText(String.valueOf(doctorAcceptAmount.getAmount()));
        return view;
    }
    class ViewHolder{
        //加载两个TextView控件,对控件的实例进行缓存
        public TextView docName;
        public TextView amount;
    }
}