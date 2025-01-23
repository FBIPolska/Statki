import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Main 
{
    public static void main(String[] args) 
    {
        Statki gra = new Statki();
        gra.ustawStatkiGracza();
        gra.ustawStatkiKomputera();
        gra.rozgrywka();
    }
}
        
        class Statki 
        {
        
            private char[][] planszagracza;
            private char[][] planszakomputera;
            private ArrayList<String> strzalygracza;
            private HashSet<String> strzalykomputera;
            private Map<String, Boolean> statkigracza;
            private static final int rozmiar = 7; // Rozmiar planszy
            private static final int statki = 4; // Liczba statków
        
            public Statki() {
                System.out.println("Zasady");
                System.out.println("1. Plansza składa się z pola 6x6");
                System.out.println("2. Każdy gracz posiada 4 statki");
                System.out.println("3. Rozmiar statku to 1x1");
                System.out.println("4. Zakazuje się ustawiania statków jeden na drugim ");
                System.out.println("5. Zakazuje się strzelania w to samo miejsce 2 razy w przecinym wypadku gracz traci turę");
                System.out.println("6. Przy braku podania współrzędnych i wykonaniu akcji istnieje ryzyko iż gra przestanie działać poprawnie więc zaleca się wpisywanie poprawnie współrzędnych");
                System.out.println("7. Przy wpisaniu błędnych współrzędnych gracz powtarza swoją turę");
                System.out.println("8. Warunkiem wygrania jest zatopienie wrogich okrętów zanim przeciwnik zrobi to pierwszy");
        
                planszagracza = new char[rozmiar][rozmiar];
                planszakomputera = new char[rozmiar][rozmiar];
                strzalygracza = new ArrayList<>();
                strzalykomputera = new HashSet<>();
                statkigracza = new HashMap<>();
        
                // Inicjalizacja planszy
                for (int i = 1; i < rozmiar; i++) 
                {
                    for (int j = 1; j < rozmiar; j++) 
                    {
                        planszagracza[i][j] = '~'; // Woda
                        planszakomputera[i][j] = '~';
                    }
                }
            }
        
            public void wyswietlPlansze(char[][] plansza, boolean pokazStatki) 
            {
                System.out.print("  ");
                for (int i = 1; i < rozmiar; i++) 
                {
                    System.out.print(i + " ");
                }
                System.out.println();
                for (int i = 1; i < rozmiar; i++) 
                {
                    System.out.print(i + " ");
                    for (int j = 1; j < rozmiar; j++) 
                    {
                        if (plansza[i][j] == 'S' && !pokazStatki) 
                        {
                            System.out.print("~ "); // Ukryj statki
                        } 
                        else 
                        {
                            System.out.print(plansza[i][j] + " ");
                        }
                    }
                    System.out.println();
                }
            }
        
            public void ustawStatkiGracza() 
            {
                Scanner scanner = new Scanner(System.in);
                int ustawioneStatki = 0;
        
                System.out.println("Ustaw swoje statki na planszy (wpisz współrzędne dla każdego statku):");
                while (ustawioneStatki < statki) 
                {
                    wyswietlPlansze(planszagracza, true);
                    System.out.print("Podaj współrzędne: ");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
        
                    if (x >= 0 && x < rozmiar && y >= 0 && y < rozmiar) 
                    {
                        if (planszagracza[x][y] == '~') 
                        {
                            planszagracza[x][y] = 'S'; // Ustawienie statku
                            statkigracza.put(x + "," + y, true);
                            ustawioneStatki++;
                        } 
                        else 
                        {
                            System.out.println("Tutaj już jest statek. Spróbuj ponownie.");
                        }
                    } 
                        else 
                        {
                        System.out.println("Nieprawidłowe współrzędne.");
                        }
                }
            }
        
           public void ustawStatkiKomputera() {
        Random rand = new Random();
        int ustawioneStatki = 0;

        while (ustawioneStatki < statki) 
        {
            int x = rand.nextInt(rozmiar);
            int y = rand.nextInt(rozmiar);
            if (planszakomputera[x][y] == '~') 
            {
                planszakomputera[x][y] = 'S'; // Ustawienie statku
                ustawioneStatki++;
            }
        }
    }

    public boolean strzelGracza(int x, int y) 
    {
        String wspolrzedne = x + "," + y;
        if (strzalygracza.contains(wspolrzedne)) 
        {
            System.out.println("Już strzelałeś w to miejsce.");
            return false;
        }
        strzalygracza.add(wspolrzedne);
        return strzel(planszakomputera, x, y);
    }

    public boolean strzelKomputera() 
    {
        Random rand = new Random();
        int x, y;
        String wspolrzedne;

        do 
        {
            x = rand.nextInt(rozmiar);
            y = rand.nextInt(rozmiar);
            wspolrzedne = x + "," + y;
        } while (strzalykomputera.contains(wspolrzedne)); // Komputer nie strzela w to samo miejsce

        strzalykomputera.add(wspolrzedne);
        return strzel(planszagracza, x, y);
    }

    private boolean strzel(char[][] plansza, int x, int y) 
    {
        if (plansza[x][y] == 'S') 
        {
            plansza[x][y] = 'X'; // Trafienie
            return true;
        } 
        else if (plansza[x][y] == '~') 
        {
            plansza[x][y] = 'O'; // Pudło
            return false;
        }
        return false;
    }

    public void rozgrywka() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Strzelaj celnie oraz szybko");

        int trafioneGracza = 0;
        int trafioneKomputera = 0;

        while (trafioneGracza < statki && trafioneKomputera < statki) 
        {
            wyswietlPlansze(planszakomputera, false);
            System.out.print("Podaj współrzędne: ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (x >= 0 && x < rozmiar && y >= 0 && y < rozmiar) 
            {
                if (strzelGracza(x, y)) {
                    System.out.println("Trafione!");
                    trafioneGracza++;
                } 
                else 
                {
                    System.out.println("Pudło!");
                }
            } 
                else 
                {
                Random random = new Random();
                int losowy = random.nextInt(6) +1;

                switch (losowy) 
                {
                    case 1: System.out.println("Gratulacje trafiłeś w szpital");
                         break;
                    case 2: System.out.println("Gratulacje trafiłeś w szkołe");
                         break;
                    case 3: System.out.println("Gratulacje trafiłeś w sierociniec");
                         break;
                    case 4: System.out.println("Gratulacje trafiłeś w KFC");
                         break;
                    case 5: System.out.println("Gratulacje trafiłeś w monopolowy");
                         break;
                    case 6: System.out.println("Gratulacje trafiłeś w mój samochód. TY GNOJU ");
                         break;
                    
                    default:
                        break;
                }

                    continue;
                }

            if (trafioneGracza < statki) 
            {
                if (strzelKomputera()) 
                {
                    System.out.println("Komputer trafił!");
                    trafioneKomputera++;
                } 
                else 
                {
                    System.out.println("Komputer spudłował!");
                }
            }
        }

        if (trafioneGracza == statki) 
        {
            System.out.println("Wygrałeś!!! Wszystkie statki komputera leżą na dnie");
        } 
        else 
        {
            System.out.println("Porażka... Komputer posłał cię na dno. Spróbuj ponownie");
        }
        System.out.println("Twoja plansza:");
        wyswietlPlansze(planszagracza, true); // Pokaż statki gracza
        System.out.println("Plansza komputera:");
        wyswietlPlansze(planszakomputera, true); // Pokaż statki komputera
    }
}
