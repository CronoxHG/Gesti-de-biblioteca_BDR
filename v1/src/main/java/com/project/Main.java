package com.project;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static final JSONArray llibres;
    // Inicialización del JSONArray
    static {
        JSONArray tempLlibres = new JSONArray();
        try {
            // Leer el contenido del archivo JSON
            String filePath = "./data/llibres.json";
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            tempLlibres = new JSONArray(content);
        } catch (IOException e) {
            System.out.println("No s'ha pogut llegir el fitxer de llibres. Inicialitzant amb un JSONArray buit.");
        }
        llibres = tempLlibres; // Asignar el JSONArray leído o vacío
    }
    
    public static void dibuixarLlista(ArrayList<String> llista) {
        for (String linia : llista) {
            System.out.println(linia);
        }
    }

    public static String ficarLlibreNou(int idLlibre, String nomLlibre, String nomAutor) {
        String filePath = "./data/llibres.json";
        // Afegir el llibre dintre del fitxer json
        JSONObject nouLlibre = new JSONObject();
    
        nouLlibre.put("Id", idLlibre + 1); // Utilitzar idLlibre directament
        nouLlibre.put("Titol", nomLlibre); // Corregit el nom del camp
        nouLlibre.put("Autor", nomAutor);
    
        llibres.put(nouLlibre);
    
        String textoJson = llibres.toString(4);
        try {
            Files.write(Paths.get(filePath), textoJson.getBytes());
            System.out.println("Llibre afegit correctament.");
            return "Llibre afegit correctament.";
        } catch (IOException e) {
            System.out.println("Ha sorgit un error inesperat en escriure el fitxer.");
            return "Error escrivint el fitxer.";
        }
    }
    
    public static String modificarLlibre(String idLlibre, String camp, Object nouValor) {
        // Verificar si el ID del libro existe
        boolean idExisteix = false;
        JSONObject llibreSeleccionat = null;
    
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getString("Id").equals(idLlibre)) { // Comparación correcta con equals()
                idExisteix = true;
                llibreSeleccionat = llibre; // Guardar referencia al libro
                break;
            }
        }
    
        // Si el ID no existe, devolver un mensaje de error
        if (!idExisteix) {
            return "Error: No s'ha trobat cap llibre amb l'ID especificat.";
        }
        // Verificar si el campo a modificar existe en el libro
        if (!llibreSeleccionat.has(camp)) { // Usar el método has() para verificar la existencia del campo
            return "Error: El camp '" + camp + "' no existeix en el llibre.";
        }
    
        // Modificar el campo del libro
        llibreSeleccionat.put(camp, nouValor);
        // Retornar confirmación
        return "OK";
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
        // Llegir el nom del llibre
        String nomLlibre = llegirNomLlibre(scanner);
        // Llegir el nom de l'autor
        String nomAutor = llegirNomAutor(scanner);
        try {
            // Afegir el llibre utilitzant ficarLlibreNou
            String resultat = ficarLlibreNou(0, nomLlibre, nomAutor);
            if (resultat != null && resultat.contains("Error")) {
                return resultat; // Retornar el missatge d'error de ficarLlibreNou
            }
            return "S'ha afegit correctament el llibre \"" + nomLlibre + "\".";
        } catch (Exception e) {
            return "Ha sorgit un error inesperat al afegir el llibre: " + e.getMessage();
        }
    }
    
    public static ArrayList<String> modificarLlibreMenu (Scanner scanner) {
        ArrayList<String> linies = new ArrayList<>();
        linies.add("=== Modificar Client ===");

        System.out.print("Introdueix l'ID del llibre a modificar: ");
        String idLlibre = scanner.nextLine().trim();

        boolean idExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getString("Id") == idLlibre) {
                idExisteix = true;
                break;
            }
        }
        if (!idExisteix) {
            linies.add("El llibre amb clau " + idLlibre + " no existeix.");
            return linies;
        }

        System.out.println("Camps disponibles per modificar: Titol i Autor");
        System.out.print("Introdueix el camp que vols modificar: ");
        String camp = scanner.nextLine().trim();

        boolean campExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getString("Titol") == camp || llibre.getString("Autor") == camp) {
                campExisteix = true;
                break;
            }
        }

        if (!campExisteix) {
            linies.add("El camp " + camp + " no és vàlid");
        }

        Object nouValor = switch (camp) {
            case "Titol" -> llegirNomLlibre(scanner);
            case "Autor" -> llegirNomAutor(scanner);
            default -> null;
        };

        if (nouValor == null) {
            return linies;
        }

        String resultat = modificarLlibre(idLlibre, camp, nouValor);

        if (!resultat.equals("OK")) {
            linies.add(resultat);
        } else {
            System.out.println("S'ha modificat el client " + idLlibre + ".");
        }

        return linies;
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