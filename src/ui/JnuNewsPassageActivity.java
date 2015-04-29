package ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.manhua.toolbox.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Manhua on 2014/6/10.
 */
public class JnuNewsPassageActivity extends Activity {
	private static final String TAG = "JnuNewsPassageActivity";
	private TextView tv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jnupassage);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String newsID = (String) bundle.get("url");

		tv = (TextView) findViewById(R.id.textView);
		new DownloadUrlTask(this).execute((newsID));
//		new DownloadUrlTask(this).execute(buildUrlByID(newsID));
	}

	public static String buildUrlByID(String id) {
		String head = "http://jwc.jnu.edu.cn/ReadNews.asp?ID=";
		String tail = "&BigClassName=0&SmallClassName=0&SpecialID=0";
		return head + id + tail;
	}

	public static HashMap<String, String> readNews(String html) {
		HashMap<String, String> hm = new HashMap<String, String>();

		// get title
		String title = null;
		Pattern pa = Pattern.compile("<font color=\"#000000\">(.*?)</font>");
		Matcher ma = pa.matcher(html);
		while (ma.find()) {
			title = ma.group();
			title = title.replaceAll("<[^>]*>", "");
			// System.out.println(title);
		}

		// getContent
		String newsContent = "";
		pa = Pattern.compile("<P(.*)</P>");
		ma = pa.matcher(html);
		StringBuilder sb = new StringBuilder();
		while (ma.find()) {
			newsContent = ma.group();
			newsContent = newsContent.replaceAll("<P", "\n    <");
			newsContent = newsContent.replaceAll("<[^>]*>", "");
			newsContent = newsContent.replaceAll("&nbsp;", " ");
			newsContent = newsContent.replaceAll("&amp;", "&");
			newsContent = newsContent.replaceAll("<br>", "\n");
			sb.append(newsContent);
		}
		// System.out.println(newsContent);
		hm.put("title", title);
		hm.put("newsContent", sb.toString());
		return hm;

	}

	private class DownloadUrlTask extends AsyncTask<String, Integer, String> {
		private Context mContext;
		ProgressDialog pd;

		public DownloadUrlTask(Context mContext) {
			this.mContext = mContext;
		}

		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			Log.i(TAG, "onPreExecute() called");

			pd = new ProgressDialog(mContext);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setTitle("Loading");
			pd.setMessage("Progresses: 0%");
			pd.setCancelable(true);
			pd.setIndeterminate(false);
			pd.show();
		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			Log.i(TAG, "doInBackground(Params... params) called");
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(params[0]);
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					long total = entity.getContentLength();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int count = 0;
					int length = -1;
					while ((length = is.read(buf)) != -1) {
						baos.write(buf, 0, length);
						count += length;
						// 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
						publishProgress((int) ((count / (float) total) * 100));
					}
					return new String(baos.toByteArray(), "gb2312");
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
			return null;
		}

		// onProgressUpdate方法用于更新进度信息
		@Override
		protected void onProgressUpdate(Integer... progresses) {
			Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
			pd.setProgress(progresses[0]);
			pd.setMessage("Progresses: " + progresses[0] + "%");
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "onPostExecute(Result result) called");
			pd.cancel();

			HashMap<String, String> hm = readNews(result);
			tv.setText(hm.get("title") + "\n\n" + hm.get("newsContent"));
		}

		// onCancelled方法用于在取消执行中的任务时更改UI
		@Override
		protected void onCancelled() {
			Log.i(TAG, "onCancelled() called");
			pd.setProgress(0);
		}

	}
}