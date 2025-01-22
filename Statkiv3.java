import java.util.Random;
import java.util.Scanner;

    


public class Statki {
    
    private char[][] planszagracza;
    private char[][] planszakomputera;
    private char[][] strzalygracza;
    private char[][] strzalykomputera;
    private static final int rozmiar = 7; // Rozmiar planszy
    private static final int statki = 4; // Liczba statków

    
    
    
    public Statki() 
    {

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
        strzalygracza = new char[rozmiar][rozmiar];
        strzalykomputera = new char[rozmiar][rozmiar];

            
        
        // Inicjalizacja plansz
            for (int i = 1; i < rozmiar; i++) 
        {
                for (int j = 1; j < rozmiar; j++) 
            {
                planszagracza[i][j] = '~'; // Woda
                planszakomputera[i][j] = '~'; 
                strzalygracza[i][j] = '~'; 
                strzalykomputera[i][j] = '~'; // Brak strzału
            }
        }
    }

    // Wyświetlanie planszy
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

    // Ustawianie statków gracza
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

    // Utawianie statków komputera
    private void rozmiescStatkiKomputera() 
    {
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

    //  Strzał gracza
    public boolean strzelGracza(int x, int y) 
    {
        if (strzalygracza[x][y] != '~') 
        {
            System.out.println("Już strzelałeś w to miejsce.");
            return false;  
        }
        return strzel(planszakomputera, x, y, strzalygracza);
    }

    // Strzał komputera
    public boolean strzelKomputera() 
    {
        Random rand = new Random();
        int x, y;

        do 
        {
            x = rand.nextInt(rozmiar);
            y = rand.nextInt(rozmiar);
        } while (strzalykomputera[x][y] != '~'); // Komputer nie strzela w to samo miejsce

        return strzel(planszagracza, x, y, strzalykomputera);
    }

    // Funkcja wykonująca strzał
    private boolean strzel(char[][] plansza, int x, int y, char[][] strzaly) 
    {
        if (plansza[x][y] == 'S') 
        {
            plansza[x][y] = 'X'; // Trafienie
            strzaly[x][y] = 'X';
            return true;
        } 
        else if (plansza[x][y] == '~') 
        {
            plansza[x][y] = 'O'; // Pudło
            strzaly[x][y] = 'O';
            return false;
        }
            return false; 
    }

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        Statki gra = new Statki(); 

        gra.ustawStatkiGracza(); // Gracz ustawia swoje statki
        gra.rozmiescStatkiKomputera(); // Komputer ustawia swoje statki

        System.out.println("Strzelaj celnie oraz szybko");

        int trafioneGracza = 0;
        int trafioneKomputera = 0;

        while (trafioneGracza < statki && trafioneKomputera < statki) 
        {
            // Ruch gracza
            gra.wyswietlPlansze(gra.planszakomputera, false);
            System.out.print("Podaj współrzędne: ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (x >= 1 && x < rozmiar && y >= 1 && y < rozmiar) 
            {
                if (gra.strzelGracza(x, y)) 
                {
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
                int randomnumber = random.nextInt(6) +1;

                switch (randomnumber) {
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

                continue; // jeśli gracz chybił powtarza on swoją turę
            }

            // Ruch komputera 
            if (trafioneGracza < statki) 
            {
                if (gra.strzelKomputera()) 
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
            System.out.println("Gratulacje! Zatopiłeś wszystkie statki komputera!");
        } 
        else 
        {
            System.out.println("Komputer zatopił wszystkie Twoje statki. Spróbuj ponownie!");
        }

        
        System.out.println("Twoja plansza:");
        gra.wyswietlPlansze(gra.planszagracza, true); // Pokaż statki gracza
        System.out.println("Plansza komputera:");
        gra.wyswietlPlansze(gra.planszakomputera, true); // Pokaż statki komputera
    }
}
