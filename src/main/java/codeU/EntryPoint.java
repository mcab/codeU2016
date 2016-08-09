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
        for (Map.Entry<String, Double> url : entries) {
            sb.append(url.getKey());
            sb.append("<br>");
        }
        return "<h2>Results for the word " + search + "<br>" + sb + "</h2>";
        //return "<h2>You searched " + search + "</h2>";
    }

    //http://localhost:8080/home/hello
    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        return "Hello, World!";
    }

}



