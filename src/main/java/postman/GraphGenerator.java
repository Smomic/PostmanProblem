package postman;

import java.util.concurrent.ThreadLocalRandom;

/** 
 * 10. Listonosz
 */

public class GraphGenerator {

	private boolean adjacencyMatrix[][];
	private int numOfVertices;

	GraphGenerator(int n, double probability) throws Exception {
		numOfVertices = n;
		adjacencyMatrix = new boolean[numOfVertices][numOfVertices];

		if (numOfVertices <= 1 || probability < 0 || probability > 1)
			throw new Exception();

		generateSpanningTree();
		generateRandomEdges(probability);
	}

	private void generateSpanningTree() {
		for (int i = 1; i < numOfVertices; ++i) {
			int randomNum = ThreadLocalRandom.current().nextInt(0, i);
			addToMatrix(i, randomNum);
		}
	}

	private void addToMatrix(int n, int m) {
		adjacencyMatrix[n][m] = true;
		adjacencyMatrix[m][n] = true;
	}

	private void generateRandomEdges(double probability) {
		int possibleEdges = (numOfVertices * numOfVertices - numOfVertices - 2 * (numOfVertices - 1)) / 2;
		int addEdges = (int) (possibleEdges * probability);
		for (int i = 0; i < addEdges;) {
			int vertice1 = ThreadLocalRandom.current().nextInt(0, numOfVertices);
			int vertice2 = ThreadLocalRandom.current().nextInt(0, numOfVertices);
			if (vertice1 != vertice2 && !checkEdge(vertice1, vertice2)) {
				addToMatrix(vertice1, vertice2);
				i++;
			}
		}
		
	}

	private boolean checkEdge(int n, int m) {
		return adjacencyMatrix[n][m];
	}

	public boolean[][] getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
}
