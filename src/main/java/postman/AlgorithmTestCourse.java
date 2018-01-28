package postman;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 10. Listonosz
 */
public class AlgorithmTestCourse {
	
	private static List<Postman> postmanList = new ArrayList<Postman>();
	private static Postman median;
	private static int TnMedian;
	private static double tnMedian;
	private static Scanner readerAlgorithm = new Scanner(System.in);

	public static void run(int min, int max, int jump, float prob) throws Exception {
		median = null;
		int counter = generateData(min, max, jump, prob);
		int nMedian = calculateMedian(counter);

		int algorithm;
		double exactTimeTab[] = new double[counter];
		double heuristicTimeTab[] = new double[counter];
		double time = 0;
		System.out.println();
		System.out.println("Mediana: " + nMedian);

		do {
			System.out.println();
			System.out.println("   (1) Algorytm dokladny");
			System.out.println("   (2) Algorytm przyblizony");
			System.out.println("   (0) Powrot do menu");
			System.out.println();
			System.out.println("Wybierz testowany algorytm: ");

			readerAlgorithm = new Scanner(System.in);
			algorithm = readerAlgorithm.nextInt();

			switch (algorithm) {
			case 0:
				postmanList.clear();
				break;
			case 1:
			case 2:
				switch (algorithm) {
				case 1:
					testMethod(algorithm, exactTimeTab, counter);
					break;
				case 2:
					testMethod(algorithm, heuristicTimeTab, counter);
					break;
				}
				System.out.println();

				time = (algorithm == 1) ? calculateTimeOfMedian(algorithm, prob, nMedian, exactTimeTab, counter)
						: calculateTimeOfMedian(algorithm, prob, nMedian, heuristicTimeTab, counter);

				if (algorithm == 1)
					showResult(algorithm, exactTimeTab, counter, time);
				else
					showResult(algorithm, heuristicTimeTab, counter, time);

				System.out.println();
				break;
			default:
				break;
			}
		} while (algorithm != 0);

	}

	public static void closeScanner() {
		readerAlgorithm.close();
	}

	private static int asymptote(int algorithm, int n, int e) {
		int d = 2 * e / n * (n - 1);
		switch (algorithm) {
		case 1:
			return n * n * n;
		case 2:
			return d / 2;
		default:
			return 0;
		}
	}

	private static int generateData(int min, int max, int jump, float prob) throws Exception {
		int size = min;
		int counter = 0;
		while (size <= max) {
			for (int i = 0; i < Constant.numOfTests; ++i) {
				GraphGenerator g = new GraphGenerator(size, prob);
				Postman p = new Postman(size);
				p.createGraph(g.getAdjacencyMatrix());
				postmanList.add(p);
			}
			size += jump;
			counter++;
		}
		return counter;
	}

	private static int calculateMedian(int counter) {
		return (counter % 2 == 1) ? postmanList.get(counter * Constant.numOfTests / 2).getNumOfVertices()
				: (postmanList.get(((counter - 1) / 2) * Constant.numOfTests).getNumOfVertices()
						+ postmanList.get(((counter - 1) / 2) * Constant.numOfTests + Constant.numOfTests).getNumOfVertices()) / 2;
	}

	private static void testMethod(int algorithm, double[] timeTab, int counter) {
		Warmup.run(Constant.warmupNumOfVertices);
		double time;
		for (int i = 0; i < counter; ++i) {
			time = 0;
			if (algorithm == 1) {
				for (int k = 0; k < Constant.numOfTests; ++k) {
					postmanList.get(i * Constant.numOfTests + k).getExactCycle(Constant.beginVertice);
					time += postmanList.get(i * Constant.numOfTests + k).getExactTime();
				}
			} else {
				for (int k = 0; k < Constant.numOfTests; ++k) {
					postmanList.get(i * Constant.numOfTests + k).getHeuristicCycle(Constant.beginVertice);
					time += postmanList.get(i * Constant.numOfTests + k).getHeuristicTime();
				}
			}
			timeTab[i] = time / Constant.numOfTests;
			System.out.print(".");
		}
	}

	private static double calculateTimeOfMedian(int algorithm, float prob, int nMedian, double[] timeTab, int counter) throws Exception {
		double time = 0;
		if (counter % 2 == 0) {
			time = 0;
			for (int i = 0; i < Constant.numOfTests; ++i) {
				GraphGenerator gM = new GraphGenerator(nMedian, prob);
				median = new Postman(nMedian);
				median.createGraph(gM.getAdjacencyMatrix());
				if (algorithm == 1) {
					median.getExactCycle(Constant.beginVertice);
					time += median.getExactTime();
				} else {
					median.getHeuristicCycle(Constant.beginVertice);
					time += median.getHeuristicTime();
				}
			}
			tnMedian = time / Constant.numOfTests;
			TnMedian = asymptote(algorithm, nMedian, median.getNumOfEdges());
		} else {
			tnMedian = timeTab[counter / 2];
			TnMedian = asymptote(algorithm, postmanList.get(counter * Constant.numOfTests / 2).getNumOfVertices(),
					postmanList.get(counter * Constant.numOfTests / 2).getNumOfEdges());
		}
		return time;
	}

	private static void showResult(int algorithm, double[] timeTab, int counter, double time) {
		double q;

		System.out.println("tnMediana: " + new DecimalFormat("#0.000").format(tnMedian) + " TnMediana: " + TnMedian);
		System.out.println("n " + "\tt(n)" + "\tq(n)");
		System.out.println("----------------------------------");

		for (int j = 0; j < counter; ++j) {
			double Tn = (double) asymptote(algorithm, postmanList.get(j * Constant.numOfTests).getNumOfVertices(),
					postmanList.get(j * Constant.numOfTests).getNumOfEdges());
			q = (timeTab[j] * TnMedian) / (Tn * tnMedian);

			System.out.print(postmanList.get(j * Constant.numOfTests).getNumOfVertices() + "  \t");
			System.out.print(new DecimalFormat("#0.000").format(timeTab[j]) + " \t");
			System.out.println(new DecimalFormat("#0.000").format(q));

			if (counter % 2 == 0 && j == counter / 2 - 1)
				showMedian(algorithm, time);
		}
	}

	private static void showMedian(int algorithm, double time) {
		double q;
		System.out.print(median.getNumOfVertices() + "  \t");
		System.out.print(new DecimalFormat("#0.000").format(time / Constant.numOfTests) + "  \t");
		double Tnn = (double) asymptote(algorithm, median.getNumOfVertices(), median.getNumOfEdges());
		q = ((time / Constant.numOfTests) * TnMedian) / (Tnn * tnMedian);
		System.out.println(new DecimalFormat("#0.000").format(q));
	}
}
