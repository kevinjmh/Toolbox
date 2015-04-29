package ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.manhua.toolbox.R;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import util.NetworkUtil;

import java.io.IOException;

/**
 * Created by Manhua on 2014/4/28.
 */
public class GDcrjcheck extends Activity {

    private EditText person_id;
    private TextView result_tv;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.gdcrjcheck);
        Button searchBtu = (Button) findViewById(R.id.id_check_button);
        person_id = (EditText) findViewById(R.id.id);
        result_tv = (TextView) findViewById(R.id.result_tv);

        searchBtu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //检查网络状态
                if (NetworkUtil.getNetworkState(GDcrjcheck.this) == NetworkUtil.NONE) {
                    Toast.makeText(GDcrjcheck.this, "网络不可用哟", Toast.LENGTH_LONG)
                            .show();
                } else {
                    if(person_id.getText().toString().equals("")){
                        return;
                    }else{
                        new DownTask().execute(person_id.getText().toString());
                    }

                }
            }


        });
    }


    private class DownTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result_tv.setText(s);
        }

        @Override
        protected String doInBackground(String... params) {
            Elements result = getStatusInfo(params[0]);

            StringBuilder sb = new StringBuilder("\n");

            //直接显示标签内容，保持页面排版
            for(int i=0;i<result.size()-3;i++){
               String e= result.get(i).toString();
                e= String.valueOf(Html.fromHtml(e));
                if(e.contains("重新输入")){
                    return sb.append(e).toString();
                }else{
                    sb.append(e.split("选择速递")[0]+"\n\n");
                }
            }
            sb.append("选择速递方式取证的，请等待速递公司上门服务，联系电话11185，谢谢查询。");
            return sb.toString();
        }

        public Elements getStatusInfo(String id) {
            String html = sendhttpClientPost(id);
            //获取目标链接的Document
            Document doc = Jsoup.parse(html);
            //获取所有input标签
            Elements els = doc.getElementsByClass("news_font");
            if (els.text() == "")
                System.out.println("result:" + els + html);

            return els;
        }

        private String sendhttpClientPost(String id) {
            String url = "http://www.gdcrj.com/wsyw/tcustomer/tcustomer.do?&method=find&applyid="+id;
            String html = null;
            HttpGet hr=new HttpGet(url) ;

            //添加参数
            try {
                HttpResponse response = new DefaultHttpClient().execute(hr);
                if (response.getStatusLine().getStatusCode() == 200) {//如果状态码为200,就是正常返回

                    html = EntityUtils.toString(response.getEntity());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return html;
        }
    }
}