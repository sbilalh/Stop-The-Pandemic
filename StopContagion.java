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
    public static int[][] removeDegree(ArrayList<ArrayList<Integer>> graph, int numNodes, boolean printCC) {

        if (printCC) {
            System.out.println();
            System.out.println("Before removing any node, following are connected components:");
            connectedComponents(graph);
            System.out.println();
        }

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

            if (printCC) {
                System.out.println("After removing node " + nodeToRemove + ", following are connected components:");
                connectedComponents(graph);
                System.out.println();
            }
        }

        return output;

    }

    // removing based on collective influence
    public static int[][] removeInfluence(ArrayList<ArrayList<Integer>> graph, int numNodes, int r, boolean printCC) {

        if (printCC) {
            System.out.println();
            System.out.println("Before removing any node, following are connected components:");
            connectedComponents(graph);
            System.out.println();
        }

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

            if (printCC) {
                System.out.println("After removing node " + nodeToRemove + ", following are connected components:");
                connectedComponents(graph);
                System.out.println();
            }

        }
        return output;
    }

    // modified version of BFS that returns an array containing the distance of each
    // node from a given source
    public static int[] BFS(ArrayList<ArrayList<Integer>> graph, int src, int v, int[] dist) {

        // initializing queue for BFS algorithm and visited array
        LinkedList<Integer> queue = new LinkedList<Integer>();
        boolean visited[] = new boolean[v];

        // setting initial values for visited array, distance array and predecessor
        // array
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
    public static int[] degreeOfNodes(ArrayList<ArrayList<Integer>> graph) {

        int[] output = new int[graph.size()];

        for (int i = 0; i < graph.size(); i++) {
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
                    sumOfLinks += (degreeOfNodes[j] - 1);
                }
            }
            influenceArray[i] = sumOfLinks * (degreeOfNodes[i] - 1);
        }
        return influenceArray;
    }

    // EXTRA CREDIT: printing connected components of the graph
    public static void DFSUtil(ArrayList<ArrayList<Integer>> graph, int i, boolean[] visited) {
        // Mark the current node as visited and print it
        visited[i] = true;
        if (graph.get(i).size() != 0) {
            System.out.print(i + " ");
        }
        // Recur for all the vertices
        // adjacent to this vertex
        for (int j : graph.get(i)) {
            if (!visited[j])
                DFSUtil(graph, j, visited);
        }
    }

    // EXTRA CREDIT: printing connected components of the graph
    public static void connectedComponents(ArrayList<ArrayList<Integer>> graph) {
        // mark all the vertices as not visited
        boolean[] visited = new boolean[graph.size()];
        for (int i = 1; i < graph.size(); ++i) {
            if (!visited[i]) {
                // print all reachable vertices from i
                DFSUtil(graph, i, visited);
                if (graph.get(i).size() != 0) {
                    System.out.println();
                }
            }
        }
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
        int size = lines + 1;

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
        switch (args[0]) {
            // removing based on degree
            case "-d":
                // if -t given then print connected components
                if (args.length == 4 && args[3].equals("-t")) {
                    // remove based on influence
                    ArrayList<ArrayList<Integer>> graph = createGraph(args[2]);
                    numNodes = Integer.parseInt(args[1]);
                    int[][] output = removeDegree(graph, numNodes, true);
                    System.out.println("Result:");
                    for (int i = 0; i < output.length; i++) {
                        System.out.println(output[i][0] + " " + output[i][1]);
                    }
                    System.out.println();
                }
                // else dont print connected components
                else {
                    // remove based on influence
                    ArrayList<ArrayList<Integer>> graph = createGraph(args[2]);
                    numNodes = Integer.parseInt(args[1]);
                    int[][] output = removeDegree(graph, numNodes, false);
                    System.out.println("Result:");
                    for (int i = 0; i < output.length; i++) {
                        System.out.println(output[i][0] + " " + output[i][1]);
                    }
                    System.out.println();
                }
                break;
            // removing based on collective influence
            case "-r":
                // not given a radius nor -t
                if (args.length == 3) {
                    ArrayList<ArrayList<Integer>> graph = createGraph(args[2]);
                        numNodes = Integer.parseInt(args[1]);
                        r = 2;
                        int[][] output = removeInfluence(graph, numNodes, r, false);
                        System.out.println("Result:");
                        for (int i = 0; i < output.length; i++) {
                            System.out.println(output[i][0] + " " + output[i][1]);
                        }
                        System.out.println();
                }
                else if (args.length == 4) {
                    // if -t given but not radius
                    if (args[3].equals("-t")) {
                        ArrayList<ArrayList<Integer>> graph = createGraph(args[2]);
                        numNodes = Integer.parseInt(args[1]);
                        r = 2;
                        int[][] output = removeInfluence(graph, numNodes, r, true);
                        System.out.println("Result:");
                        for (int i = 0; i < output.length; i++) {
                            System.out.println(output[i][0] + " " + output[i][1]);
                        }
                        System.out.println();
                    } else { // if radius given but not -t
                        ArrayList<ArrayList<Integer>> graph = createGraph(args[3]);
                        numNodes = Integer.parseInt(args[2]);
                        r = Integer.parseInt(args[1]);
                        int[][] output = removeInfluence(graph, numNodes, r, false);
                        System.out.println("Result:");
                        for (int i = 0; i < output.length; i++) {
                            System.out.println(output[i][0] + " " + output[i][1]);
                        }
                        System.out.println();
                    }
                } else {
                    ArrayList<ArrayList<Integer>> graph = createGraph(args[3]);
                    numNodes = Integer.parseInt(args[2]);
                    r = Integer.parseInt(args[1]);
                    int[][] output = removeInfluence(graph, numNodes, r, true);
                    System.out.println("Result:");
                    for (int i = 0; i < output.length; i++) {
                        System.out.println(output[i][0] + " " + output[i][1]);
                    }
                    System.out.println();
                }

                break;
        }
    }

}
