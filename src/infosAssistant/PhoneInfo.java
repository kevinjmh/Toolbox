package infosAssistant;

import java.io.File;

import com.manhua.toolbox.R;
import android.telephony.TelephonyManager;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class PhoneInfo extends Activity {
	
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_info);
		
		tv=(TextView) findViewById(R.id.textView);
		tv.setText(getDeviceInfo()+getSimInfo()+getSDCardInfo()+getWifiInfo());
		
	}
	
	
	private String getDeviceInfo(){
		StringBuilder sb=new StringBuilder();
		sb.append("Product: " + android.os.Build.PRODUCT+"\n");
		sb.append("BRAND: " + android.os.Build.BRAND+"\n");
		sb.append("DEVICE: " + android.os.Build.DEVICE+"\n");
		sb.append("MODEL: " + android.os.Build.MODEL+"\n");
		sb.append("DISPLAY: " + android.os.Build.DISPLAY+"\n");
		sb.append("RELEASE: " + android.os.Build.VERSION.RELEASE+"\n");
		sb.append("SDK: " + android.os.Build.VERSION.SDK_INT+"\n");
		sb.append("CPU_ABI: " + android.os.Build.CPU_ABI+"\n");
		sb.append("ID: " + android.os.Build.ID+"\n");
		sb.append("FINGERPRINT: " + android.os.Build.FINGERPRINT+"\n");	
		
		DisplayMetrics dm = new DisplayMetrics();  
	    getWindowManager().getDefaultDisplay().getMetrics(dm);  
	    sb.append("Screen: "+String.valueOf(dm.heightPixels)+"x"+String.valueOf(dm.heightPixels)+"\n");
	    return sb.toString();	     
	}
	
	private String getSimInfo(){
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb=new StringBuilder();
		sb.append("IMEI: " + tm.getDeviceId()+"\n");
		sb.append("NetworkOperator: " + tm.getNetworkOperatorName()+"\n");
		sb.append("SimOperator: " + tm.getSimOperatorName()+"\n");
		sb.append("SimSerialNumber: " + tm.getSimSerialNumber()+"\n");
		sb.append("Roaming: " + (tm.isNetworkRoaming()?"true":"false")+"\n");
		sb.append("Line1Number: " + tm.getLine1Number()+"\n");	  
		
//	    Log.i(TAG,sb.toString());
	    return sb.toString();	 
	}
	
	public String getSDCardInfo() {  
	    /*  
	     * <uses-permission  
	     * android:name="android.permission.WRITE_EXTERNAL_STORAGE">  
	     * </uses-permission>  
	     */ 
	    if (Environment.getExternalStorageState().equals(  
	            Environment.MEDIA_MOUNTED)) {  
	        File path = Environment.getExternalStorageDirectory();  
	        StatFs statfs = new StatFs(path.getPath());  
	        long blockSize = statfs.getBlockSize();  
	        long totalBlocks = statfs.getBlockCount();  
	        long availaBlock = statfs.getAvailableBlocks();  
	        // 计算总空间大小和空闲的空间大小,单位B
	        long availableSize = blockSize * availaBlock;  
	        long allSize = blockSize * totalBlocks;  
	        return ("SDCardInfo: "+availableSize/1024 /1024+"MB/"+allSize/1024 /1024+"MB"+"\n");
	    }
		return "No SD Card Found";  	 
	}
	
	public String getWifiInfo() {  
	    // 获取wifi服务  
	    WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
	    if (!wifiManager.isWifiEnabled()) {  
	    	return "wifi is not on";
//	        wifiManager.setWifiEnabled(true);  
	    }  
	    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	    int ipAddress = wifiInfo.getIpAddress();  
	    String ip = intToIp(ipAddress);  
	    return "IP: "+ip+"\nmacAdd: " + wifiInfo.getMacAddress()+"\nSSID:"  + wifiInfo.getSSID()+"\n";  
	}  
	

	private String intToIp(int i) {  
	    return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)  
	            + "." + (i >> 24 & 0xFF);  
	}  
	
	private static int ipToInt(String ip) {  
        String[] ips = ip.split("\\.");  
        return (Integer.parseInt(ips[0]) << 24) + (Integer.parseInt(ips[1]) << 16)  
                + (Integer.parseInt(ips[2]) << 8) + Integer.parseInt(ips[3]);  
    } 


}
