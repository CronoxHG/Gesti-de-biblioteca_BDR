package com.project;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void dibuixarLlista(ArrayList<String> llista) {
        for (String linia : llista) {
            System.out.println(linia);
        }
    }

    

    public static String ficarLlibreNou(int idLlibre, String nomLlibre, String nomAutor) {
        String idLlibre = jsonObject.
    }

    public static boolean validarNomLlibre(String nomLlibre) {
        if (nomLlibre == null || nomLlibre.trim().isEmpty()) {
            System.out.println("El nom del llibre no pot estar buit");
            return false;
        }

        if (nomLlibre.trim().length() < 3) {
            System.out.println("El nom del llibre ha de tenir almenys 3 caràcters");
            return false;
        }

        if (!nomLlibre.matches("[a-zA-ZÀ-ÿ0-9 .,'-]+")) {
            System.out.println("El nom del llibre conté caràcters no vàlids");
            return false;
        }

        return true;
    }

    public static boolean validarNomAutor (String nomAutor) {
        if (nomAutor == null || nomAutor.trim().isEmpty()) {
            System.out.println("El nom de l'autor no pot estar buit");
            return false;
        }

        if (nomAutor.trim().length() < 2) {
            System.out.println("El nom de l'autor ha de tenir almenys 2 caràcters");
            return false;
        }

        if (!nomAutor.matches("[a-zA-ZÀ-ÿ .'-]+")) {
            System.out.println("El nom de l'autor conté caràcters no vàlids");
            return false;
        }

        return true;
    }

    public static String llegirNomLlibre(Scanner scanner) {
        System.out.print("Introdueix el nom del llibre: ");
        String nomLlibre = scanner.nextLine();

        while (!validarNomLlibre(nomLlibre)) {
            System.out.println("Nom no vàlid. Només s'accepten lletres i espais");
            System.out.print("Introdueix el nom del llibre: ");
            nomLlibre = scanner.nextLine();
        }

        return nomLlibre;
    }

    public static String llegirNomAutor(Scanner scanner) {
        System.out.print("Introdueix el nom de l'autor del llibre: ");
        String nomAutor = scanner.nextLine();

        while (!validarNomAutor(nomAutor)) {
            System.out.println("Nom no vàlid. Només s'accepten lletres i espais");
            System.out.print("Introdueix el nom de l'autor del llibre: ");
            nomAutor = scanner.nextLine();
        }
        
        return nomAutor;
    }

    public static String afegirLlibre(Scanner scanner) {

        System.out.println("=== Afegir Llibre ===");

        String nomLlibre = llegirNomLlibre(scanner);
        String nomAutor = llegirNomAutor(scanner);


    }

    public static ArrayList<String> menuLlibres() {
        String menuText = """
                Gestió de llibres
                1. Afegir
                2. Modificar
                3. Eliminar
                4. Llistar
                0. Tornar al menú principal
                """;
        String[] lines = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static ArrayList<String> menuLlistarLlibres() {
        String menuText = """
                Llistar llibres
                1. Tots
                2. En préstec
                3. Per autor
                4. Cercar títol
                0. Tornar al menú de llibres
                """;
        String[] lines = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static String obtenerOpcion(Scanner scanner) {
        ArrayList<String> menu = menuLlibres();
    
        while (true) {
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
    
            try {
                int index = Integer.parseInt(opcio);
                if (index == 0) {
                    return "Tornar al menú principal";
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
    
    public static String obtenerOpcionLlistar(Scanner scanner) {
        ArrayList<String> menu = menuLlistarLlibres();

        while (true) {
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();

            try {
                int index = Integer.parseInt(opcio);
                if (index == 0) {
                    return "Tornar al menú de llibres";
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

    public static void gestionaMenuLlibres(Scanner scanner) {
        ArrayList<String> menuLlibres = menuLlibres();
    
        while (true) {
            dibuixarLlista(menuLlibres);
    
            String opcio = obtenerOpcion(scanner);
            switch (opcio) {
                case "Tornar al menú principal":
                    return;
                case "Afegir":
                    // Aquí se pone la función para añadir libros
                    break;
                case "Modificar":
                    // Aquí se pone la función para modificar libros
                    break;
                case "Eliminar":
                    // Aquí se pone la función para eliminar libros
                    break;
                case "Llistar":
                    gestionaMenuLlistarLlibres(scanner);
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho");
            }
        }
    }
    
    
    public static void gestionaMenuLlistarLlibres(Scanner scanner) {
        ArrayList<String> menuLlistarLlibres = menuLlistarLlibres();

        while (true) {
            dibuixarLlista(menuLlistarLlibres);

            String opcio = obtenerOpcionLlistar(scanner);
            switch (opcio) {
                case "Tornar al menú de llibres":
                    return;
                case "Tots":
                    // Aqui se pone la función para listar todos los libros
                    break;
                case "En préstec":
                    // Aqui se pone la función para listar los libros en prestamo
                    break;
                case "Per autor":
                    // Aqui se pone la función para listar los libros por autor
                    break;
                case "Cercar títol":
                    // Aqui se pone la función para listar los libros buscados por titulo
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho");
            }
        }
    }
    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        gestionaMenuLlibres(scanner);

        scanner.close();
    }
}