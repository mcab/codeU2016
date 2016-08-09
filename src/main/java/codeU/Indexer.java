package codeU;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jsoup.select.Elements;


public class Indexer {
	private Map<String, Map<String,Integer>> counts; //Maps the terms to a map of the urls to the counts	
	private Map<String,Set<String>> urls; //Maps query strings to urls indexed
	public Map<String, Integer> numTerms; // Maps urls to total number of terms in that Url.

	/**
	 * Constructor.
	 * 
	 * @param
	 */
	public Indexer() {
		counts = new HashMap<String,Map<String,Integer>>();
		urls = new HashMap<String,Set<String>>();
		numTerms = new HashMap<String, Integer>();
	}
	
	/**
	 * Checks whether the url is indexed.
	 * 
	 * @param url
	 * @return
	 */
	public boolean isIndexed(String query,String url) {
		if(urls.containsKey(query)){
			if(urls.get(query).contains(url))
				return true;
			else
				return false;
		}else{
			return false;
		}
	}
	
	
	/**
	 *Returns the number of terms in a given url
	 *
	 */
	 public int getNumTerms(String url){
		 return numTerms.get(url).intValue();
	 }

	/**
	 * Looks up a search term and returns a set of URLs.
	 * 
	 * @param term
	 * @return Set of URLs.
	 */
	public Set<String> getURLs(String term) {
		return counts.get(term).keySet();
	}

	/**
	 * Looks up a term and returns a map from URL to count.
	 * 
	 * @param term
	 * @return Map from URL to count.
	 */
	public Map<String, Integer> getCounts(String term) {
		return counts.get(term);
	}


	/**
	 * Returns the number of times the given term appears at the given URL.
	 * 
	 * @param url
	 * @param term
	 * @return
	 */
	public Integer getCount(String url, String term) {
		return counts.get(term).get(url);
	}

	/**
	 * Add a page to the index.
	 * 
	 * @param url         URL of the page.
	 * @param paragraphs  Collection of elements that should be indexed.
	 */
	public void indexPage(String url, List<String> terms, String query, Elements paragraphs){
		System.out.println("Indexing " + url);
		
		// make a TermCounter and count the terms in the paragraphs
		TermCounter tc = new TermCounter(url,terms);
		tc.processElements(paragraphs);
		//Add the counts to the counts map
		for(String term:tc.keySet()){
			if(counts.containsKey(term)){
				counts.get(term).put(url,tc.get(term));
				
			}else{
				Map<String,Integer> m = new HashMap<String,Integer>();
				m.put(url, tc.get(term));
				counts.put(term,m);
			}
		}
		//Add the terms to the numTerms map
		numTerms.put(url,tc.size());
		//Add url to set
		if(urls.containsKey(query)){
			urls.get(query).add(url);
		}else{
			Set<String> u = new HashSet<String>();
			u.add(url);
			urls.put(query, u);
		}
		
	}
	

	/**
	 * Returns the set of terms that have been indexed.
	 * 
	 * Should be used for development and testing, not production.
	 * 
	 * @return
	 */
	public Set<String> termSet() {
		return counts.keySet();
	}

	

}
