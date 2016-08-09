package codeU;

import java.io.IOException;
import java.lang.String;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


 
public class WikiCrawler {
	// keeps track of where we started
	private final String source;
	
	// the index where the results go
	private Indexer index;
	
	// queue of URLs to be indexed
	private Queue<String> queue = new LinkedList<String>();
	
	// fetcher used to get pages from Wikipedia
	final static WikiFetcher wf = new WikiFetcher();
	
	//Query terms
	private List<String> terms;
	
	private String query;

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index
	 */
	public WikiCrawler(String source, String query, List<String> terms, Indexer index) {
		this.source = source;
		this.index = index;
		this.terms = terms;
		this.query = query;
		
	}
	/**
	 * Sets the queue for the crawler
	 * 
	 */
	public void setQueue(){
		Elements paras;
		try {
			paras = wf.fetchWikipedia(source);
			
			queueInternalLinks(paras);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Returns the number of URLs in the queue.
	 * 
	 * @return
	 */
	public int queueSize() {
		return queue.size();	
	}

	
	/**
	 * Gets a URL from the queue and indexes it.
	 * @param b 
	 * 
	 * @return Number of pages indexed.
	 * @throws IOException
	 */
	public void crawl() throws IOException{
        
		if(queue.isEmpty())
			return;
		String url=queue.poll();
		while(!queue.isEmpty()){
			if(!index.isIndexed(query,url)){
				index.indexPage(url,terms,query,wf.fetchWikipedia(url));
			}
			url = queue.poll();
		}
		
	}
	
	/**
	 * Parses paragraphs and adds internal links to the queue.
	 * 
	 * @param paragraphs
	 */
	// NOTE: absence of access level modifier means package-level
  void queueInternalLinks(Elements paragraphs) {
        for (Element paragraph: paragraphs) {
            queueInternalLinks(paragraph);
        }
    }
 
    private void queueInternalLinks(Element paragraph) {
        Elements elts = paragraph.select("a[href]");
        for (Element elt: elts) {
            String relURL = elt.attr("href");
			if(relURL.startsWith("/wiki/")){
				//System.out.println(relURL);
				String absURL="https://en.wikipedia.org"+relURL;
				queue.offer(absURL);
			}
        }
        //System.out.println(queue);
    }

}
