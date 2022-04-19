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
    public static int[][] removeDegree(ArrayList<ArrayList<Integer>> graph, int numNodes) {

        int[][] output = new int[numNodes][2];
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

            output[i][0] = nodeToRemove;
            output[i][1] = max;

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

        return output;

    }

    // removing based on collective influence
    public static int[][] removeInfluence(ArrayList<ArrayList<Integer>> graph, int numNodes, int r) {

        int[][] output = new int[numNodes][2];
        // outer for loop to remove only numNodes number of nodes
        for (int i = 0; i < numNodes; i++) {

            int[] influenceArray = collectiveInfluence(graph, r);
            // finding node with highest influence
            int max = 0;
            int nodeToRemove = 0;
            for (int j = 0; j < influenceArray.length; j++) {
                if (max < influenceArray[j]) {
                    max = influenceArray[j];
                    nodeToRemove = j;
                }
            }

            output[i][0] = nodeToRemove;
            output[i][1] = max;

            // removing nodeToRemove
            for (int j = 0; j < graph.get(nodeToRemove).size(); j++) {
                int curr = graph.get(nodeToRemove).get(j);
                for (int k = 0; k < graph.get(curr).size(); k++) {
                    if (graph.get(curr).get(k) == nodeToRemove) {
                        graph.get(curr).remove(k);
                    }
                }
            }
            graph.set(nodeToRemove, new ArrayList<>());
        }
        return output;
    }

    // modified version of BFS that returns an array containing the distance of each node from a given source
    public static int[] BFS(ArrayList<ArrayList<Integer>> graph, int src, int v, int[] dist) {
        
        // initializing queue for BFS algorithm and visited array
        LinkedList<Integer> queue = new LinkedList<Integer>();
        boolean visited[] = new boolean[v];

        // setting initial values for visited array, distance array and predecessor array
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
        }

        // setting array values for source node
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // performing BFS and updating values in distance array
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < graph.get(u).size(); i++) {
                if (visited[graph.get(u).get(i)] == false) {
                    visited[graph.get(u).get(i)] = true;
                    dist[graph.get(u).get(i)] = dist[u] + 1;
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

    // calculates and returns an array of collective influence of each node 
    public static int[] collectiveInfluence(ArrayList<ArrayList<Integer>> graph, int r) {
        
        // initializing required arrays
        int[] degreeOfNodes = degreeOfNodes(graph);
        int[] radiusArray = new int[graph.size()];
        int[] influenceArray = new int[graph.size()];
        
        // populating influence array with collective influence of each node
        for (int i = 0; i < influenceArray.length; i++) {
            radiusArray = BFS(graph, i, graph.size(), radiusArray);
            int sumOfLinks = 0;
            for (int j = 0; j < radiusArray.length; j++) {
                if (r == radiusArray[j]) {
                    sumOfLinks += (degreeOfNodes[j]-1);
                }
            }
            influenceArray[i] = sumOfLinks * (degreeOfNodes[i]-1);
        }
        return influenceArray;
    }

    // adding values to the graph
    public static void addValues(File f, ArrayList<ArrayList<Integer>> l) throws FileNotFoundException {
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

    // creates the intial graph
    public static ArrayList<ArrayList<Integer>> createGraph(String input) throws FileNotFoundException {
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

        // size of the graph
        int size = lines+1;

        // this will be the graph
        ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>(size);
        for (int i = 0; i < size; i++) {
            graph.add(new ArrayList<>());
        }
        scnr.close();
        addValues(file, graph);

        return graph;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        int numNodes = 0;
        int r = 2;
        switch (args.length) {
            // removing based on degree if -d given or based on collective influence with default radius of 2
            case 3:
                if (args[0].equals("-d")) {
                    // remove based on influence
                    ArrayList<ArrayList<Integer>> graph = createGraph(args[2]);
                    numNodes = Integer.parseInt(args[1]);
                    int[][] output = removeDegree(graph, numNodes);
                    for (int i = 0; i < output.length; i++) {
                        System.out.println(output[i][0] + " " + output[i][1]);
                    }
                }
                else {
                    //remove based on collective influence with default radius of 2
                    ArrayList<ArrayList<Integer>> graph = createGraph(args[2]);
                    numNodes = Integer.parseInt(args[1]);
                    int[][] output = removeInfluence(graph, numNodes, r);
                    for (int i = 0; i < output.length; i++) {
                        System.out.println(output[i][0] + " " + output[i][1]);
                    }
                }
                break;
            // removing based on collective influence with given radius
            case 4:
                ArrayList<ArrayList<Integer>> graph = createGraph(args[3]);
                numNodes = Integer.parseInt(args[2]);
                r = Integer.parseInt(args[1]);
                int[][] output = removeInfluence(graph, numNodes, r);
                for (int i = 0; i < output.length; i++) {
                    System.out.println(output[i][0] + " " + output[i][1]);
                }
                break;
        }
    }
}
