package com.yulius.belitungtourism.algorithm;

import java.util.ArrayList;
import java.util.Stack;

public class TSPNearestNeighbour {
    private int numberOfNodes;
    private Stack<Integer> stack;

    public TSPNearestNeighbour(){
        stack = new Stack<Integer>();
    }

    public ArrayList<Integer> tsp(double adjacencyMatrix[][]) {
        ArrayList<Integer> result = new ArrayList<>();
        numberOfNodes = adjacencyMatrix[1].length;
        double[] visited = new double[numberOfNodes];
        for(int i=0;i<visited.length;i++){
            visited[i] = -1;
        }

        visited[0] = 1;
        stack.push(0);
        result.add(0);
        int element, dst = 0, i;
        double min = Integer.MAX_VALUE;
        boolean minFlag = false;
        System.out.println("city 0 " + "\t");

        while (!stack.isEmpty()){
            element = stack.peek();
            i = 0;
            min = Double.MAX_VALUE;
            while (i < numberOfNodes) {
                if (adjacencyMatrix[element][i] > 0 && visited[i] == -1){
                    if (min > adjacencyMatrix[element][i]){
                        min = adjacencyMatrix[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag) {
                visited[dst] = 0;
                stack.push(dst);
                result.add(dst);
                System.out.println(" city " + dst + "\t");
                minFlag = false;
                continue;
            }
            stack.pop();
        }
        return result;
    }

}
