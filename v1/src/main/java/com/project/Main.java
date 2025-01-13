package com.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dibuixarMenu(ArrayList<String> llista) {
        for (String linia : llista) {
            System.out.println(linia);
        }
    }

    public static ArrayList<String> menuPrincipal() {
        String menuText = """
                Gestió de biblioteca
                1. Llibres
                2. Usuaris
                3. Préstecs
                0. Sortir
                """;
        String[] linies = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(linies));
    }

    public static String obtenirOpcio(Scanner scanner) {
        ArrayList<String> menu = menuPrincipal();

        while (true) {
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine().trim();

            try {
                int index = Integer.parseInt(opcio);
                if (index == 0) {
                    return "Sortir";
                }
                else if (index > 0 && index < menu.size() - 1) {
                    return menu.get(index).substring(3).trim();
                }
            }
            catch (NumberFormatException e) {

            }

            System.out.println("Opció no vàlida. Torna a intentar-ho.");
        }
    }

    public static void gestionaMenuPrincipal(Scanner scanner) {
        ArrayList<String> menu = menuPrincipal();

        while (true) {
            clearScreen();
            dibuixarMenu(menu);

            String opcio = obtenirOpcio(scanner);
            switch (opcio) {
                case "Sortir":
                    dibuixarMenu(new ArrayList<>(List.of("Fins Aviat!")));
                    return;
                case "Llibres":
                    // Aquí anem al menu de Llibres
                    break;
                case "Usuaris":
                    // Aquí anem al menu d'Usuaris
                    break;
                case "Préstecs":
                    // Aquí anem al menu de Préstecs
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
                    break;
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        gestionaMenuPrincipal(scanner);

        scanner.close();
    }
}