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

    public static JSONArray llibres;
    // Inicialización del JSONArray
    static {
        llibres = new JSONArray();
        try {
            // Leer el contenido del archivo JSON
            String filePath = "./data/llibres.json";
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            llibres = new JSONArray(content);
        } catch (IOException e) {
            System.out.println("No s'ha pogut llegir el fitxer de llibres. Inicialitzant amb un JSONArray buit.");
        }
    }

    public static String guardarLlibres(String filePath) {
        try {
            Files.write(Paths.get(filePath), llibres.toString(4).getBytes());
            return "Operació completada correctament.";
        } catch (IOException e) {
            return "Error escrivint el fitxer.";
        }
    }
    
    public static void dibuixarLlista(ArrayList<String> llista) {
        for (String linia : llista) {
            System.out.println(linia);
        }
    }

    public static int calcularNouId() {
        int maxId = 0;
        for (int i = 0; i < llibres.length(); i++) {
            maxId = Math.max(maxId, llibres.getJSONObject(i).getInt("Id"));
        }
        return maxId + 1;
    }

    public static String ficarLlibreNou(int idLlibre, String nomLlibre, String nomAutor) {
        String filePath = "./data/llibres.json";
        // Afegir el llibre dintre del fitxer json
        JSONObject nouLlibre = new JSONObject();
        int nouId = calcularNouId();
    
        nouLlibre.put("Id", nouId);
        nouLlibre.put("Titol", nomLlibre);
        nouLlibre.put("Autor", nomAutor);
    
        llibres.put(nouLlibre);
    
        String resultat = guardarLlibres(filePath);
        return resultat.startsWith("Error") ? resultat : "Llibre afegit correctament.";
    }
    
    public static String modificarLlibre(int idLlibre, String camp, Object nouValor) {
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("Id") == idLlibre) {
                if (llibre.has(camp)) {
                    llibre.put(camp, nouValor);
                    return guardarLlibres("./data/llibres.json");
                } else {
                    return "Error: El camp '" + camp + "' no existeix.";
                }
            }
        }
        return "Error: No s'ha trobat cap llibre amb l'ID especificat.";
    }

    public static String esborrarLlibre(int idLlibre) {
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("Id") == idLlibre) {
                llibres.remove(i);
                return guardarLlibres("./data/llibres.json");
            }
        }
        return "Error: No s'ha trobat cap llibre amb l'ID especificat.";
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

    public static void afegirLlibre(Scanner scanner) {
        System.out.println("=== Afegir Llibre ===");
        System.out.print("Títol del llibre: ");
        String nomLlibre = llegirNomLlibre(scanner);
        System.out.print("Autor: ");
        String nomAutor = llegirNomAutor(scanner);
        String resultat = ficarLlibreNou(0, nomLlibre, nomAutor);
        System.out.println(resultat);
    }
    
    public static void modificarLlibreMenu(Scanner scanner) {
        System.out.println("=== Modificar Llibre ===");
        System.out.print("ID del llibre a modificar: ");
        int idLlibre = scanner.nextInt();
        boolean idExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("Id") == idLlibre) {
                idExisteix = true;
                break;
            }
        }
        if (!idExisteix) {
            System.out.println("El llibre amb ID '" + idLlibre + "' no existeix.");
            return;
        }
        scanner.nextLine();

        System.out.print("Camp a modificar (Titol/Autor): ");
        String camp = scanner.nextLine();
        boolean campExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getString("Titol").equals(camp) || llibre.getString("Autor").equals(camp)) {
                campExisteix = true;
                break;
            }
        }
        if (!campExisteix) {
            System.out.println("El camp '" + camp + "' no existeix.");
            return;
        }
        
        Object nouValor = switch (camp) {
            case "Titol" -> llegirNomLlibre(scanner);
            case "Autor" -> llegirNomAutor(scanner);
            default -> null;
        };

        String resultat = modificarLlibre(idLlibre, camp, nouValor);
        System.out.println(resultat);
    }

    public static void esborrarLlibreMenu(Scanner scanner) {
        System.out.println("=== Esborrar Llibre ===");
        System.out.print("ID del llibre a esborrar: ");
        int idLlibre = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea
        String resultat = esborrarLlibre(idLlibre);
        System.out.println(resultat);
    }

    public static void llistarTotsLlibres() {
        String header = String.format("| %-10s | %-30s | %-50s |", "Id Llibre", "Títol", "Autor(s)");
        String separador = "-".repeat(header.length());
        System.out.println(separador);
        System.out.println(header);
        System.out.println(separador);

        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            String fila = String.format(
                "| %-10d | %-30s | %-50s |",
                llibre.getInt("Id"),
                llibre.getString("Titol"),
                llibre.getString("Autor")
            );
            System.out.println(fila);
        }
        System.out.println(separador);
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
                    afegirLlibre(scanner);
                    break;
                case "Modificar":
                    modificarLlibreMenu(scanner);
                    break;
                case "Eliminar":
                    esborrarLlibreMenu(scanner);
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
                    llistarTotsLlibres();
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