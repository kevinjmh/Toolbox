package infosAssistant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manhua.toolbox.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FSExplorer extends ListActivity implements OnItemClickListener {
 
	ListView itemlist = null;
	String path = "/mnt";
	List<Map<String, Object>> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("文件浏览器");
		itemlist = getListView();
		itemlist.setOnItemClickListener(this);
		refreshListItems(path);
	}

	private void refreshListItems(String path) {
		setTitle("文件浏览器 > "+path);
		list = buildListForSimpleAdapter(path);
		SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.file_row,
				new String[] { "name", "path" ,"img"}, new int[] { R.id.name,
						R.id.desc ,R.id.img});
		itemlist.setAdapter(notes);
		itemlist.setSelection(0);
	}

	private List<Map<String, Object>> buildListForSimpleAdapter(String path) {
		File[] files = new File(path).listFiles();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(files.length);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("name", "/");
		root.put("img", R.drawable.file_root);
		root.put("path", "go to root directory");
		list.add(root);
		Map<String, Object> pmap = new HashMap<String, Object>();
		pmap.put("name", "..");
		pmap.put("img", R.drawable.file_paranet);
		pmap.put("path", "go to paranet Directory");
		list.add(pmap);
		for (File file : files){
			Map<String, Object> map = new HashMap<String, Object>();
			if(file.isDirectory()){
				map.put("img", R.drawable.directory);
			}else{
				map.put("img", R.drawable.file_doc);
			}
			map.put("name", file.getName());
			map.put("path", file.getPath());
			list.add(map);
		}
		return list;
	}
	
	private void goToParent() {
		File file = new File(path);
		File str_pa = file.getParentFile();
		if(str_pa == null){
			Toast.makeText(FSExplorer.this,"已经是根目录",
					Toast.LENGTH_SHORT).show();
			refreshListItems(path);	
		}else{
			path = str_pa.getAbsolutePath();
			refreshListItems(path);	
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if (position == 0) {
			path = "/";
			refreshListItems(path);
		}else if(position == 1){
			goToParent();
		} else {
			path = (String) list.get(position).get("path");
			File file = new File(path);
			if(file.canRead() && file.isDirectory()){
					refreshListItems(path);
			}else{
				Toast.makeText(FSExplorer.this,"权限不足",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

 

}
