package com.manhua.toolbox.showInfo;

import infosAssistant.FetchData;
import infosAssistant.PreferencesUtil;

import com.manhua.toolbox.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment implements Runnable{
	
	private ProgressDialog pd;
	public String info_datas;
	public int id;
	TextView info;
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private InfoContent.InfoItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = InfoContent.ITEM_MAP.get(getArguments().getInt(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			info=((TextView) rootView.findViewById(R.id.item_detail));
			id=mItem.id;
			load_data();		
		}

		return rootView;
	}
	
	
	private void load_data() {
		pd = ProgressDialog.show(getActivity(), "Please Wait a moment..",
				"fetch info datas...", true, false);
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		switch (id) {
		case PreferencesUtil.CPU_INFO:
			info_datas = FetchData.fetch_cpu_info();
			break;
		case PreferencesUtil.DISK_INFO:
			info_datas = FetchData.fetch_disk_info();
			break;
		case PreferencesUtil.NET_STATUS:
			info_datas = FetchData.fetch_netstat_info();
			break;
		case PreferencesUtil.VER_INFO:
			info_datas = FetchData.fetch_version_info();
			break;
		case PreferencesUtil.DMESG_INFO:
			info_datas = FetchData.fetch_dmesg_info();
			break;
		case PreferencesUtil.RunningProcesses:
			info_datas = FetchData.fetch_process_info();
			break;
		case PreferencesUtil.NET_CONFIG:
			info_datas = FetchData.fetch_netcfg_info();
			break;
		case PreferencesUtil.MOUNT_INFO:
			info_datas = FetchData.fetch_mount_info();
			break;
		case PreferencesUtil.TEL_STATUS:
			info_datas = FetchData.fetch_tel_status(getActivity());
			break;
		case PreferencesUtil.MEM_INFO:
			info_datas = FetchData.getMemoryInfo(getActivity());
			break;
		case PreferencesUtil.SystemProperty:
			info_datas = FetchData.getSystemProperty();
			break;
		case PreferencesUtil.DisplayMetrics:
			info_datas = FetchData.getDisplayMetrics(getActivity());
			break;
		case PreferencesUtil.RunningService:
			info_datas = FetchData.getRunningServicesInfo(getActivity());
			break;
		case PreferencesUtil.RunningTasks:
			info_datas = FetchData.getRunningTasksInfo(getActivity());
			break;
		}
		handler.sendEmptyMessage(0);		
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
			info.setText(info_datas);
		}
	};
	
}
