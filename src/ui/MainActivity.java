package ui;

import infosAssistant.InfosAssistantActivity;
import util.MyImageView;
import util.NetworkUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.manhua.toolbox.R;

public class MainActivity extends Activity implements View.OnClickListener {

	MyImageView news, share, login, wechat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle("Manhua's ToolBox");

		news = (MyImageView) findViewById(R.id.news);
		share = (MyImageView) findViewById(R.id.share);
		login = (MyImageView) findViewById(R.id.login);
		wechat = (MyImageView) findViewById(R.id.wechat);

		news.setOnClickListener(this);
		share.setOnClickListener(this);
		login.setOnClickListener(this);
		wechat.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {// 一个listener处理所有click动作
		Intent intent = null;
		switch (view.getId()) {
		case R.id.wechat:
			intent = new Intent(this, InfosAssistantActivity.class);// JnuIPQuery
			startActivity(intent);
			return;
			// Toast.makeText(MainActivity.this,"JnuIPQuery暂停使用...",
			// Toast.LENGTH_LONG).show();
		case R.id.news:
			intent = new Intent(this, UcasTitleUrlListActivity.class);
			break;
		case R.id.share:
			intent = new Intent(this, BaiduLocation.class);
			break;
		case R.id.login:
			intent = new Intent(this, GDcrjcheck.class);
			break;
		default:
			break;
		}
		// 检查网络状态
		if (NetworkUtil.getNetworkState(MainActivity.this) == NetworkUtil.NONE) {
			Toast.makeText(MainActivity.this, "网络不可用哟", Toast.LENGTH_LONG)
					.show();
		} else if (intent != null) {
			startActivity(intent);
		}

	}

	private long exitTime = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 双击退出
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				// 退出代码
				onDestroy();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onDestroy() {
		super.onDestroy();
		System.exit(0); // 结束进程
	}

}
