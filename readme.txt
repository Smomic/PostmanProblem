 *TEMAT:
 10. Listonosz: Dany jest graf nieskierowany G(V,E), podzbiór krawędzi F⊂E. Odnaleźć możliwie najkrótszy cykl 
 w grafie, który zawiera każdą krawędź co najmniej raz. Należy porównać rozwiązanie dokładne z heurystyką.

 *OBSŁUGA
 Po uruchomieniu programu w oknie konsoli pojawiają się opcje menu z wyborem trybu wykonania programu:
  (1) przy użyciu danych z pliku tekstowego, służący do testowania poprawności dla małych instancji
  (2) przy użyciu danych generowanych automatycznie z parametryzacją, także służący do testowania poprawności
  (3) wykonanie z generacją danych, pomiarem czasu i prezentacją wyników pomiarów
  (0) wyjście z programu 
  Pierwsze dwa tryby wykonują obliczenia najpierw dla algorytmu dokładnego, a następnie dla heurystyki.
  Z kolei trzeci tryb pozwala na wybór algorytmu do sprawdzenia, co jest pomocne podczas badania złożoności 
  dla rozwiązania przybliżonego.
 
 *DANE WEJŚCIOWE
 W przypadku danych pobieranych w pliku tekstowego, w pierwszej linii powinna znaleźć się liczba wierzchołków grafu, 
 a w każdej kolejnej dwie wartości wierzchołków odpowiadające łączącej je krawędzi, z wyłączeniem przypadku multigrafu.
 Z kolei w przypadku danych generowanych automatycznie wymagane jest podanie ilości wierzchołków (liczba całkowita) 
 oraz prawdopodobieństwa wystąpienia krawędzi między wierzchołkami (liczba zmiennoprzecinkowa z przedziału [0,1]).
 
 *PREZENTACJA WYNIKÓW
 W pierwszych dwóch trybach programu przyjęto, że wyświetlana jest informacja, czy rozwiązanie przybliżone jest zgodne 
 z rozwiązaniem dokładnym, a jeśli nie, to jaka jest procentowa wartość odchylenia. Dodatkowo, jeśli ilość wierzchołków 
 jest mniejsza od 50 to prezentowana jest sekwencja szukanego cyklu. Co więcej, dla trybu danych generowanych 
 automatycznie wyświetlany jest również czas wykonanie każdego z algorytmów.
 
 *KRÓTKI OPIS METOD ROZWIĄZANIA
 Algorytm dokładny:
 Na początku następuje sprawdzenie czy graf jest spójny, bo tylko dla takich znalezienie cyklu jest możliwe.
 Następnie wyszukiwane są wierzchołki stopnia nieparzystego i wyznaczane są najkrótsze ścieżki, łączące te wierzchołki.
 W dalszej kolejności wyszukiwane jest minimalne skojarzenie dla wierzchołków nieparzystych i dla tak wyznaczonych 
 ścieżek dublowane są krawędzie. Ostatecznie wykorzystując przeszukiwanie w głąb, wyznaczany jest szukany cykl w grafie.
 
 Algorytm przybliżony:
 Metoda polega na stworzeniu małego cyklu w grafie i rozbudowywaniu go o kolejne podcykle, tak aby przejść po wszystkich 
 krawędziach. Podczas przechodzenia po grafie zapamiętywane są wierzchołki startowe, czyli takie, od których zaczął się 
 cykl lub podcykl i do których trzeba wrócić, bo go zamknąć. Ponadto ścieżki pomiędzy tym samym wierzchołkiem startowym są 
 budowane tak, aby były jak najdłuższe. Aby możliwy był powrót do wierzchołka startowego, zapamiętywane są wartości 
 wierzchołków poprzednich podczas przechodzenia po grafie. Dodatkowo algorytm eliminuje przypadki, kiedy podczas przechodzenia 
 po wierzchołkach poprzednich napotkana zostanie pętla, co jest szczególnie częste w grafach "rzadkich".
 
 Generator grafu:
 Budowanie grafu odbywa się w pierwszej kolejności na stworzeniu losowego drzewa rozpinającego, tak, aby otrzymać graf spójny.
 Następnie losowane są wierzchołki, pomiędzy którymi należy dodać krawędź, eliminując sytuacje, gdzie powstałby multigraf.
 
 *INFORMACJE O FUNKCJONALNEJ DEKOMPOZYCJI PROGRAMU
 Program podzielony został na pliki źródłowe zgodnie z konwencją języka Java:
 AlgorithmTestCourse.java		- klasa implementująca tryb nr 3
 Application.java			- główna część programu
 Constant.java				- klasa zawierająca stałe użyte w projekcie
 FileCourse.java			- klasa implementująca tryb nr 1
 Graph.java				- klasa implementująca graf
 GraphGenerator.java			- klasa implementująca generator grafu
 Postman.java				- klasa implementująca wykorzystywane algorytmy
 RandomCourse				- klasa implementująca tryb nr 2
 Warmup.java				- klasa odpowiadająca za rozgrzanie maszyny