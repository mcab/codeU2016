package codeU;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class WikiFetcher {
	private long lastRequestTime = -1;
	private long minInterval = 1000;
	public static final ExecutorService execService = Executors.newFixedThreadPool(10);



	public WikiFetcher(){

	}

	public List<Future<HashMap<String,Elements>>> fetchWikipedia(Queue<String> queue) throws InterruptedException{
		ArrayList<Connect> tasks = new ArrayList<Connect>();
		for(String url: queue){
			tasks.add(new Connect(url));
		}
		List<Future<HashMap<String,Elements>>> results = execService.invokeAll(tasks);
		return results;
	}

	/**
	 * Fetches and parses a URL string, returning a list of paragraph elements.
	 *
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Elements fetchWiki(String url) throws IOException{
		sleepIfNeeded();

		Connection cn = Jsoup.connect(url);
		//download and parse the document
		//Connection conn = Jsoup.connect(url);
		Document doc = cn.get();

		// select the content text and pull out the paragraphs.
		Element content = doc.getElementById("mw-content-text");

		// TODO: avoid selecting paragraphs from sidebars and boxouts
		Elements paras = content.getElementsByTag("li");
		//System.out.println(paras);
		return paras;

	}

	/**
	 * Rate limits by waiting at least the minimum interval between requests.
	 */
	private void sleepIfNeeded() {
		if (lastRequestTime != -1) {
			long currentTime = System.currentTimeMillis();
			long nextRequestTime = lastRequestTime + minInterval;
			if (currentTime < nextRequestTime) {
				try {
					//System.out.println("Sleeping until " + nextRequestTime);
					Thread.sleep(nextRequestTime - currentTime);
				} catch (InterruptedException e) {
					System.err.println("Warning: sleep interrupted in fetchWikipedia.");
				}
			}
		}
		lastRequestTime = System.currentTimeMillis();
	}
}
