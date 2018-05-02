package lzjs.com.refreshandload.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import lzjs.com.refreshandload.R;


public class RefreshAndLoadMainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_refresh_load_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void launchListView(View view){
        XListViewActivity.launch(this);
    }

    public void launchScrollView(View view){
        XScrollViewActivity.launch(this);    }
}
