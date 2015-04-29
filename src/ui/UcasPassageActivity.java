package ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.manhua.toolbox.R;

/**
 * Created by Manhua on 2014/6/10.
 */
public class UcasPassageActivity extends Activity {
	private static final String TAG = "UcasPassageActivity";
	String imageUrl;
	Bitmap bitmap = null;
	ImageView imView;
	
	public static String domainName="http://renwen.ucas.ac.cn";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.ucaspassage);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String newsUrl = (String) bundle.get("url");

		imView = (ImageView) findViewById(R.id.imageView);

		new DownloadUrlTask().execute((newsUrl));
	}

	public Bitmap returnBitMap(String url) {
		try {
			HttpClient client = new DefaultHttpClient();

			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	public Bitmap returnBitMap2(String url) {
		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private class DownloadUrlTask extends AsyncTask<String, Integer, String> {

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected String doInBackground(String... params) {
			Log.i(TAG, "doInBackground(Params... params) called");

			Document doc = null;
			String imgurlString = null;
			try {
				doc = Jsoup.connect(params[0]).get();
				Elements els = doc.getElementsByAttributeValueMatching("src",
						"Images");
				for (Element element : els) {
					if (element.hasAttr("alt")) {
						imgurlString = element.attr("src");
						System.out.println(domainName + imgurlString);
						imageUrl = domainName + imgurlString;
						returnBitMap(imageUrl);
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return imageUrl;
		}

		// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "onPostExecute(Result result) called");
			imView.setImageBitmap(bitmap);
		}

	}
}