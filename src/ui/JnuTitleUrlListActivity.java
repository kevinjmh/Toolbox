package ui;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.TitleUrlListActivity;

public class JnuTitleUrlListActivity extends TitleUrlListActivity {
	
	@Override
	public void setArg() {
		url="http://jwc.jnu.edu.cn/index-new.asp";
		contentPageActivityClass=JnuNewsPassageActivity.class;		
	}
	
	@Override
	public List<String> extractList() {
		Document doc;
		String linkString;
		List<String> ret = new ArrayList<String>();
		try {
			doc = Jsoup.connect(url).get();
			Elements els = doc.getElementsByTag("a");
			for (Element link : els) {
                linkString = link.attr("href");
                ret.add(link.text());
                ret.add("http://jwc.jnu.edu.cn/"+ linkString.substring(0,20)+"&BigClassName=0&SmallClassName=0&SpecialID=0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}


}
