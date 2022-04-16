import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class StopContagion {

    // removing based on highest degree
    public static void removeDegree(ArrayList<ArrayList<Node>> graph, int numNodes) {

        // outer for loop to remove only numNodes number of nodes
        for (int i = 0; i < numNodes; i++) {

            int max = 0;
            int nodeToRemove = 0;

            // finds node with max degree
            for (int j = 0; j < graph.size(); j++) {
                if (graph.get(j).size() > max) {
                    max = graph.get(j).size();
                    nodeToRemove = j;
                }
            }

            // removing nodeToRemove
            for (int j = 0; j < max; j++) {
                int curr = graph.get(nodeToRemove).get(j).getValue();
                for (int k = 0; k < graph.get(curr).size(); k++) {
                    if (graph.get(curr).get(k).getValue() == nodeToRemove) {
                        graph.get(curr).remove(k);
                    }
                }
            }
            graph.set(nodeToRemove, new ArrayList<>());

        }
    }

    // removing based on collective influence
    public static void removeInfluence(ArrayList<ArrayList<Node>> graph, int numNodes) {

    }

    // testing print method
    public static void print(ArrayList<ArrayList<Node>> graph) {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + "->");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j).getValue() + ", ");
                
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        String line;
        String input = args[0];
        File file = new File(input);
        Scanner sc = new Scanner(file);

        ArrayList<ArrayList<Node>> l = new ArrayList<ArrayList<Node>>(100);

        for(int i =0; i < 100; i++){
            l.add(new ArrayList<>());
        }

        while (sc.hasNextLine()) {
            // getting input
            line = sc.nextLine();
            String[] split = line.split("\\s+");

            if (split[0] != null && split[1] != null) {
                // adding values to adjancency list (graph)
                int index = Integer.parseInt(split[0]);
                int edge = Integer.parseInt(split[1]);
                l.get(index).add(new Node(edge));
                l.get(edge).add(new Node(index));
            }
            split = null;
        }

        print(l);
        System.out.println("now removing");
        removeDegree(l, 1);
        print(l);
    }
}
