package com.project;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void esperarEnter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Prem Enter per continuar...");
        scanner.nextLine();
    }    

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

    public static JSONArray usuaris;
    // Inicialización del JSONArray
    static {
        usuaris = new JSONArray();
        try {
            // Leer el contenido del archivo JSON
            String filePath = "./data/usuaris.json";
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            usuaris = new JSONArray(content);
        } catch (IOException e) {
            System.out.println("No s'ha pogut llegir el fitxer de llibres. Inicialitzant amb un JSONArray buit.");
        }
    }

    public static JSONArray prestecs;
    // Inicialización del JSONArray
    static {
        prestecs = new JSONArray();
        try {
            // Leer el contenido del archivo JSON
            String filePath = "./data/prestecs.json";
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            prestecs = new JSONArray(content);
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
            maxId = Math.max(maxId, llibres.getJSONObject(i).getInt("id"));
        }
        return maxId + 1;
    }

    public static String ficarLlibreNou(int idLlibre, String nomLlibre, String nomAutor) {
        String filePath = "./data/llibres.json";
        // Crear el nou llibre
        JSONObject nouLlibre = new JSONObject();
        int nouId = calcularNouId();
    
        // Construir el JSONArray per al camp "Autor"
        JSONArray autorsArray = new JSONArray();
        autorsArray.put(nomAutor); // Afegir el nom de l'autor com un element del array
    
        // Configurar els valors del llibre nou
        nouLlibre.put("id", nouId);
        nouLlibre.put("titol", nomLlibre);
        nouLlibre.put("autor", autorsArray); // Afegir l'array d'autors
    
        // Afegir el llibre a la llista de llibres
        llibres.put(nouLlibre);
    
        // Guardar els llibres actualitzats al fitxer
        String resultat = guardarLlibres(filePath);
        return resultat.startsWith("Error") ? resultat : "Llibre afegit correctament.";
    }
    
    public static String modificarLlibre(int idLlibre, String camp, Object nouValor) {
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("id") == idLlibre) {
                if (llibre.has(camp)) {
                    llibre.put(camp, nouValor);
                    return guardarLlibres("./data/llibres.json");
                } else {
                    return "Error: El camp '" + camp + "' no existeix.";
                }
            }
        }
        return "Error: L'ID '" + idLlibre + "' no existeix.";
    }

    public static String esborrarLlibre(int idLlibre) {
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("id") == idLlibre) {
                llibres.remove(i);
                return guardarLlibres("./data/llibres.json");
            }
        }
        return "Error: L'ID '" + idLlibre + "' no existeix.";
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
        String nomLlibre = llegirNomLlibre(scanner);
        String nomAutor = llegirNomAutor(scanner);
        String resultat = ficarLlibreNou(0, nomLlibre, nomAutor);
        System.out.println(resultat);
    }
    
    public static void modificarLlibreMenu(Scanner scanner) {
        System.out.println("=== Modificar Llibre ===");
        llistarTotsLlibres(false);
        System.out.print("ID del llibre a modificar: ");
        int idLlibre = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer después de leer un entero
    
        // Buscar si el libro con el ID existe
        JSONObject llibreSeleccionat = null;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("id") == idLlibre) {
                llibreSeleccionat = llibre;
                break;
            }
        }
        // Si no se encuentra el libro, mostrar mensaje y salir
        if (llibreSeleccionat == null) {
            System.out.println("Error: L'ID '" + idLlibre + "' no existeix.");
            return;
        }
    
        System.out.print("Camp a modificar (Titol/Autor): ");
        String camp = scanner.nextLine().trim();
        // Verificar que el campo a modificar sea válido
        if (!camp.equals("Titol") && !camp.equals("Autor")) {
            System.out.println("El camp '" + camp + "' no és vàlid. Només pots modificar Titol o Autor.");
            return;
        }
    
        // Manejar la modificación del campo
        if (camp.equals("Titol")) {
            String nouTitol = llegirNomLlibre(scanner);
            llibreSeleccionat.put("titol", nouTitol);
        } else if (camp.equals("Autor")) {
            String autorsInput = llegirNomAutor(scanner);
            String[] autorsArray = autorsInput.split(",\\s*"); // Separar autores por comas
            JSONArray nousAutors = new JSONArray(List.of(autorsArray));
            llibreSeleccionat.put("autor", nousAutors);
        }
    
        // Guardar los cambios en el archivo
        String resultat = guardarLlibres("./data/llibres.json");
        System.out.println(resultat.startsWith("Error") ? resultat : "S'ha modificat correctament el llibre amb ID '" + idLlibre + "'.");
    }
    
    public static void esborrarLlibreMenu(Scanner scanner) {
        System.out.println("=== Esborrar Llibre ===");
        llistarTotsLlibres(false);
        System.out.print("ID del llibre a esborrar: ");
        int idLlibre = scanner.nextInt();
        boolean idExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("id") == idLlibre) {
                idExisteix = true;
                break;
            }
        }
        if (!idExisteix) {
            System.out.println("Error: L'ID '" + idLlibre + "' no existeix.");
            return;
        }
        scanner.nextLine();
        String resultat = esborrarLlibre(idLlibre);
        System.out.println(resultat);
        llistarTotsLlibres(false);
    }

    public static void llistarTotsLlibres(boolean pausar) {
        String header = String.format("| %-10s | %-30s | %-50s |", "Id Llibre", "Títol", "Autor(s)");
        String separador = "-".repeat(header.length());
        System.out.println(separador);
        System.out.println(header);
        System.out.println(separador);

        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            JSONArray autorsArray = llibre.getJSONArray("autor");
            StringBuilder autors = new StringBuilder();
            for (int j = 0; j < autorsArray.length(); j++) {
                autors.append(autorsArray.getString(j));
                if (j < autorsArray.length() - 1) {
                    autors.append(", "); // Separar múltiples autores con una coma
                }
            }

            // Crear fila para el libro
            String fila = String.format(
                "| %-10s | %-30s | %-50s |",
                llibre.getInt("id"),
                llibre.getString("titol"),
                autors.toString()
            );
            System.out.println(fila);
            System.out.println(separador);
        }
        if (pausar) {
            esperarEnter();
        }
    }

    public static void llistarLlibresEnPrestec() {
        JSONArray llibresLlistar = llibres;
        JSONArray usuarisLlistar = usuaris;
        JSONArray prestecsLlistar = prestecs;
    
        System.out.println("-".repeat(166));
        String llibresEnPrestecs = "Llibres en prèstecs";
        int padding = 162 - llibresEnPrestecs.length();
        int espaiEsquerra = padding / 2;
        int espaiDreta = padding - espaiEsquerra;
        System.out.println("| " + " ".repeat(espaiEsquerra) + llibresEnPrestecs + " ".repeat(espaiDreta) + " |");
        System.out.println("-".repeat(166));
        System.out.println(String.format("| %-8s | %-35s | %-35s | %35s | %12s | %12s |", "Id Prestec", "Nom i cognoms",
                "Títol del Llibre", "Autor/Autors", "Data de prèstec", "Data de devolució"));
        System.out.println("-".repeat(166));
    
        for (int i = 0; i < llibresLlistar.length(); i++) {
            JSONObject llibre = llibresLlistar.getJSONObject(i);
            for (int j = 0; j < prestecsLlistar.length(); j++) {
                JSONObject prestec = prestecsLlistar.getJSONObject(j);
                for (int k = 0; k < usuarisLlistar.length(); k++) {
                    JSONObject usuari = usuarisLlistar.getJSONObject(k);
                    if (llibre.getInt("id") == prestec.getInt("idLlibre")) {
                        if (usuari.getInt("id") == prestec.getInt("idUsuari")) {
                            System.out.println(String.format("| %-10s | %-35s | %-35s | %35s | %15s | %17s |",
                                    prestec.getInt("id"),
                                    usuari.getString("Nom") + " " + usuari.getString("Cognoms"),
                                    llibre.getString("titol"),
                                    llibre.getJSONArray("autor").join(", "),
                                    prestec.getString("dataPrestec"),
                                    prestec.getString("dataDevolucio")));
                            System.out.println("-".repeat(166));
                        }
                    }
                }
            }
        }
        esperarEnter();
    }
    
    public static void llistarLlibresPerAutor() {
        HashMap<String, List<JSONObject>> autorsMap = new HashMap<>();

        String header = String.format("| %-50s | %-30s | %-10s |", "Autor(s)", "Títol", "Id Llibre");
        String separador = "-".repeat(header.length());
        System.out.println(separador);
        System.out.println(header);
        System.out.println(separador);
        
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            JSONArray autorsArray = llibre.getJSONArray("autor");
            
            for (int j = 0; j < autorsArray.length(); j++) {
                String autor = autorsArray.getString(j);

                autorsMap.putIfAbsent(autor, new ArrayList<>());
                autorsMap.get(autor).add(llibre);
            }

            String fila = String.format(
                "| %-50s | %-30s | %-10s |",
                autorsArray.toString(),
                llibre.getString("titol"),
                llibre.getInt("id")
            );
            System.out.println(fila);
            System.out.println(separador);
        }
        esperarEnter();
    }

    public static void llistarLlibresPerBusqueda() {
        Scanner scanner = new Scanner(System.in);
        String header = String.format("| %-10s | %-30s | %-50s |", "Id Llibre", "Títol", "Autor(s)");
        String separador = "-".repeat(header.length());

        System.out.print("Introdueix un títol d'un llibre: ");
        String titolLlibre = scanner.nextLine();

        System.out.println(separador);
        System.out.println(header);
        System.out.println(separador);
        boolean titolExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            JSONArray autorsArray = llibre.getJSONArray("autor");
            StringBuilder autors = new StringBuilder();
            for (int j = 0; j < autorsArray.length(); j++) {
                autors.append(autorsArray.getString(j));
                if (j < autorsArray.length() - 1) {
                    autors.append(", "); // Separar múltiples autores con una coma
                }
            }
            if (llibre.getString("titol").contains(titolLlibre)) {
                titolExisteix = true;
                String fila = String.format(
                "| %-10s | %-30s | %-50s |",
                llibre.getInt("id"),
                llibre.getString("titol"),
                autors.toString()
                );
                System.out.println(fila);
                System.out.println(separador);
            }
        }
        if (!titolExisteix) {
            System.out.println("El títol del llibre '" + titolLlibre + "' no existeix");
            scanner.nextLine();
            return;
        }
        esperarEnter();
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
            clearScreen();
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
            clearScreen();
            dibuixarLlista(menuLlistarLlibres);

            String opcio = obtenerOpcionLlistar(scanner);
            switch (opcio) {
                case "Tornar al menú de llibres":
                    return;
                case "Tots":
                    llistarTotsLlibres(true);
                    break;
                case "En préstec":
                    llistarLlibresEnPrestec();
                    break;
                case "Per autor":
                    llistarLlibresPerAutor();
                    break;
                case "Cercar títol":
                    llistarLlibresPerBusqueda();
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