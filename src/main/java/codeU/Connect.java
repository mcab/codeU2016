package codeU;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by Sahana on 8/9/2016.
 */
public class Connect implements Callable<HashMap<String,Elements>> {
    String url;

    public Connect(String url){
        this.url = url;

    }

    public HashMap<String,Elements> call() throws IOException{
        Connection conn = Jsoup.connect(url);
        Document doc = conn.get();

        // select the content text and pull out the paragraphs.
        Element content = doc.getElementById("mw-content-text");

        // TODO: avoid selecting paragraphs from sidebars and boxouts
        Elements paras = content.getElementsByTag("li");
        //System.out.println(paras);

        HashMap<String,Elements> hm = new HashMap<String, Elements>();
        hm.put(url,paras);
        return hm;
    }
}
