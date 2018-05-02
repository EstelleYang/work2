package com.lzjs.uappoint.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.adapter.PacsChatMsgViewAdapter;
import com.lzjs.uappoint.bean.PacsChatMsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import appoint.lzjs.com.pulltorefreshview.PullToRefreshView;

/**
 * Created by SangJP on 2017.3.20.
 */

public class PacsChatMsgFragment extends Fragment implements OnClickListener {

    private Button mBtnSend;
    //private Button mBtnBack;
    private EditText mEditTextContent;
    //聊天内容的适配器
    private PacsChatMsgViewAdapter mAdapter;
    private ListView mListView;
    //聊天的内容
    private List<PacsChatMsg> mDataArrays = new ArrayList<PacsChatMsg>();
    private static final String ARG_PARAM1 = "param";
    private String argParam1;
    public Context context; // 存储上下文对象
    public Activity activity; // 存储上下文对象

    private String[] msgArray = new String[]{"  这种情况最好复查一个CT！",
            "您好，今天病理报告出来了，胸腺癌。和大血管粘连这种情况我们手术成功几率大吗？...",
            "...",
            "麻烦问一下，我前面的问题影响大吗？",
            "患者肩背部似针扎式疼痛，胸口不适，不敢侧卧睡觉，曾以为骨刺治疗一年多。前几天做ct发现胸腺肿瘤，而且粘连大血管。",
            "请问医生这种情况应该怎么治疗，可以手术吗？手术能切干净了吗？",
            "可以的！",
            "好的大夫，谢谢！"};

    private String[]dataArray = new String[]{"2016-09-01 18:00", "2016-09-01 18:10",
            "2016-09-01 18:11", "2016-09-01 18:20",
            "2016-09-01 18:30", "2016-09-01 18:35",
            "2016-09-01 18:40", "2016-09-01 18:50"};
    private final static int COUNT = 8;

    public static PacsChatMsgFragment newInstance(String arg) {
        PacsChatMsgFragment f = new PacsChatMsgFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PARAM1, arg);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        argParam1 = getArguments().getString(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        activity = getActivity();
        View view = inflater.inflate(R.layout.pacschatmsg, container, false);
        initView(view);
        initData();
        return view;
    }

    //初始化视图
    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview);
        //mBtnBack = (Button) view.findViewById(R.id.btn_back);
        //mBtnBack.setOnClickListener(this);
        mBtnSend = (Button) view.findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent = (EditText) view.findViewById(R.id.et_sendmessage);
    }

    //初始化要显示的数据
    private void initData() {
        for(int i = 0; i < COUNT; i++) {
            PacsChatMsg entity = new PacsChatMsg();
            entity.setMsgdate(dataArray[i]);
            if (i % 2 == 0)
            {
                entity.setUsername("Expert");
                entity.setMsgtype(true);
            }else{
                entity.setUsername("me");
                entity.setMsgtype(false);
            }

            entity.setMsgtext(msgArray[i]);
            mDataArrays.add(entity);
        }
        mAdapter = new PacsChatMsgViewAdapter(context, mDataArrays);
        mListView.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch(view.getId()) {
            //case R.id.btn_back:
            //    break;
            case R.id.btn_send:
                send();
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void send()
    {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0)
        {
            PacsChatMsg entity = new PacsChatMsg();
            entity.setMsgdate(getDate());
            entity.setUsername("");
            entity.setMsgtype(false);
            entity.setMsgtext(contString);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    //获取日期
    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
        return sbBuffer.toString();
    }
    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return true;
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
