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
        System.out.println("Query Instructions: ");
        System.out.println("term : returns most relevant Wikipedia pages in order for the provided term");
        System.out.println("term1 && term2 : returns Wikipedia pages in order that contain both terms");
        System.out.println("term1 || term2 : returns Wikipedia pages in order that contain either term");
        System.out.println("term1 - term2 : returns Wikipedia pages in order that contain term1 but not term2");
        System.out.println("Enter x to quit");
        while(!searched){
            String query = search[0];
            List<String> words = Arrays.asList(query.split(" "));
            String sourceUrl = "https://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=all&fulltext=Search&search=";
            if(words.size()>2){
                String term1 = words.get(0);
                String term2 = words.get(2);
                sourceUrl+=term1;
                crawl(sourceUrl,words,query, index);
                if(words.get(1).equals("&&")){
                    searched = true;
                    WikiSearch ws = search(term1,index).and(search(term2,index));
                    return ws.sort();
                }else if(words.get(1).equals("||")){
                    searched = true;
                    WikiSearch ws = search(term1,index).or(search(term2,index));
                    return ws.sort();
                }else if(words.get(1).equals("-")){
                    searched = true;
                    WikiSearch ws = search(term1,index).minus(search(term2,index));
                    return ws.sort();

                }else{
                    System.out.println("Try Again!");
                    continue;
                }

            }else if(words.size() == 1){
                sourceUrl+=words.get(0);
                if(words.get(0).equals("x")){
                    break;
                }
                //noinspection Since15
                if(words.get(0).isEmpty()){
                    System.out.println("Try Again!");
                    continue;
                }
                searched = true;
                crawl(sourceUrl, words, query, index);
                WikiSearch ws = search(words.get(0),index);
                System.out.println("-------------------RESULTS-------------------");
                return ws.sort();
            }else{
                System.out.println("Try Again!");
                continue;
            }

        }

        return null;

    }
    /*public static void main(String[] search) throws IOException {
        // TODO Auto-generated method stub
        //setup
        Indexer index = new Indexer();
        boolean searched = false;

        //start prompt
        System.out.println("WIKIPEDIA SEARCH");
        System.out.println("Query Instructions: ");
        System.out.println("term : returns most relevant Wikipedia pages in order for the provided term");
        System.out.println("term1 && term2 : returns Wikipedia pages in order that contain both terms");
        System.out.println("term1 || term2 : returns Wikipedia pages in order that contain either term");
        System.out.println("term1 - term2 : returns Wikipedia pages in order that contain term1 but not term2");
        System.out.println("Enter x to quit");
        while(!searched){
            String query = search[0];
            List<String> words = Arrays.asList(query.split(" "));
            String sourceUrl = "https://en.wikipedia.org/w/index.php?title=Special%3ASearch&profile=all&fulltext=Search&search=";
            if(words.size()>2){
                String term1 = words.get(0);
                String term2 = words.get(2);
                sourceUrl+=term1;
                crawl(sourceUrl,words,query, index);
                if(words.get(1).equals("&&")){
                    searched = true;
                    WikiSearch ws = search(term1,index).and(search(term2,index));
                    ws.print();
                }else if(words.get(1).equals("||")){
                    searched = true;
                    WikiSearch ws = search(term1,index).or(search(term2,index));
                    ws.print();
                }else if(words.get(1).equals("-")){
                    searched = true;
                    WikiSearch ws = search(term1,index).minus(search(term2,index));
                    ws.print();

                }else{
                    System.out.println("Try Again!");
                    continue;
                }

            }else if(words.size() == 1){
                sourceUrl+=words.get(0);
                if(words.get(0).equals("x")){
                    break;
                }
                //noinspection Since15
                if(words.get(0).isEmpty()){
                    System.out.println("Try Again!");
                    continue;
                }
                searched = true;
                crawl(sourceUrl, words, query, index);
                WikiSearch ws = search(words.get(0),index);
                System.out.println("-------------------RESULTS-------------------");
                ws.print();
            }else{
                System.out.println("Try Again!");
                continue;
            }

        }


    }*/

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
