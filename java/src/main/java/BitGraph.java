import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Bit Graph
 * Created by utsav on 11/15/15.
 */
public class BitGraph {

    HashMap<String, LinkedHashSet<String>> edgeList;

    public BitGraph() {
        edgeList = new HashMap<String, LinkedHashSet<String>>();
    }

    public Set<String> getClosure(String walletId) {
        return edgeList.get(walletId);
    }

    public void addEdge(String walletId1, String walletId2) {

        if (edgeList.containsKey(walletId1) && edgeList.containsKey((walletId2))) {

            LinkedHashSet<String> wallet1Keys = edgeList.get(walletId1);
            LinkedHashSet<String> wallet2Keys = edgeList.get(walletId2);

            if (wallet1Keys == wallet2Keys) {
                return;
            }

            // Union of these two sets
            // O(n)

            LinkedHashSet<String> newSet = new LinkedHashSet<String>();

            for (String wallet:
                 wallet1Keys) {
                newSet.add(wallet);
            }

            for (String wallet:
                 wallet2Keys) {
                newSet.add(wallet);
            }

            newSet.add(walletId1);
            newSet.add(walletId2);

            for (String wallet:
                 newSet) {
                edgeList.put(wallet, newSet);

            }

        } else if(edgeList.containsKey(walletId1) && !edgeList.containsKey((walletId2))) {
            LinkedHashSet<String> current = edgeList.get(walletId1);
            current.add(walletId2);
            edgeList.put(walletId2, current);

        } else if(!edgeList.containsKey(walletId1) && edgeList.containsKey((walletId2))) {
            LinkedHashSet<String> current = edgeList.get(walletId2);
            current.add(walletId1);
            edgeList.put(walletId1, current);
        }
        else {
            LinkedHashSet<String> set = new LinkedHashSet<String>();
            set.add(walletId1);
            set.add(walletId2);
            edgeList.put(walletId1, set);
            edgeList.put(walletId2, set);
        }
    }
}
