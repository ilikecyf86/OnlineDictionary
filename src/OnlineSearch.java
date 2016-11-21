import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by cyf on 2016/11/9.
 */
public class OnlineSearch {
    public static void main(String args[]){
        //baidu youdao bing
        String s = searchYoudao("part");
        System.out.println(s);
    }

    public static String searchBaidu(String keyword) {
        String url = "http://dict.baidu.com/s?wd=" + keyword + "&device=pc&from=home&q=" + keyword;
        String result = keyword + "\n";
        try {
            Document page = Jsoup.connect(url).get();
            Elements meaning = page.select("#simple_means-wrapper div div p");
            for(int i = 0; i < meaning.size(); i++){
                result += meaning.get(i).text();
                result += "\n";
            }

            Elements meaningAndOther = page.select("#simple_means-wrapper div p span");
            if(meaningAndOther.size()!= 0)
                result += "\n";
            for(int i = meaning.size(); i < meaningAndOther.size(); i++){
                result += meaningAndOther.get(i).text();
                result += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String searchYoudao(String keyword) {
        String url = "http://dict.youdao.com/w/eng/" + keyword + "/#keyfrom=dict2.index";
        String result = keyword + "\n";
        try {
            Document page = Jsoup.connect(url).get();
            Elements meaning = page.select("#phrsListTab div ul li");
            for(int i = 0; i < meaning.size(); i++){
                result += meaning.get(i).text();
                result += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String searchBing(String keyword) {
        String url = "http://cn.bing.com/dict/search?q=" + keyword + "&go=%E6%90%9C%E7%B4%A2&qs=bs&form=Z9LH5";
        String result = keyword + "\n";
        try {
            Document page = Jsoup.connect(url).get();
            Elements meaning = page.select(".qdef ul li");
            for(int i = 0; i < meaning.size(); i++){
                result += meaning.get(i).text();
                result += "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
