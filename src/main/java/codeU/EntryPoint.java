package codeU;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/entry-point")
public class EntryPoint {
    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("search") String search) throws IOException {
        WikiMain w = new WikiMain();
        StringBuilder sb = new StringBuilder();
        List<Map.Entry<String, Double>> entries = w.searcher(new String[]{search});

        if(entries == null) {
            return "There are no search results for " + search;
        }

        int urlNumber = 1;
        for (Map.Entry<String, Double> url : entries) {
            sb.append(urlNumber + ") <a href=" + url.getKey() + ">" + cleanURL(url.getKey().substring(30)) + "</a><br>\n");
            urlNumber += 1;
        }
        return "<h2>Results for the word " + search + "<br>" + sb;
    }
    private String cleanURL(String url){
        String clean = url;
        clean = clean.replace("_", " ");
        clean = clean.replace("%22", "\"");
        clean = clean.replace("%27", "\'");
        return clean;
    }
}