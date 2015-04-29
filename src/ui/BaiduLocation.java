package ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.manhua.toolbox.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Kevin on 14-3-15.
 */
public class BaiduLocation extends Activity {

    private LocationClient mLocClient;
    private TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        //show for user
        tv=(TextView) findViewById(R.id.textView);
        Button relocate_button = (Button) findViewById(R.id.button);

        relocate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("");
                locate();
            }
        });

        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(new MyLocationListenner());

        locate();
    }

    /**
     * 定位
     * */
    void locate(){
        setLocationOption();
        mLocClient.start(); tv.append("\n正在启动定位引擎...\n");
        mLocClient.requestLocation(); tv.append("正在请求定位...\n");
    }

    private void setLocationOption() {

        // 设置相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);// 禁止启用缓存定位
        option.setPoiNumber(10); // 最多返回POI个数
        option.setPoiDistance(1000); // poi查询距离
        option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
        mLocClient.setLocOption(option);
    }

    /**
     * 监听函数，有新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null)
                return;
            StringBuffer sb = new StringBuffer(256);
			sb.append("\n时间time : "); sb.append(location.getTime());
			sb.append("\n返回值error code : "); sb.append(location.getLocType());
			sb.append("\n纬度latitude : "); sb.append(location.getLatitude());
			sb.append("\n经度lontitude : "); sb.append(location.getLongitude());
			sb.append("\n精度radius : "); sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\n当前位置addr : ");
                sb.append(location.getAddrStr());
            }

            tv.append(sb.toString()+"\n");

            mLocClient.requestPoi();
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
            /*StringBuffer sb = new StringBuffer(256);
			sb.append("Poi时间: ");
            sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
            if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                 sb.append(poiLocation.getAddrStr());
            }
            sb.append("\n纬度:");
            sb.append(poiLocation.getLatitude());
            sb.append("\n经度:");
            sb.append(poiLocation.getLongitude());
            sb.append("\n精度:");
            sb.append(poiLocation.getRadius()+",");
*/
            tv.append("\n你的周围：\n");
            if (poiLocation.hasPoi()) {
                tv.append(extractePoiInfo(poiLocation.getPoi()));
            } else {
                tv.append("noPoi information");
            }

            mLocClient.stop();
        }
    }

    //提取百度定位的附近兴趣点
    private String extractePoiInfo(String ss){
        StringBuffer sb = new StringBuffer(256);

        try {
            JSONObject mJsonObject=new JSONObject (ss);
            JSONArray mJsonArray = mJsonObject.getJSONArray("p");

            JSONObject temp;
            for (int i=0;i<mLocClient.getLocOption().getPoiNumber();i++){
                temp=mJsonArray.getJSONObject(i);
                sb.append("离");
                sb.append(temp.getString("tel")+temp.getString("name"));
                sb.append("\t"+temp.getString("dis")+"米\n");
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //确保退出了百度定位
    @Override
    public void onDestroy() {
        if (mLocClient.isStarted()) {
            mLocClient.stop();
        }
        super.onDestroy();
    }
}