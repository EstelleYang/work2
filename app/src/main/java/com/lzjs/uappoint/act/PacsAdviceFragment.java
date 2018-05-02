package com.lzjs.uappoint.act;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzjs.uappoint.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PacsAdviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PacsAdviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PacsAdviceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button btn_edit;
    private Button btn_send;
    private boolean bEditStatus = false;
    private TextView view_content;
    private EditText edit_content;

    public PacsAdviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PacsAdviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PacsAdviceFragment newInstance(String param1, String param2) {
        PacsAdviceFragment fragment = new PacsAdviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View pacsadviceview = inflater.inflate(R.layout.fragment_pacs_advice, container, false);
        view_content = (TextView) pacsadviceview.findViewById(R.id.view_content);
        edit_content = (EditText) pacsadviceview.findViewById(R.id.edit_content);
        btn_edit = (Button) pacsadviceview.findViewById(R.id.pacs_advice_edit);
        btn_send = (Button) pacsadviceview.findViewById(R.id.pacs_advice_send);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bEditStatus) {
                    bEditStatus = true;
                    edit_content.setVisibility(View.VISIBLE);
                    view_content.setVisibility(View.GONE);
                    btn_edit.setText("保存诊断报告");
                } else {
                    bEditStatus = false;
                    edit_content.setVisibility(View.GONE);
                    view_content.setVisibility(View.VISIBLE);
                    btn_edit.setText("编辑诊断报告");
                }
            }
        });
        return pacsadviceview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
