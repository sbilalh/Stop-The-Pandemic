import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class StopContagion {

    // removing based on highest degree
    public static void removeDegree(ArrayList<ArrayList<Integer>> graph, int numNodes) {

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
                int curr = graph.get(nodeToRemove).get(j);
                for (int k = 0; k < graph.get(curr).size(); k++) {
                    if (graph.get(curr).get(k) == nodeToRemove) {
                        graph.get(curr).remove(k);
                    }
                }
            }
            graph.set(nodeToRemove, new ArrayList<>());

        }
    }

    // removing based on collective influence
    public static void removeInfluence(ArrayList<ArrayList<Integer>> graph, int numNodes) {

    }

    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    public static int[] BFS(ArrayList<ArrayList<Integer>> graph, int src, int v, int[] pred, int[] dist) {
        
        LinkedList<Integer> queue = new LinkedList<Integer>();
        boolean visited[] = new boolean[v];

        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        visited[src] = true;
        dist[src] = -1;
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < graph.get(u).size(); i++) {
                if (visited[graph.get(u).get(i)] == false) {
                    visited[graph.get(u).get(i)] = true;
                    dist[graph.get(u).get(i)] = dist[u] + 1;
                    pred[graph.get(u).get(i)] = u;
                    queue.add(graph.get(u).get(i));
                }
            }
        }
        return dist;
    }


    // returns an array of the degree of the node
    public static int[] degreeOfNodes(ArrayList<ArrayList<Integer>> graph){

        int[] output = new int[graph.size()];
        
        for(int i = 0; i < graph.size(); i++){
            output[i] = graph.get(i).size();
        }

        return output;
    }


    // calculates collective influence of each node (puts it into array) returns node of highest influence
    public static Integer collectiveInfluence(ArrayList<ArrayList<Integer>> graph) {

    }


    // testing print method
    public static void print(ArrayList<ArrayList<Integer>> graph) {
        for (int i = 0; i < graph.size(); i++) {
            System.out.print(i + "->");
            for (int j = 0; j < graph.get(i).size(); j++) {
                System.out.print(graph.get(i).get(j) + ", ");

            }
            System.out.println();
        }
    }

    // creating the graph
    public static void createGraph(File f, ArrayList<ArrayList<Integer>> l) throws FileNotFoundException {
        String line;
        Scanner sc = new Scanner(f);

        while (sc.hasNextLine()) {

            // getting input
            line = sc.nextLine();
            String[] split = line.split("\\s+");

            if (split[0] != null && split[1] != null) {
                
                // adding values to adjancency list (graph)
                int index = Integer.parseInt(split[0]);
                int edge = Integer.parseInt(split[1]);
                l.get(index).add(edge);
                l.get(edge).add(index);
            }
            split = null;
        }
        sc.close();

    }

    public static void main(String[] args) throws FileNotFoundException {

        String input = args[0];
        File file = new File(input);
        Scanner scnr = new Scanner(file);

        // reads lines of input to know size of graph
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null)
                lines++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // this will be the graph
        ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>(lines+1);
        for (int i = 0; i < lines+1; i++) {
            graph.add(new ArrayList<>());
        }
        scnr.close();
        createGraph(file, graph);
        
        print(graph);

        // testing BFS
        int[]output = new int[15];
        int[] pred = new int[15];
        output = BFS(graph, 4, 15, pred, output);
        System.out.println("final output:");
        for(int i=1; i<output.length; i++){
            System.out.println("index "+i+": "+output[i]);
        }
    }
}
