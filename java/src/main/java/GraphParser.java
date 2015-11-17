import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * Parses and creates a new Graph from csv file
 * Created by utsav on 11/15/15.
 */

public class GraphParser {

    public BitGraph parse(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        BitGraph g = new BitGraph();
        while (scanner.hasNext()) {
           String [] walletIds = scanner.nextLine().split(",");
           g.addEdge(walletIds[0], walletIds[1]);
        }
        return g;
    }
}
