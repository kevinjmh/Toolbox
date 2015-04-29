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
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 14-3-15.
 */
public class JnuIPQuery extends Activity {

    private EditText student_name;
    private EditText student_no;
    private TextView result_tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jnu_net);

        Button searchBtu  =(Button)findViewById(R.id.ip_search_button);
        student_no=(EditText)findViewById(R.id.stu_no);
        student_name=(EditText)findViewById(R.id.stu_name);
        result_tv=(TextView)findViewById(R.id.result_tv);

        searchBtu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //检查网络状态
                if (NetworkUtil.getNetworkState(JnuIPQuery.this) == NetworkUtil.NONE) {
                    Toast.makeText(JnuIPQuery.this, "网络不可用哟", Toast.LENGTH_LONG)
                            .show();
                }else{
                    new DownTask().execute(student_name.getText().toString(), student_no.getText().toString());
                    /*Toast.makeText(IPQuery.this,student_name.getText().toString()+student_no.getText().toString(), Toast.LENGTH_LONG)
                            .show();*/
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
            //check input
            if(params[0]==null||params[1]==null){
                return "\n\n\n登陆失败，请输入正确的学号和姓名\n";
            }

            Elements result=getNetInfo(params[0],params[1]);

            StringBuilder sb=new StringBuilder();
            if(result.text()==""){
                return "\n\n\n登陆失败，请输入正确的学号和姓名\n";
            }else{
                //直接显示标签内容，保持页面排版
                int i=0;
                sb.append("\n");
                for(Element td:result){
                    if(i<24){
                        switch(i%2){
                            case 0:
                                sb.append(td.text());
                                break;
                            case 1:
                                sb.append(Html.fromHtml(td.toString()));
                                sb.append("\n");
                                break;
                        }
                    }
                    else{
                        sb.append(Html.fromHtml(td.toString()));
                    }
                    i++;

                }
            }
            return sb.toString();
        }
    }

    public static Elements getNetInfo(String stu_name,String stu_no) {
        String html=sendhttpClientPost(stu_no,stu_name);
        //获取目标链接的Document
        Document doc = Jsoup.parse(html);
        //获取所有input标签
        Elements els = doc.getElementsByTag("td");
        if(els.text()=="")
            System.out.println("result:"+els+html);

        return els;
    }

    private static String sendhttpClientPost(String stu_no,String stu_name) {
        String url="http://mynet.jnu.edu.cn/zhwl/login.asp";
        String html = null;
        //POST的URL
        HttpPost httppost=new HttpPost(url);
        //建立HttpPost对象
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        //建立一个NameValuePair数组，用于存储欲传送的参数
        params.add(new BasicNameValuePair("id_num",stu_no));
        params.add(new BasicNameValuePair("u_name",stu_name));
        //  params.setContentCharset("UTF-8");
        //添加参数
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params,"GBK"));
            //设置编码
            HttpResponse response = new DefaultHttpClient().execute(httppost);
            if(response.getStatusLine().getStatusCode()==200){//如果状态码为200,就是正常返回

                html = EntityUtils.toString(response.getEntity());
                //处理乱码，对取得的result字符串作下转换
                html=new String(html.getBytes("ISO-8859-1"),"GBK");
                //得到返回的字符串
                //  System.out.println(html);
                //打印输出
                //如果是下载文件,可以用response.getEntity().getContent()返回InputStream
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;

        //发送Post,并返回一个HttpResponse对象
        //Header header = response.getFirstHeader("Content-Length");
        //String Length=header.getValue();
        // 上面两行可以得到指定的Header
    }
}