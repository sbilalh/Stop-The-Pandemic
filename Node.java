public class Node {
 
    boolean relaxed;
    int value;

    Node(){}

    Node(int v){
        value = v;
    }

    Node(boolean r){
        relaxed = r;
    }

    Node(int v, boolean r){
        value = v;
        relaxed = r;
    }

    public int getValue() {
        return value;
    }

}
