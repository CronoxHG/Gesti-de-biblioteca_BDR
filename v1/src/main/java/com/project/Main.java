package com.project;
import java.util.ArrayList;

import java.util.*;

public class Main {

    public static void dibuixarLlista(ArrayList<String> llista) {
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
        String[] lines = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static String obtenerOpcion(Scanner scanner) {
        ArrayList<String> menu = menuPrincipal();
    
        while (true) {
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
    
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
                // Ignorar la excepción y pedir otra entrada
            }
    
            System.out.println("Opció no vàlida. Torna a intentar-ho");
        }
    }

    public static void gestionaMenuPrincipal(Scanner scanner) {
        ArrayList<String> menuLlibres = menuPrincipal();
    
        while (true) {
            dibuixarLlista(menuLlibres);
    
            String opcio = obtenerOpcion(scanner);
            switch (opcio) {
                case "Sortir":
                    return;
                case "Llibres":
                    // Aquí se llama a la funcion para ir a Libros
                    break;
                case "Usuaris":
                    // Aquí se llama a la funcion para ir a Usuarios
                    break;
                case "Préstecs":
                    // Aquí se llama a la función para ir a prestamos
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho");
            }
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        gestionaMenuPrincipal(scanner);

        scanner.close();
    }
}