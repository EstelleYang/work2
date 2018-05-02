package com.redare.imagepicker.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.redare.imagepicker.AndroidImagePicker;
import com.redare.imagepicker.R;
import com.redare.imagepicker.widget.photodraweeview.OnPhotoTapListener;
import com.redare.imagepicker.widget.photodraweeview.PhotoDraweeView;

import java.io.Serializable;
import java.util.List;


/**
 * 图片预览删除
 *
 */
public class PreviewDelActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = ImagePreviewActivity.class.getSimpleName();

    TextView mTitleCount;
    TextView mBtnOk;
    private ImageView backBtn;

    List<String> mImageList;

    int mShowItemPosition = 0;


    ViewPager mViewPager;

    TouchImageAdapter mAdapter ;



    private boolean enableSingleTap = true;//singleTap to do something


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_del);

        mImageList = (List<String>) getIntent().getSerializableExtra("images");
        mShowItemPosition = getIntent().getIntExtra("position",0);

        mBtnOk = (TextView) findViewById(R.id.btn_del);
        backBtn = (ImageView) findViewById(R.id.btn_backpress);
        mBtnOk.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        mTitleCount = (TextView) findViewById(R.id.tv_title_count);
        mTitleCount.setText(mShowItemPosition+1+"/" + mImageList.size());

        initView();

        AndroidImagePicker.getInstance().clearSelectedImages();
    }

    private void initView() {
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mAdapter = new TouchImageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mShowItemPosition, false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleCount.setText(position+1+"/" + mImageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_del) {
            mAdapter.remove(mViewPager.getCurrentItem());
            mTitleCount.setText(mViewPager.getCurrentItem()+1+"/" + mImageList.size());
            if (mImageList.size()==0){
                Intent intent = new Intent();
                intent.putExtra("images", (Serializable) mImageList);
                setResult(RESULT_OK,intent);
                finish();
            }
        }else if (i==R.id.btn_backpress){
            Intent intent = new Intent();
            intent.putExtra("images", (Serializable) mImageList);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("images", (Serializable) mImageList);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class TouchImageAdapter extends FragmentStatePagerAdapter {
        public TouchImageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        public void remove(int position){
            mImageList.remove(position);
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            SinglePreviewFragment fragment = new SinglePreviewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(SinglePreviewFragment.KEY_URL, mImageList.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

    }

    @SuppressLint("ValidFragment")
    private class SinglePreviewFragment extends Fragment {
        public static final String KEY_URL = "key_url";
        private PhotoDraweeView photoDraweeView;
        private String url;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();

            url = (String) bundle.getSerializable(KEY_URL);
            Log.i(TAG, "=====current show image path:" + url);

            photoDraweeView = new PhotoDraweeView(getActivity());
            photoDraweeView.setBackgroundColor(0xff000000);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            photoDraweeView.setLayoutParams(params);

            photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    getActivity().finish();
                }
            });
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "file://"+url;
            }

            ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(
                    Uri.parse(url))
                    .setResizeOptions(new ResizeOptions(768,1280))
                    .setAutoRotateEnabled(true);

            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setOldController(photoDraweeView.getController());
            controller.setImageRequest(requestBuilder.build());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    if (imageInfo == null) {
                        return;
                    }
                    photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            photoDraweeView.setController(controller.build());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return photoDraweeView;
        }

    }

}
