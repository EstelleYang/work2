/**
 * ****************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************
 */
package com.lzjs.uappoint.fresco.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.fresco.ui.widget.HackyViewPager;
import com.lzjs.uappoint.fresco.utils.ImageUrlUtils;

import uk.co.senab.photoview.PhotoView;

/**
 * Lock/Unlock button is added to the ActionBar.
 * Use it to temporarily disable ViewPager navigation in order to correctly interact with ImageView by gestures.
 * Lock/Unlock state of ViewPager is saved and restored on configuration changes.
 *
 * Julia Zudikova
 */

public class ViewPagerActivity extends Activity {

    private static final String ISLOCKED_ARG = "isLocked";
    private ViewPager mViewPager;
    private int position;
    private String[] urls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityfresco_view_pager);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        setContentView(mViewPager);

        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            urls = getIntent().getStringArrayExtra("urls");
        }
        mViewPager.setAdapter(new SamplePagerAdapter(urls));
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            urls = getIntent().getStringArrayExtra("urls");
            mViewPager.setCurrentItem(position);
        }

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
            ((HackyViewPager) mViewPager).setLocked(isLocked);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private static final String[] sDrawables = ImageUrlUtils.getImagePacsUrls();
        private String[] urls;

        public SamplePagerAdapter(String[] paramUrls) {
            urls = paramUrls;
        }

        @Override
        public int getCount() {
            return urls.length;
        }
        /*public int getCount() {
            return sDrawables.length;
        }*/

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            //photoView.setImageUri(sDrawables[position]);
            photoView.setImageUri(urls[position]);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    private boolean isViewPagerActive() {
        return (mViewPager != null && mViewPager instanceof HackyViewPager);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
        }
        super.onSaveInstanceState(outState);
    }

}
