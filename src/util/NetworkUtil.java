package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;


public class NetworkUtil {
	/** 没有网络 */
	public final static int NONE = 0; // no network

	/** Wifi网络 */
	public final static int WIFI = 1; // Wi-Fi

	/** 移动网络 */
	public final static int MOBILE = 2; // 3G,GPRS

	/**
	 * 得到网络状态
	 * 
	 * @param context
	 *            Context
	 * @return 网络状态值
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// mobile
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return MOBILE;
		}
		// Wifi
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return WIFI;
		}
		return NONE;
	}
}
