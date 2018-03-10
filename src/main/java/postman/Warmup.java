package postman;

/**
 * 10. Listonosz
 */

public class Warmup {
	
	public static void run(int numOfVertices) {
		GraphGenerator generator = null;
		try {
			generator = new GraphGenerator(numOfVertices, 1);
		} catch (Exception ignored) {
		}
		Postman postman = new Postman(numOfVertices);
		assert generator != null;
		postman.createGraph(generator.getAdjacencyMatrix());
		postman.getExactCycle(numOfVertices);
	}
}
