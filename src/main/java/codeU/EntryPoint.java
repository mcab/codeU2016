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
        //String s = "";
        List<Map.Entry<String, Double>> entries = w.searcher(new String[]{search});

        if(entries == null){
            return "There are no search results for " + search;
        }

        for (Map.Entry<String, Double> url : entries) {
            sb.append("<a href= " + url.getKey() + ">" + url.getKey() + "</a>");
            //s += url.getKey();
            sb.append("<br>");
        }
        //window.open("data:text/html;charset=utf-8,"+html, "", "_blank")
        return "<h2>Results for the word " + search + "<br>" + sb;
    }
}
