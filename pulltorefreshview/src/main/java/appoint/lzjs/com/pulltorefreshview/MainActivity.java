package appoint.lzjs.com.pulltorefreshview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;



public class MainActivity extends Activity implements OnClickListener, PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

	private PullToRefreshView mPullToRefreshView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		/* 上拉加载更多 */
		h.sendEmptyMessage(1);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		/* 下拉刷新数据 */
		h.sendEmptyMessage(2);
	}

	@Override
	public void onClick(View v) {

	}

	Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				h.postAtTime(new Runnable() {
					@Override
					public void run() {
						mPullToRefreshView.onFooterRefreshComplete();
					}
				}, SystemClock.uptimeMillis() + 1000);
			} else if (msg.what == 2) {
				h.postAtTime(new Runnable() {
					@Override
					public void run() {
						mPullToRefreshView.onHeaderRefreshComplete();
					}
				}, SystemClock.uptimeMillis() + 1000);
			}
		};
	};

}
