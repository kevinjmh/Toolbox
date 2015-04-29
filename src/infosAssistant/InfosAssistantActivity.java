package infosAssistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.manhua.toolbox.R;
import com.manhua.toolbox.showInfo.InfoContent;
import com.manhua.toolbox.showInfo.InfoContent.InfoItem;
import com.manhua.toolbox.showInfo.ItemListActivity;

public class InfosAssistantActivity extends ListActivity implements OnItemClickListener {

	ListView itemlist = null;
	List<Map<String, Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		itemlist = getListView();
		refreshListItems();
	}
	
	private void refreshListItems() {
		list = buildListForSimpleAdapter();
		SimpleAdapter notes = new SimpleAdapter(this, list, R.layout.infos_assistant_listitem,
				new String[] { "name", "desc" }, new int[] { R.id.name,
						R.id.desc });
		itemlist.setAdapter(notes);
		itemlist.setOnItemClickListener(this);
		itemlist.setSelection(0);
	}
	
	private List<Map<String, Object>> buildListForSimpleAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// Build a map for the attributes
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "系统信息");
		map.put("desc", "查看设备系统版本,运营商及其系统信息.");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "硬件信息");
		map.put("desc", "查看包括CPU,硬盘,内存等硬件信息.");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "软件信息");
		map.put("desc", "查看已经安装的软件信息.");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "运行时信息");
		map.put("desc", "查看设备运行时的信息.");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name", "文件浏览器");
		map.put("desc", "浏览查看文件系统.");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name", "显示手机信息");
		map.put("desc", "旧版 以前写的");
		list.add(map);
		
		return list;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		InfoContent.clearItems();
		switch (position) {
		case 0:
			InfoContent.addItem(new InfoItem(PreferencesUtil.VER_INFO, "操作系统版本"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.SystemProperty, "系统信息"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.TEL_STATUS, "运营商信息"));
			break;
		case 1:
			InfoContent.addItem(new InfoItem(PreferencesUtil.CPU_INFO, "CPU信息"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.MEM_INFO, "内存信息"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.DISK_INFO, "硬盘信息"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.NET_CONFIG, "网络信息"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.DisplayMetrics, "显示屏信息"));
			break;
		case 2:
			startActivity(new Intent(this, SoftwareListActivity.class));
			return;
		case 3:
			InfoContent.addItem(new InfoItem(PreferencesUtil.RunningService, "运行的service"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.RunningTasks, "运行的Task"));
			InfoContent.addItem(new InfoItem(PreferencesUtil.RunningProcesses, "进程信息"));
			break;
		case 4:
			startActivity(new Intent(this, FSExplorer.class));	
			return;	
		case 5:
			startActivity(new Intent(this, PhoneInfo.class));	
			return;				
		}
		startActivity(new Intent(this, ItemListActivity.class));		
	}


}
