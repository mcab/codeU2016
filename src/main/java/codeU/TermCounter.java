package codeU;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

/**
 * Encapsulates a map from search term to frequency (count).
 * 
 * @author downey
 * 
 */
public class TermCounter {

	private Map<String, Integer> map;
	private String label;
	//private List<String> terms;
	//private List<String> stopwords;

	public TermCounter(String label, List<String> terms) {
		this.label = label;
		this.map = new HashMap<String, Integer>();
		//this.terms = terms;

	}

	public String getLabel() {
		return label;
	}

	/**
	 * Returns the total of all counts.
	 * 
	 * @return
	 */
	public int size() {
		int total = 0;
		for (Integer value : map.values()) {
			total += value;
		}
		return total;
	}

	/**
	 * Takes a collection of Elements and counts their words.
	 * 
	 * @param paragraphs
	 */
	public void processElements(Elements paragraphs) {
		for (Node node : paragraphs) {
			processTree(node);
		}
	}

	/**
	 * Finds TextNodes in a DOM tree and counts their words.
	 * 
	 * @param root
	 */
	public void processTree(Node root) {
		// NOTE: we could use select to find the TextNodes, but since
		// we already have a tree iterator, let's use it.
		for (Node node : new WikiNodeIterable(root)) {
			if (node instanceof TextNode) {
				processText(((TextNode) node).text());
			}
		}
	}

	/**
	 * Splits `text` into words and counts them.
	 * 
	 * @param text
	 *            The text to process.
	 */
	public void processText(String text) {
		// replace punctuation with spaces, convert to lower case, and split on
		// whitespace
		String[] array = text.replaceAll("\\pP", " ").toLowerCase()
				.split("\\s+");

		for (int i = 0; i < array.length; i++) {
			String term = array[i];
			if(!term.isEmpty())
				incrementTermCount(term);
			
		}
	}

	/**
	 * Increments the counter associated with `term`.
	 * 
	 * @param term
	 */
	public void incrementTermCount(String term) {
		//System.out.println(term);
		if(map.containsKey(term))	
			map.put(term, map.get(term) + 1);
		else
			map.put(term,1);
	}
	
	/**
	 * Returns the count associated with this term, or 0 if it is unseen.
	 * 
	 * @param term
	 * @return
	 */
	public Integer get(String term) {
		Integer count = map.get(term);
		if(count != null)
			return count;
		else
			return 0;
	}
	
	/**
	 * Returns the set of terms that have been counted.
	 * 
	 * @return
	 */
	public Set<String> keySet() {
		return map.keySet();
	}

	/**
	 * Print the terms and their counts in arbitrary order.
	 */
	public void printCounts() {
		for (String key : keySet()) {
			Integer count = map.get(key);
			System.out.println(key + ", " + count);
		}
		System.out.println("Total of all counts = " + size());
	}

}