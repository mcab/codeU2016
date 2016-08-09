package codeU;

/**
 * Created by Sonam on 8/4/2016.
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WikiMain {

    public List<Map.Entry<String, Double>> searcher(String[] search) throws IOException {
        Indexer index = new Indexer();
        boolean searched = false;

        //start prompt
        System.out.println("WIKIPEDIA SEARCH");
        System.out.println("Searching for: " + search[0]);
        while(!searched){
            String query = search[0];
            List<String> words = Arrays.asList(query.split(" "));
            String sourceUrl = "https://en.wikipedia.org/w/index.php?title=Special:Search&profile=advanced&profile=advanced&fulltext=Search&search=";
            if (words.size() > 2){
                String term1 = words.get(0);
                String term2 = words.get(2);
                sourceUrl += term1;
                crawl(sourceUrl, words, query, index);
                if(words.get(1).equals("&&")){
                    searched = true;
                    WikiSearch ws1 = search(term1,index);
                    WikiSearch ws2 = search(term2,index);
                    if(ws1 == null || ws2 == null){
                        return null;
                    }else {
                        WikiSearch ws = ws1.and(ws2);
                        return ws.sort();
                    }
                }else if(words.get(1).equals("||")){
                    searched = true;
                    WikiSearch ws1 = search(term1,index);
                    WikiSearch ws2 = search(term2,index);
                    if(ws1 == null || ws2 == null){
                        return null;
                    }else {
                        WikiSearch ws = ws1.or(ws2);
                        return ws.sort();
                    }
                }else if(words.get(1).equals("-")){
                    searched = true;
                    WikiSearch ws1 = search(term1,index);
                    WikiSearch ws2 = search(term2,index);
                    if(ws1 == null || ws2 == null){
                        return null;
                    }else {
                        WikiSearch ws = ws1.minus(ws2);
                        return ws.sort();
                    }
                }else{
                    System.out.println("Try Again!");
                    continue;
                }
            }else if(words.size() == 1){
                sourceUrl+=words.get(0);
                searched = true;
                crawl(sourceUrl, words, query, index);
                WikiSearch ws = search(words.get(0), index);
                if(ws == null){
                    return null;
                }
                System.out.println("-------------------RESULTS-------------------");
                return ws.sort();
            }else{
                System.out.println("Try Again!");
                continue;
            }

        }
        return null;
    }


    private static String query(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a query or type x to quit: ");
        if(sc.hasNextLine()){
            String line = sc.nextLine();
            return line;
        }else{
            return null;
        }
    }

    private static WikiSearch search(String term, Indexer index){
        Map<String, Integer> m = index.getCounts(term);
        if(m == null){
            return null;
        }
        Map<String, Double> map = new HashMap<String, Double>();
        for(String key: m.keySet()){
            map.put(key,new Double(m.get(key)));
        }
        //System.out.println(map);
        //System.out.println(index.numTerms);
        return new WikiSearch(map,index);
    }

    private static void crawl(String url, List<String> terms,String query, Indexer index)throws IOException{
        WikiCrawler wc = new WikiCrawler(url, query, terms, index);
        wc.setQueue();
        wc.crawl();
    }
}
