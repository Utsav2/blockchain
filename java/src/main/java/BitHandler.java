/**
 * Serves
 * Created by utsav on 11/16/15.
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class BitHandler extends AbstractHandler {

    BitGraph bitGraph;

    public BitHandler(String filepath) {
        try {
            bitGraph = new GraphParser().parse(filepath);
        }
        catch (FileNotFoundException e) {
            System.err.println("No such file");
            System.exit(1);
        }
    }

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Access-Control-Allow-Origin", "*");
        String hash = baseRequest.getParameter("hash");
        Set<String> result = bitGraph.getClosure(hash);
        Gson gson = new Gson();
        String ret;
        if (result == null) {
            ret = gson.toJson(new ArrayList<String>());
        } else {
            ret = gson.toJson(result);
        }
        baseRequest.setHandled(true);
        response.getWriter().println(ret);
    }

    public static void printUsage() {
        System.out.println("First argument - Port number.");
        System.out.println("Second argument - csv edge list filepath to parse");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        Server server = new Server(Integer.parseInt(args[0]));
        server.setHandler(new BitHandler(args[1]));

        server.start();
        server.join();
    }
}

