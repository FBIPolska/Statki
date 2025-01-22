import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Statki {

    // Rozmiar planszy i liczba statków
    private static final int ROZMIAR = 5;
    private static final int STATKI = 3;

    // Plansze
    private char[][] planszaGracza;
    private char[][] planszaKomputera;

    // Struktury danych
    private ArrayList<String> strzalyGracza; // Lista strzałów gracza
    private HashSet<String> strzalyKomputera; // Zbiór strzałów komputera
    private Map<String, Boolean> statkiGracza; // Mapa dla statków gracza (współrzędne -> status)

    public Statki() {
        planszaGracza = new char[ROZMIAR][ROZMIAR];
        planszaKomputera = new char[ROZMIAR][ROZMIAR];
        strzalyGracza = new ArrayList<>();
        strzalyKomputera = new HashSet<>();
        statkiGracza = new HashMap<>();

        // Inicjalizacja plansz
        for (int i = 0; i < ROZMIAR; i++) {
            for (int j = 0; j < ROZMIAR; j++) {
                planszaGracza[i][j] = '~'; // Woda
                planszaKomputera[i][j] = '~'; // Woda
            }
        }
    }

    // Funkcja do wyświetlania planszy
    public void wyswietlPlansze(char[][] plansza, boolean pokazStatki) {
        System.out.print("  ");
        for (int i = 0; i < ROZMIAR; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < ROZMIAR; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < ROZMIAR; j++) {
                if (plansza[i][j] == 'S' && !pokazStatki) {
                    System.out.print("~ "); // Ukryj statki
                } else {
                    System.out.print(plansza[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    // Funkcja do ustawiania statków gracza
    public void ustawStatkiGracza() {
        Scanner scanner = new Scanner(System.in);
        int ustawioneStatki = 0;

        System.out.println("Ustaw swoje statki na planszy (wpisz współrzędne x y dla każdego statku):");
        while (ustawioneStatki < STATKI) {
            wyswietlPlansze(planszaGracza, true);
            System.out.print("Podaj współrzędne (x y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (x >= 0 && x < ROZMIAR && y >= 0 && y < ROZMIAR) {
                if (planszaGracza[x][y] == '~') {
                    planszaGracza[x][y] = 'S'; // Ustawienie statku
                    statkiGracza.put(x + "," + y, true); // Dodaj statek do mapy
                    ustawioneStatki++;
                } else {
                    System.out.println("Tutaj już jest statek. Spróbuj ponownie.");
                }
            } else {
                System.out.println("Nieprawidłowe współrzędne.");
            }
        }
    }

    // Funkcja do rozmieszczania statków komputera
    private void rozmiescStatkiKomputera() {
        Random rand = new Random();
        int ustawioneStatki = 0;

        while (ustawioneStatki < STATKI) {
            int x = rand.nextInt(ROZMIAR);
            int y = rand.nextInt(ROZMIAR);
            if (planszaKomputera[x][y] == '~') {
                planszaKomputera[x][y] = 'S'; // Ustawienie statku
                ustawioneStatki++;
            }
        }
    }

    // Funkcja do strzału
    public boolean strzelGracza(int x, int y) {
        String wspolrzedne = x + "," + y;
        if (strzalyGracza.contains(wspolrzedne)) {
            System.out.println("Już strzelałeś w to miejsce. Spróbuj ponownie.");
            return false;  // Gracz nie może strzelać w to samo miejsce
        }
        strzalyGracza.add(wspolrzedne); // Dodaj strzał do listy
        return strzel(planszaKomputera, x, y);
    }

    // Funkcja do strzału komputera
    public boolean strzelKomputera() {
        Random rand = new Random();
        int x, y;
        String wspolrzedne;

        do {
            x = rand.nextInt(ROZMIAR);
            y = rand.nextInt(ROZMIAR);
            wspolrzedne = x + "," + y;
        } while (strzalyKomputera.contains(wspolrzedne)); // Komputer nie strzela w to samo miejsce

        strzalyKomputera.add(wspolrzedne); // Dodaj strzał komputera do zbioru
        return strzel(planszaGracza, x, y);
    }

    // Funkcja wykonująca strzał
    private boolean strzel(char[][] plansza, int x, int y) {
        if (plansza[x][y] == 'S') {
            plansza[x][y] = 'X'; // Trafienie
            return true;
        } else if (plansza[x][y] == '~') {
            plansza[x][y] = 'O'; // Pudło
            return false;
        }
        return false; // W to miejsce już strzelano
    }

    // Funkcja do zarządzania turami gry
    public void tura() {
        Scanner scanner = new Scanner(System.in);
        int trafioneGracza = 0;
        int trafioneKomputera = 0;

        while (trafioneGracza < STATKI && trafioneKomputera < STATKI) {
            // Ruch gracza
            wyswietlPlansze(planszaKomputera, false);
            System.out.print("Podaj współrzędne (x y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (x >= 0 && x < ROZMIAR && y >= 0 && y < ROZMIAR) {
                if (strzelGracza(x, y)) {
                    System.out.println("Trafione!");
                    trafioneGracza++;
                } else {
                    System.out.println("Pudło!");
                }
            } else {
                System.out.println("Nieprawidłowe współrzędne.");
                continue; // Jeśli nieprawidłowe współrzędne, powtarzamy ruch gracza
            }

            // Ruch komputera 
            if (trafioneGracza < STATKI) {
                if (strzelKomputera()) {
                    System.out.println("Komputer trafił!");
                    trafioneKomputera++;
                } else {
                    System.out.println("Komputer pudło!");
                }
            }
        }

        // Wynik gry
        if (trafioneGracza == STATKI) {
            System.out.println("Gratulacje! Zatopiłeś wszystkie statki komputera!");
        } else {
            System.out.println("Komputer zatopił wszystkie Twoje statki. Spróbuj ponownie!");
        }

        // Wyświetlanie ostatecznych plansz
        System.out.println("Ostateczne plansze:");
        System.out.println("Twoja plansza:");
        wyswietlPlansze(planszaGracza, true); // Pokaż statki gracza
        System.out.println("Plansza komputera (ukryte statki):");
        wyswietlPlansze(planszaKomputera, true); // Ukryj statki komputera
    }

    public static void main(String[] args) {
        Statki gra = new Statki(); // Utworzenie nowej gry
        gra.ustawStatkiGracza(); // Gracz ustawia swoje statki
        gra.rozmiescStatkiKomputera(); // Komputer rozmieszcza swoje statki
        gra.tura(); // Rozpoczynanie gry
    }
}
