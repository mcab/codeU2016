package codeU;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Represents the results of a search query.
 *
 */
public class WikiSearch {
	
	// map from URLs that contain the term(s) to relevance score
	private Map<String, Double> map;
	private Indexer index;

	/**
	 * Constructor.
	 * 
	 * @param map
	 */
	public WikiSearch(Map<String, Double> map, Indexer index) {
		this.map = map;
		this.index = index;
	}
	
	/**
	 * Looks up the relevance of a given URL based on TF-IDF
	 * TF(t) = (Number of times term t appears in a document) / (Total number of terms in the document).
	 * IDF(t) = log_e(Total number of documents / Number of documents with term t in it)
	 * @param url
	 * @return
	 */
	public Double getRelevance(String url) {
		double tf = map.get(url)/((double)index.getNumTerms(url));
		//double idf = Math.log(((double)index.getNumURLs())/(double)(map.keySet().size()));
		return tf; 
		
	}
	
	/**
	 * Prints the contents in order of relevance.
	 * 
	 * @param map
	 */
	public void print() {
		List<Entry<String, Double>> entries = sort();
		for (Entry<String, Double> entry: entries) {
			System.out.println(entry);
		}
	}
	
	/**
	 * Computes the union of two search results.
	 * 
	 * @param that
	 * @return New WikiSearch object.
	 */
	public WikiSearch or(WikiSearch that) {
        // FILL THIS IN!
		Map<String, Double> m= new HashMap<String, Double>();
		for(String k: this.map.keySet()){
			if(that.map.containsKey(k)){
				m.put(k,totalRelevance(this.getRelevance(k),that.getRelevance(k)));
			}else{
				m.put(k,this.getRelevance(k));
			}
		}
		for(String k2: that.map.keySet()){
			if(!m.containsKey(k2)){
				m.put(k2,that.getRelevance(k2));
			}
		}
		return new WikiSearch(m,this.index);
	}
	
	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New WikiSearch object.
	 */
	public WikiSearch and(WikiSearch that) {
        // FILL THIS IN!
		Map<String, Double> m=new HashMap<String, Double>();
		for(String k: this.map.keySet()){
			if(that.map.containsKey(k)){
				m.put(k,totalRelevance(this.getRelevance(k),that.getRelevance(k)));
			}
		}
		return new WikiSearch(m, this.index);
		
	}
	
	/**
	 * Computes the intersection of two search results.
	 * 
	 * @param that
	 * @return New WikiSearch object.
	 */
	public WikiSearch minus(WikiSearch that) {
        // FILL THIS IN!
		Map<String, Double> m=new HashMap<String, Double>();
		for(String k: this.map.keySet()){
			if(!that.map.containsKey(k)){
				m.put(k,this.getRelevance(k));
			}
		}
		return new WikiSearch(m, this.index);
	}
	
	/**
	 * Computes the relevance of a search with multiple terms.
	 * 
	 * @param rel1: relevance score for the first search
	 * @param rel2: relevance score for the second search
	 * @return
	 */
	protected double totalRelevance(Double rel1, Double rel2) {
		// simple starting place: relevance is the sum of the term frequencies.
		return rel1 + rel2;
	}

	/**
	 * Sort the results by relevance.
	 * 
	 * @return List of entries with URL and relevance.
	 */
	public List<Entry<String, Double>> sort() {
        // FILL THIS IN!
		    Comparator<Entry<String, Double>> comparator = new Comparator<Entry<String, Double>>() {
				public int compare(Entry<String,Double> e1, Entry<String,Double> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			};
			Map<String, Double> URLToRel = new HashMap<String, Double>();
			for(String u : map.keySet()){
				URLToRel.put(u,getRelevance(u));
			}
			List<Entry<String,Double>> lst=new LinkedList<Entry<String,Double>>();
			lst.addAll(URLToRel.entrySet());
			Collections.sort(lst,comparator);
			return lst;
	}
	
	
}