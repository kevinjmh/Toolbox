package util;

import java.util.ArrayList;
import java.util.List;

import ui.MainActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Manhua on 2014/12/23.
 */

public abstract class TitleUrlListActivity extends ListActivity {

	private ArrayAdapter<String> adp;
	protected String url;
	protected Class<?> contentPageActivityClass;

	List<String> titleList = new ArrayList<String>();
	List<String> urlList = new ArrayList<String>();

	public abstract void setArg();
	public abstract List<String> extractList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setArg();
		adp = new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, titleList);
		ListView listView = getListView();
		listView.setAdapter(adp);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// view in browser
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(urlList.get(position));
				intent.setData(content_url);
				startActivity(intent);
				return true;
			}
		});

		new DownloadUrlTask().execute(url);
		Toast.makeText(this, "单击查看图片，长按打开浏览器", Toast.LENGTH_LONG)
		.show();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(getApplicationContext(),
				contentPageActivityClass);
		intent.putExtra("url", urlList.get(position));
		startActivity(intent);

		super.onListItemClick(l, v, position, id);
	}
	
	private class DownloadUrlTask extends AsyncTask<String, Integer, List<String>> {

		@Override
		protected List<String> doInBackground(String... params) {		
			return extractList();
		}

		@Override
		protected void onPostExecute(List<String> result) {
			for (int i=0; i<result.size(); i++) {
				titleList.add(result.get(i++));
				urlList.add(result.get(i));
			}			
			adp.notifyDataSetChanged();
		}
	}
}
