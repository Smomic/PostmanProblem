package postman;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 10. Listonosz
 */

public class Application {

	public static void main(String args[]) {
		int choice;
		do {
			System.out.println("Tryby wykonania programu: ");
			System.out.println("   (1) dane dostarczone z pliku");
			System.out.println("   (2) dane generowane automatycznie");
			System.out.println("   (3) wykonanie z prezentacja wynikow pomiarow");
			System.out.println("   (0) wyjdz z programu");
			System.out.println();
			System.out.print("Wybierz tryb wykonania: ");
			Scanner readerFilePath = new Scanner(System.in);
			Scanner readerMin = new Scanner(System.in);
			Scanner readerMax = new Scanner(System.in);
			Scanner readerJump = new Scanner(System.in);
			Scanner readerProb = new Scanner(System.in);
			Scanner readerChoice = new Scanner(System.in);
			choice = readerChoice.nextInt();

			switch (choice) {
			case 1:
				System.out.print("Wprowadz sciezke do pliku .txt: ");
				String filePath = readerFilePath.nextLine();
				System.out.println();

				try {
					FileCourse.run(filePath);
				} catch (NumberFormatException | IOException e) {
					System.out.println("Brak pliku o podanej sciezce!");
				} catch (Exception e) {
					System.out.println("Graf nie spojny!");
				}
				break;

			case 2:
				System.out.print("Wprowadz liczbe wierzcholkow: ");
				Scanner readerNumber = new Scanner(System.in);
				Scanner readerProbability = new Scanner(System.in);

				try {
					int numOfVertices = readerNumber.nextInt();
					if (numOfVertices < 2)
						throw new Exception();

					System.out.print("Wprowadz prawdopodobienstwo krawedzi miedzy wierzcholkami (przedzial [0,1]): ");
					String probability = readerProbability.nextLine();
					System.out.println();
					RandomCourse.run(numOfVertices, Float.parseFloat(probability));

				} catch (Exception e) {
					System.out.println("Niepoprawny format danych!");
				}

				break;

			case 3:
				System.out.print("Wprowadz minimalna liczbe wierzcholkow: ");
				try {
					int min = readerMin.nextInt();
					if (min < 2)
						throw new Exception();

					System.out.print("Wprowadz maksymalna liczbe wierzcholkow: ");
					int max = readerMax.nextInt();
					if (max < 2 || max < min)
						throw new Exception();

					System.out.print("Wprowadz skok: ");
					int jump = readerJump.nextInt();
					if (jump < 0)
						throw new Exception();

					System.out.print("Wprowadz prawdopodobienstwo krawedzi miedzy wierzcholkami (przedzial [0,1]): ");
					String prob = readerProb.nextLine();

					AlgorithmTestCourse.run(min, max, jump, Float.parseFloat(prob));

				} catch (Exception e) {
					System.out.println("Niepoprawny format danych!");
				}
				break;

			case 0:
				System.out.println("Zamykam program...");
				AlgorithmTestCourse.closeScanner();
				readerFilePath.close();
				readerMin.close();
				readerMax.close();
				readerJump.close();
				readerProb.close();
				readerChoice.close();
				break;

			default:
				System.out.println("Operacja jest niemozliwa do wykonania!");
				break;
			}

			System.out.println();

		} while (choice != 0);

	}

	public static void showResult(Postman postman, boolean timeCounting) {
		if (timeCounting)
			Warmup.run(Constant.warmupNumOfVertices);

		int exactSize = Application.showResultOfMethod(postman, 1, timeCounting);
		int heuristicSize = Application.showResultOfMethod(postman, 2, timeCounting);
		float inacurrency = calculateInacurrency(exactSize, heuristicSize);
		System.out.println();
		System.out.println((inacurrency == 0) ? "dlugosc cykli identyczna"
				: new DecimalFormat("#0.00").format(inacurrency) + "% odchylenia");
	}

	private static int showResultOfMethod(Postman postman, int algorithm, boolean timeCounting) {
		LinkedList<Integer> cycle = (algorithm == 1) ? postman.getExactCycle(Constant.beginVertice)
				: postman.getHeuristicCycle(Constant.beginVertice);

		System.out.print((algorithm == 1) ? "Rozwiazanie dokladne    - " : "\nRozwiazanie przyblizone - ");

		if (timeCounting)
			showTime(postman, algorithm);

		if (postman.getNumOfVertices() <= Constant.shownNumOfVertices)
			showSequenceOfCycle(cycle, algorithm);

		return cycle.size();
	}

	private static void showTime(Postman postman, int algorithm) {
		System.out.print("czas: ");
		System.out.print((algorithm == 1) ? String.format("%.3f", postman.getExactTime())
				: String.format("%.3f", postman.getHeuristicTime()));
		System.out.print("s, ");
	}

	private static void showSequenceOfCycle(LinkedList<Integer> cycle, int algorithm) {
		System.out.print("cykl: ");
		Iterator<Integer> iter = (algorithm == 1) ? cycle.listIterator() : cycle.listIterator();
		while (iter.hasNext()) {
			System.out.print(iter.next() + (iter.hasNext() ? " > " : "  "));
		}
	}

	public static float calculateInacurrency(int exactSize, int heuristicSize) {
		if (heuristicSize <= exactSize)
			return 0;
		else {
			return (((float) (heuristicSize - exactSize) / exactSize)) * 100;
		}
	}
}