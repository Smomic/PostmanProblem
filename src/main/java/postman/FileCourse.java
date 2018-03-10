package postman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 10. Listonosz
 */

public class FileCourse {

	public static void run(String filePath) throws Exception {
		List<Integer> list = new ArrayList<>();
		readFromFile(filePath, list);

		if (list.isEmpty())
			throw new Exception();

		Postman postman = new Postman(list);
		if (!postman.isConnected())
			throw new Exception();
		Application.showResult(postman, false);
	}

	private static void readFromFile(String filePath, List<Integer> list) throws NumberFormatException, IOException {
			BufferedReader buffer = new BufferedReader(new FileReader(filePath));
			String lineJustFetched;
			String[] wordsArray;
			while (true) {
				if ((lineJustFetched = buffer.readLine()) == null)
					break;

				else {
					wordsArray = lineJustFetched.split(" ");
					for (String i : wordsArray) {
						if (!"".equals(i))
							list.add(Integer.parseInt(i));
					}
				}
			}

			buffer.close();
	}
}
