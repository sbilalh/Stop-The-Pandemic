import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

    // returns the shortest path array for a given node
    //length initially should be 0
    public static void shortestPath(ArrayList<ArrayList<Integer>> graph, Integer start, int[] output, int length, int index) {

        // base case
        if ((graph.get(start).size()-1 == index) && output[graph.get(start).get(index)] != -1) {
            return;
        }
        else {
            if (graph.get(start).size() != index && output[graph.get(start).get(index)] == -1){
               
                output[graph.get(start).get(index)] = length;
                shortestPath(graph, graph.get(start).get(index), output, length+1, 0);
                
            }else{
                
                    shortestPath(graph, start, output, length, index+1);
            }
        
        }

    }

    // changes shortest path of all nodes with radius 1 to 0 (because r-1 is required)
     public static void shortestPathUtility(ArrayList<ArrayList<Integer>> graph, Integer start, int[] output) {
         for (int i = 0; i < graph.get(start).size(); i++) {
             output[graph.get(start).get(i)] = 0;
         }
    }


    // returns an array of the degree of the node
    public static int[] degreeOfNodes(ArrayList<ArrayList<Integer>> graph){

        int[] output = new int[graph.size()];
        
        for(int i = 0; i < graph.size(); i++){
            output[i] = graph.get(i).size();
        }

        return output;
    }


    // // calculates collective influence of each node (puts it into array) returns node of highest influence
    // public static Integer collectiveInfluence(ArrayList<ArrayList<Integer>> graph) {

    // }


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
        ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>(lines+1);
        for (int i = 0; i < lines+1; i++) {
            l.add(new ArrayList<>());
        }

        scnr.close();

        createGraph(file, l);
        print(l);
        //System.out.println("now removing");
        //removeDegree(l, 1);
       // print(l);


        // l.get(1).add(2);
        // l.get(2).add(1);

        // l.get(1).add(3);
        // l.get(3).add(1);

        // l.get(2).add(4);
        // l.get(4).add(2);

        // l.get(2).add(5);
        // l.get(5).add(2);

        // l.get(3).add(6);
        // l.get(6).add(3);

        // l.get(4).add(5);
        // l.get(5).add(4);


        int[]output = new int[8];
        for (int i =0; i<output.length; i++) {
            output[i]=-1;
        }
        
        for (int i = 0; i < l.get(4).size(); i++){   
            shortestPath(l, 4, output,0, i);
        }
        shortestPathUtility(l, 4, output);
        
        
        for(int i=1; i<output.length; i++){
            System.out.println("index "+i+": "+output[i]);
        }
    }
}
