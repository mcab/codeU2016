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
    @Produces("text/html")
    public String postMethod(@FormParam("search") String search) throws IOException, InterruptedException, ExecutionException  {
        final long startTime = System.currentTimeMillis();
        WikiMain w = new WikiMain();
        StringBuilder sb = new StringBuilder();
        List<Map.Entry<String, Double>> entries = w.searcher(new String[]{search});

        if(entries == null) {
            return "There are no search results for " + search;
        }

        for (Map.Entry<String, Double> url : entries) {
            sb.append("\t\t\t\t<h4><li><a href=" + url.getKey() + ">" + cleanURL(url.getKey().substring(30)) + "</a>: " + url.getValue() + "</li></h4>\n");
        }
        final long endTime = System.currentTimeMillis();
        return "<html>\n<head>" +
                "\n\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\">" +
                "\n</head>\n<body>" +
                "\n\t<div class=\"container\">" +
                "\n\t\t<h2>Results for the word " + search + "</h2>" +
                "\n\t\t\t<ol>\n" + sb + "\t\t\t</ol>" +
                "\n\n<h6>This page took " + (endTime - startTime) + " ms to render." +
                "\n\t</div>" +
                "\n</body>\n</html>";
    }
    private String cleanURL(String url){
        String clean = url;
        clean = clean.replace("_", " ");
        clean = clean.replace("%22", "\"");
        clean = clean.replace("%27", "\'");
        clean = clean.replace("%C2%A1", "¡");
        return clean;
    }
}
