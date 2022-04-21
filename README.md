# CSC-172-Project-3

## Stop the Contagion!

### Authors: Syed Bilal Hussain (**shussa11**), Tiago Davies (**tdavies5**)


#### Running steps: 

`javac StopContagion.java>`
`java StopContagion "custom args go here"`

> This program takes in a file containing pairs of connected integers on each line. It generates an unweighted, undirected graph (represented as an adjacency list using `ArrayList<ArrayList<Integer>>` and removes a specified number nodes based on either the degree or the collective influence of each node (radius must be given to calculate collective influence). 

> **IMPORTANT NOTE**: Extra Credit has been done to print connected component. If user specifies a -t as the last argument then the program will also output the connected components of the graph (excluding the nodes that are isolated) after the removal of each node. Sample argument to print connected components - `java StopContagion -d 4 destruction_example_1.txt -t`

> **ANOTHER IMPORTANT NOTE**: As mentioned in the project requirements, in order for our program to work the input file must contain nodes that will always be numbers, starting from 1 and increasing by one.
