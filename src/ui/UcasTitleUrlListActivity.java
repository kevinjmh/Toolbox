package ui;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.TitleUrlListActivity;

public class UcasTitleUrlListActivity extends TitleUrlListActivity {
	
	public static String domainName="http://renwen.ucas.ac.cn";
			
	@Override
	public void setArg() {
		url=domainName+"/Pages/announceList.aspx";
		contentPageActivityClass=UcasPassageActivity.class;		
	}
	
	@Override
	public List<String> extractList() {
		String linkText;
		Document doc;
		String linkString;
		List<String> ret = new ArrayList<String>();
		try {
			doc = Jsoup.connect(url).get();
			Elements els = doc.getElementsByClass("newbiaoti1");
			for (Element link : els) {
				if (link.getElementsByTag("a").size() > 0) {
					linkText = link.text();
                    if(linkText.startsWith("【")){// 只处理讲座类信息
                        linkText=linkText.substring(1+linkText.indexOf("】"));
                        linkString = link.child(0).attr("href");
                        ret.add(linkText);
                        ret.add(domainName+"/Pages/"+ linkString);
                    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


}
