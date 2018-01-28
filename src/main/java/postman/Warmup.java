package postman;

/**
 * 10. Listonosz
 */

public class Warmup {
	
	public static void run(int numOfVertices) {
		GraphGenerator generator = null;
		try {
			generator = new GraphGenerator(numOfVertices, 1);
		} catch (Exception e) {
		}
		Postman postman = new Postman(numOfVertices);
		postman.createGraph(generator.getAdjacencyMatrix());
		postman.getExactCycle(numOfVertices);
	}
}
