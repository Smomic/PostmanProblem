package postman;

/**
 * 10. Listonosz
 */

public class RandomCourse {
	
	public static void run(int numOfVertices, float probability) throws Exception {
		GraphGenerator generator = new GraphGenerator(numOfVertices, probability);
		Postman postman = new Postman(numOfVertices);
		postman.createGraph(generator.getAdjacencyMatrix());
		Warmup.run(Constant.warmupNumOfVertices);
		Application.showResult(postman, true);
	}
}
