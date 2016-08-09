package codeU;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Path("/entry-point")
public class EntryPoint {


    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String postMethod(@FormParam("search") String search) throws IOException,InterruptedException,ExecutionException {
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
        return "<h3>Results for the query " + search + "<br>" + sb;
    }

    //http://localhost:8080/home/hello
    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return "Hello, World!";
    }

}



