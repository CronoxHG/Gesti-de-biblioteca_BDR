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
import java.time.LocalDate;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static LocalDate dataDeAvui = LocalDate.now();
    public static String filePathPrestecs = "./data/prestecs.json";
    public static String filePathLlibres = "./data/llibres.json";
    public static String filePathUsuaris = "./data/usuaris.json";

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

    public static JSONArray llibres() throws IOException {
        // importar llibres.json
        String contentLlibres = new String(Files.readAllBytes(Paths.get(filePathLlibres)));
        JSONArray llibres = new JSONArray(contentLlibres);
        return llibres;
    }

    public static JSONArray usuaris() throws IOException {
        // importar usuaris.json
        String contentUsuaris = new String(Files.readAllBytes(Paths.get(filePathUsuaris)));
        JSONArray usuaris = new JSONArray(contentUsuaris);
        return usuaris;
    }

    public static JSONArray prestecs() throws IOException {
        // importar prestecs.json
        String contentPrestecs = new String(Files.readAllBytes(Paths.get(filePathPrestecs)));
        JSONArray prestecs = new JSONArray(contentPrestecs);
        return prestecs;
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

    public static boolean digit(String num) {
        for (char numSeparat : num.toCharArray()) {
            if (!Character.isDigit(numSeparat)) {
                return false;
            }
        }
        if (num == "" || num == null){
            return false;
        }
        return true;
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

    public static boolean dataValida(String data) { // mirar si realmente se necesita porque debería de cogerlo
        // directamente, a lo mejor se necesita para
        String[] dataStringArray = data.split("-");
        // mirar de que tots els elements de l'array siguin numeros sencers.
        if (dataStringArray.length != 3) { // comprobar que la longitut del array que conté la data es correcte.
        return false;
        }
        for (String num : dataStringArray) {
        if (!digit(num)) {
        return false;
        }
        }
        boolean esBisiesto = true;
        Integer any = Integer.parseInt(dataStringArray[0]);
        Integer mes = Integer.parseInt(dataStringArray[1]);
        Integer dia = Integer.parseInt(dataStringArray[2]);
        if (any % 4 == 0) {
        if (any % 100 == 0) {
        if (any % 400 == 0) {
        esBisiesto = true;
        } else {// no es bisiesto
        esBisiesto = false;
        }
        } else {
        esBisiesto = true;
        }

        } else { // no es bisiesto
        esBisiesto = false;
        }

        if (("0" + mes).equals("02") && dia > 29) {
        return false;
        }

        if (!(esBisiesto) && ("0" + mes).equals("02") && dia > 28) {
        return false;
        }
        if (mes < 1 || mes > 12) {
        return false;
        }
        if (dia < 1) {
        return false;
        }

        switch (mes) {
        case 1: {
        if (dia > 31) {
        return false;
        }
        }
        case 3: {
        if (dia > 31) {
        return false;
        }
        }
        case 4: {
        if (dia > 30) {
        return false;
        }
        }
        case 5: {
        if (dia > 31) {
        return false;
        }
        }
        case 6: {
        if (dia > 30) {
        return false;
        }
        }
        case 7: {
        if (dia > 31) {
        return false;
        }
        }
        case 8: {
        if (dia > 31) {
        return false;
        }
        }
        case 9: {
        if (dia > 30) {
        return false;
        }
        }
        case 10: {
        if (dia > 31) {
        return false;
        }
        }
        case 11: {
        if (dia > 30) {
        return false;
        }
        }
        case 12: {
        if (dia > 31) {
        return false;
        }
        }
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

    public static void afegirPrestec() {
        String filePath = "./data/prestecs.json";
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray prestecs = new JSONArray(content);
        // no puc preguntar l'id del prèstec hauria de ser autoincrement.
        System.out.print("Introdueix l'id del llibre que vols: ");// podriem fer-lo també amb títol.
        String idLlibre = scanner.nextLine();

        // verificar que el idLlibre es un número.
        try {
            Integer.parseInt(idLlibre);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + idLlibre + " no és un id vàlid.");
            return;
        }

        Integer idLlibreInteger = Integer.parseInt(idLlibre);
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            // si el libro está prestado no puedo prestarlo.
            if (prestec.getInt("idLlibre") == idLlibreInteger) {
                try {
                    throw new RuntimeException();
                } catch (RuntimeException e) {
                    System.out.println("El llibre amb id " + idLlibreInteger + " ja està prestat.");
                    return;
                }

                // també es podría fer així:
                // System.out.println("El llibre amb id "+idLlibreInteger+" ja està prestat.");
                // return;
            }
        }

        String filePathLlibres = "./data/llibres.json";
        String contentLlibres = null;
        try {
            contentLlibres = new String(Files.readAllBytes(Paths.get(filePathLlibres)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray llibres = new JSONArray(contentLlibres);
        // agafat la informació de l'arxiu usuaris.json
        String filePathUsuari = "./data/usuaris.json";
        String contentUsuari = null;
        try {
            contentUsuari = new String(Files.readAllBytes(Paths.get(filePathUsuari)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        boolean llibreExisteix = false;
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            if (llibre.getInt("id") == idLlibreInteger) {
                llibreExisteix = true;
            }
        }
        if (!llibreExisteix) {
            System.out.println("El llibre no existeix.");
            return;
        }

        System.out.print("Introdueix l'id del usuari: ");// podriem fer-lo també amb títol.
        String idUsuari = scanner.nextLine();
        try {
            Integer.parseInt(idUsuari);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + idUsuari + "no és un id d'usuari vàlid.");
            return;
        }
        Integer idUsuariInteger = Integer.parseInt(idUsuari);

        // si l'usuari té 4 llibres prestats, no pot més.
        Integer cnt = 0;
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            if (prestec.getInt("idUsuari") == idUsuariInteger) {
                cnt += 1;
            }
        }
        if (cnt >= 4) {
            try {
                throw new RuntimeException();
            } catch (RuntimeException e) {
                System.out.println("Aquest usuari no pot tenir més llibres en préstec");
                return;
            }
        }
        JSONArray usuaris = new JSONArray(contentUsuari);
        boolean usuariExisteix = false;
        for (int j = 0; j < usuaris.length(); j++) {
            JSONObject usuari = usuaris.getJSONObject(j);
            if (usuari.getInt("id") == idUsuariInteger) {
                usuariExisteix = true;
            }
        }
        if (!usuariExisteix) {
            System.out.println("El usuari no existeix.");
            return;
        }

        Integer numMesGran = 0;
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            if (prestec.getInt("id") > numMesGran) {
                numMesGran = prestec.getInt("id");
            }
        }

        // afegir el prestec dintre del fitxer json.
        JSONObject nouPrestec = new JSONObject();

        nouPrestec.put("id", numMesGran + 1);
        nouPrestec.put("idLlibre", idLlibreInteger);
        nouPrestec.put("idUsuari", idUsuariInteger);
        nouPrestec.put("dataPrestec", dataDeAvui);
        nouPrestec.put("dataDevolucio", dataDeAvui.plusDays(7));
        prestecs.put(nouPrestec);
        String textoJson = prestecs.toString(4);
        try {
            Files.write(Paths.get(filePath), textoJson.getBytes());
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
    }
    
    public static void esborrarPrestecs() {
        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        // mostrar la llista de llibres en prestec.
        llistarPrestecs();

        System.out.print("Inserta l'id del préstec que vols esborrar: ");
        String idEsborrar = scanner.nextLine();
        System.out.println();

        if (!digit(idEsborrar)) {
            System.out.println("L'id ha de ser un número.");
            return;
        }
        boolean idExistent = false;
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            if (prestec.getInt("id") == Integer.parseInt(idEsborrar)) {
                idExistent = true;
                prestecs.remove(i);
                String textoJson = prestecs.toString(4);
                try {
                    Files.write(Paths.get(filePathPrestecs), textoJson.getBytes());
                } catch (IOException e) {
                    System.out.println("Ha surgit un error inesperat en el fitxer.");
                    return;
                }
                llistarPrestecs();
                System.out.println();
                System.out.println("S'ha esborrar el préstec.");
                return;
            }

        }
        if (!idExistent) {
            System.out.println("L'id " + idEsborrar + " no existeix.");
            return;
        }
    }
    
    public static void modificarLlibrePrestecs() {
        JSONArray llibres = null;
        try {
            llibres = llibres();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        llistatDePrestec();
        System.out.print("Inserta l'id del préstec, per modificar: ");
        String idPrestec = scanner.nextLine();

        if (!digit(idPrestec)) {
            System.out.println("L'id del préstec ha de ser un número");
            return;
        }

        boolean existeixPrestec = false;
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            if (Integer.parseInt(idPrestec) == prestec.getInt("id")) {
                existeixPrestec = true;
                System.out.print("Camp a modificar (Llibre/Data devolucio): ");
                String opc = scanner.nextLine();
                switch (opc.toLowerCase().trim().replace('ó', 'o')) {
                    case "llibre":
                        String idLlibre = null;
                        System.out.print("Inserta l'id del llibre: ");
                        idLlibre = scanner.nextLine();
                        if (!digit(idLlibre)) {
                            System.out.println("L'id del llibre ha de ser un número");
                            return;
                        }

                        boolean idExisteix = false;
                        for (int j = 0; j < llibres.length(); j++) {
                            JSONObject llibre = llibres.getJSONObject(j);
                            if (llibre.getInt("id") == Integer.parseInt(idLlibre)) {
                                idExisteix = true;
                            }
                        }
                        for (int k = 0; k < prestecs.length(); k++) {
                            if (prestecs.getJSONObject(k).getInt("idLlibre") == Integer.parseInt(idLlibre)) {
                                System.out.println("El llibre ja està en préstec.");
                                return;
                            }

                        }
                        if (!idExisteix) {
                            System.out.println("No existeix el llibre amb id " + idLlibre);
                            return;
                        }
                        prestec.put("idLlibre", Integer.parseInt(idLlibre));
                        String textoJson = prestecs.toString(4);
                        try {
                            Files.write(Paths.get(filePathPrestecs), textoJson.getBytes());
                        } catch (IOException e) {
                            System.out.println("Ha surgit un error inesperat en el fitxer.");
                            return;
                        }
                        break;
                    case "data devolucio":
                        System.out.print("Inserta una data vàlida (aaaa-mm-dd): ");
                        String dataPrestec = scanner.nextLine();
                        if (!dataValida(dataPrestec)) {
                            System.out.println("La data " + dataPrestec + " no és vàlida.");
                            return;
                        }
                        LocalDate dataDevolucioLocal = LocalDate.parse(dataPrestec);
                        LocalDate dataGuardada = LocalDate.parse(prestec.getString("dataPrestec"));
                        

                        if (dataDevolucioLocal.isBefore(dataGuardada)){
                            System.out.println("Has d'inserir una data més gran a la guardada (data préstec)");
                            return;
                        }
                        prestec.put("dataDevolucio", dataPrestec);
                        textoJson = prestecs.toString(4);
                        try {
                            Files.write(Paths.get(filePathPrestecs), textoJson.getBytes());
                        } catch (IOException e) {
                            System.out.println("Ha surgit un error inesperat en el fitxer.");
                            return;
                        }

                        break;
                    default:
                        System.out.println("El camp que has introduït no es correcte.");
                        return;
                }
                llistatDePrestec();
                System.out.println();
                System.out.println("S'ha canviat correctament");

            }
        }
        if (!existeixPrestec){
            System.out.println("El préstec amb l'id "+idPrestec + " no existeix.");
            return;
        }
    }
    
    public static void llistatDePrestec() { // llista normal
        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray llibres = null;
        try {
            llibres = llibres();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray usuaris = null;
        try {
            usuaris = usuaris();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        System.out.println("-".repeat(118));
        String llibresEnPrestecs = "Llista de préstecs";
        Integer padding = 114 - llibresEnPrestecs.length();
        Integer espaiEsquerra = padding / 2;
        Integer espaiDreta = padding - espaiEsquerra;
        System.out.println("| " + " ".repeat(espaiEsquerra) + llibresEnPrestecs + " ".repeat(espaiDreta) + " |");
        System.out.println("-".repeat(118));
        
        System.out.println(String.format("| %-8s | %-35s | %25s | %12s | %12s |", "Id Prestec", "Títol del Llibre",
                "Nom i cognoms", "Data de prèstec", "Data de devolució"));
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            for (int j = 0; j < llibres.length(); j++) {
                JSONObject llibre = llibres.getJSONObject(j);
                for (int k = 0; k < usuaris.length(); k++) {
                    JSONObject usuari = usuaris.getJSONObject(k);
                    if (prestec.getInt("idLlibre") == llibre.getInt("id")) {
                        if (prestec.getInt("idUsuari") == usuari.getInt("id")) {
                            System.out.println("-".repeat(118));
                            System.out.println(String.format("| %-10s | %-35s | %25s | %15s | %17s |",
                                    prestec.getInt("id"), llibre.getString("titol"),
                                    usuari.getString("nom") + " " + usuari.getString("cognoms"),
                                    prestec.getString("dataPrestec"), prestec.getString("dataDevolucio")));
                        }
                    }
                }
            }
        }
        System.out.println("-".repeat(118));
    }
    
    public static void llistarPrestecsUsuari() {
        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray llibres = null;
        try {
            llibres = llibres();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray usuaris = null;
        try {
            usuaris = usuaris();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        System.out.print("Inserta l'id o el nom de l'usuari: ");
        String identificadorUsuari = scanner.nextLine();

        Integer idUsuari = null;
        boolean usuariExistent = false;
        for (int i = 0; i < usuaris.length(); i++) {
            JSONObject usuari = usuaris.getJSONObject(i);
            if (identificadorUsuari.equals(Integer.toString(usuari.getInt("id")))
                    || identificadorUsuari.equals(usuari.getString("nom"))) {
                idUsuari = usuari.getInt("id");
                usuariExistent = true;
            }
        }
        if (!usuariExistent) {
            System.out.println("L'usuari amb id o nom " + identificadorUsuari + " no existeix.");
            return;
        }

        JSONObject usuari = null;
        for (int k = 0; k < usuaris.length(); k++) {
            JSONObject usuariBucle = usuaris.getJSONObject(k);
            if (usuariBucle.getInt("id") == idUsuari) {
                usuari = usuaris.getJSONObject(k);
            }
        }
        System.out.println("-".repeat(93));
        String nomCognoms = usuari.getString("nom") + " " + usuari.getString("cognoms");
        Integer padding = 90 - nomCognoms.length();
        Integer espaInteger = padding / 2;
        System.out.println(
                String.format("| " + " ".repeat(espaInteger) + "%s" + " ".repeat(espaInteger) + " |", nomCognoms));

        // si existeix pero no te cap prestec.
        boolean noTePrestesc = true;
        System.out.println("-".repeat(93));
        for (int j = 0; j < prestecs.length(); j++) {
            JSONObject prestec = prestecs.getJSONObject(j);
            for (int l = 0; l < llibres.length(); l++) {
                JSONObject llibre = llibres.getJSONObject(l);
                if (llibre.getInt("id") == prestec.getInt("idLlibre")) {
                    if (prestec.getInt("idUsuari") == idUsuari) {
                        System.out.println(String.format("| %-10s | %-30s | %25s | %15s |", prestec.getInt("id"),
                                llibre.getString("titol"), prestec.getString("dataPrestec"),
                                prestec.getString("dataDevolucio")));
                        System.out.println("-".repeat(93));
                        noTePrestesc = false;
                    }
                }
            }
        }
        if (noTePrestesc) {
            System.out.println();
            System.out.println("Aquest usuari no té préstecs.");
            System.out.println();
            return;
        }
    }
    
    public static void llistarPrestecs() {
        JSONArray llibres = null;
        try {
            llibres = llibres();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray usuaris = null;
        try {
            usuaris = usuaris();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        System.out.println("-".repeat(166));
        String llibresEnPrestecs = "Tots els préstecs";
        Integer padding = 162 - llibresEnPrestecs.length();
        Integer espaiEsquerra = padding / 2;
        Integer espaiDreta = padding - espaiEsquerra;
        System.out.println("| " + " ".repeat(espaiEsquerra) + llibresEnPrestecs + " ".repeat(espaiDreta) + " |");
        System.out.println("-".repeat(166));
        System.out.println(String.format("| %-8s | %-35s | %-35s | %35s | %12s | %12s |", "Id Prestec", "Nom i cognoms",
                "Títol del Llibre",
                "Autor/Autors", "Data de prèstec", "Data de devolució"));
        System.out.println("-".repeat(166));
        for (int i = 0; i < llibres.length(); i++) {
            JSONObject llibre = llibres.getJSONObject(i);
            for (int j = 0; j < prestecs.length(); j++) {
                JSONObject prestec = prestecs.getJSONObject(j);
                for (int k = 0; k < usuaris.length(); k++) {
                    JSONObject usuari = usuaris.getJSONObject(k);
                    if (llibre.getInt("id") == prestec.getInt("idLlibre")) {
                        if (usuari.getInt("id") == prestec.getInt("idUsuari")) {
                            String autoresJoin = llibre.getJSONArray("autor").join(", ");
                            System.out.println(String.format("| %-10s | %-35s | %-35s | %35s | %15s | %17s |",
                                    prestec.getInt("id"),
                                    usuari.getString("nom") + " " + usuari.getString("cognoms"),
                                    llibre.getString("titol"),
                                    autoresJoin.length() > 35 ? autoresJoin.substring(0,35) : autoresJoin,
                                    prestec.getString("dataPrestec"),
                                    prestec.getString("dataDevolucio")));
                            System.out.println("-".repeat(166));
                        }
                    }
                }
            }
        }

    }
    
    public static void llistaPrestecsForaDeTermini() {
        JSONArray llibres = null;
        try {
            llibres = llibres();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray usuaris = null;
        try {
            usuaris = usuaris();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        if (prestecs.isEmpty()){
            System.out.println("No hi han préstecs per llistar");
            return;
        }

        System.out.println("-".repeat(151));
        String llibresEnPrestecsForaTermini = "Llistat de préstecs fora de termini";
        Integer padding = 147 - llibresEnPrestecsForaTermini.length();
        Integer espaiEsquerra = padding / 2;
        Integer espaiDreta = padding - espaiEsquerra;
        System.out.println("| " + " ".repeat(espaiEsquerra) + llibresEnPrestecsForaTermini + " ".repeat(espaiDreta) + " |");
        System.out.println("-".repeat(151));
        System.out.println(String.format("| %15s | %35s | %-35s | %-25s | %-25s |","Id Préstec","Titol del llibre","Nom i cognoms de l'usuari","Data del préctec","Data de la devolució"));
        System.out.println("-".repeat(151));
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            for (int j = 0; j < llibres.length(); j++) {
                JSONObject llibre = llibres.getJSONObject(j);
                for (int k = 0; k < usuaris.length(); k++) {
                    JSONObject usuari = usuaris.getJSONObject(k);
                    LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));
                    if (dataDevolucio.isBefore(dataDeAvui)) {
                        if (llibre.getInt("id") == prestec.getInt("idLlibre")) {
                            if (usuari.getInt("id") == prestec.getInt("idUsuari")) {
                                System.out.println(String.format("| %15s | %35s | %-35s | %-25s | %-25s |",
                                        prestec.getInt("id"), llibre.getString("titol"),
                                        usuari.getString("nom") + " " + usuari.getString("cognoms"),
                                        prestec.getString("dataPrestec"), prestec.getString("dataDevolucio")));
                                        System.out.println("-".repeat(151));
                            }
                        }
                    }
                }

            }
        }
    }
    
    public static void llistaPrestecsActius() {
        JSONArray llibres = null;
        try {
            llibres = llibres();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray usuaris = null;
        try {
            usuaris = usuaris();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }

        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        System.out.println("-".repeat(151));
        String llibresEnPrestecsActius = "Llistat de préstecs actius";
        Integer padding = 147 - llibresEnPrestecsActius.length();
        Integer espaiEsquerra = padding / 2;
        Integer espaiDreta = padding - espaiEsquerra;
        System.out.println("| " + " ".repeat(espaiEsquerra) + llibresEnPrestecsActius + " ".repeat(espaiDreta) + " |");
        System.out.println("-".repeat(151));
        System.out.println(String.format("| %15s | %35s | %-35s | %-25s | %-25s |","Id Préstec","Titol del llibre","Nom i cognoms de l'usuari","Data del préctec","Data de la devolució"));
        System.out.println("-".repeat(151));
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            for (int j = 0; j < llibres.length(); j++) {
                JSONObject llibre = llibres.getJSONObject(j);
                for (int k = 0; k < usuaris.length(); k++) {
                    JSONObject usuari = usuaris.getJSONObject(k);
                    LocalDate dataDevolucio = LocalDate.parse(prestec.getString("dataDevolucio"));
                    if (dataDevolucio.isAfter(dataDeAvui)) {
                        if (llibre.getInt("id") == prestec.getInt("idLlibre")) {
                            if (usuari.getInt("id") == prestec.getInt("idUsuari")) {
                                System.out.println(String.format("| %15s | %35s | %-35s | %-25s | %-25s |",
                                        prestec.getInt("id"), llibre.getString("titol"),
                                        usuari.getString("nom") + " " + usuari.getString("cognoms"),
                                        prestec.getString("dataPrestec"), prestec.getString("dataDevolucio")));
                                        System.out.println("-".repeat(151));
                            }
                        }
                    }
                }

            }
        }
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
        String input = scanner.nextLine().trim();

        // Intentar convertir el ID a un número entero
        int idLlibre;
        try {
            idLlibre = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: L'ID ha de ser un número enter.");
            return; // Salir y volver al menú
        }

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
        if (!camp.replace("í", "i").equalsIgnoreCase("Titol") && !camp.equalsIgnoreCase("Autor")) {
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
        System.out.print("ID del llibre a eliminar: ");
        String input = scanner.nextLine().trim();

        // Intentar convertir el ID a un número entero
        int idLlibre;
        try {
            idLlibre = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: L'ID ha de ser un número enter.");
            return; // Salir y volver al menú
        }
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
        llistarTotsLlibres(true);
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
                                    usuari.getString("nom") + " " + usuari.getString("cognoms"),
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

    public static ArrayList<String> menuPrestecs() {
        String menuText = """
                Gestió de devolucions
                1. Afegir
                2. Modificar
                3. Eliminar
                4. Llistar
                0. Tornar al menú principal
                """;
        String[] lines = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static ArrayList<String> menuLlistarPrestecs() {
        String menuText = """
                Llistar devolucions
                1. Tots els préstecs
                2. Préstecs fora de termini
                3. Préstecs actius
                4. Préstecs per usuari
                5. Llibres en préstec
                0. Tornar al menú de llibres
                """;
        String[] lines = menuText.split("\\R");
        return new ArrayList<>(Arrays.asList(lines));
    }

    public static void gestionaMenuLlibres(Scanner scanner) {
        ArrayList<String> menuLlibres = menuLlibres();
    
        while (true) {
            clearScreen();
            dibuixarLlista(menuLlibres);
    
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
            switch (opcio.toLowerCase().replace("ú", "u")) {
                case "0":
                case "tornar al menú principal":
                    return;
                case "1":
                case "afegir":
                    afegirLlibre(scanner);
                    break;
                case "2":
                case "modificar":
                    modificarLlibreMenu(scanner);
                    break;
                case "3":
                case "eliminar":
                    esborrarLlibreMenu(scanner);
                    break;
                case "4":
                case "llistar":
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

            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
            switch (opcio.toLowerCase().replace("é", "e").replace("í", "i")) {
                case "0":
                case "tornar al menú de llibres":
                    return;
                case "1":
                case "tots":
                    llistarTotsLlibres(true);
                    break;
                case "2":
                case "en préstec":
                    llistarLlibresEnPrestec();
                    break;
                case "3":
                case "per autor":
                    llistarLlibresPerAutor();
                    break;
                case "4":
                case "cercar títol":
                    llistarLlibresPerBusqueda();
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho");
            }
            esperarEnter();
        }
    }

    public static void gestionaMenuPrestecs(Scanner scanner) {
        ArrayList<String> menuPrestecs = menuPrestecs();
    
        while (true) {
            clearScreen();
            dibuixarLlista(menuPrestecs);
    
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
            clearScreen();

            switch (opcio.toLowerCase().replace("ú", "u")) {
                case "0":
                case "tornar al menu principal":
                    return;
                case "1":
                case "afegir":
                    afegirPrestec();
                    esperarEnter();
                    break;
                case "2":
                case "modificar":
                    modificarLlibrePrestecs();
                    esperarEnter();
                    break;
                case "3":
                case "eliminar":
                    esborrarPrestecs();
                    esperarEnter();
                    break;
                case "4":
                case "llistar":
                    gestionaMenuPrestecsLlistar(scanner);
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho");
                    esperarEnter();
            }
        }
    }
    
    public static void gestionaMenuPrestecsLlistar(Scanner scanner) {

        ArrayList<String> menuLlistarPrestecs = menuLlistarPrestecs();

        while (true) {
            clearScreen();
            dibuixarLlista(menuLlistarPrestecs);

            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
            clearScreen();
            switch (opcio.toLowerCase().replace("ú", "u").replace("é", "e").replace("è", "e")) {
                case "0":
                case "tornar al menu de prestecs":
                    return;
                case "tots els prestecs":
                case "1":
                    llistarPrestecs();
                    break;
                case "2":
                case "prestecs fora de termini":
                    llistaPrestecsForaDeTermini();
                    break;
                case "3":
                case "prestecs actius":
                    llistaPrestecsActius();
                    break;
                case "4":
                case "prestecs per usuari":
                    llistarPrestecsUsuari();
                    break;
                case "5":
                case "llibres en prestec":
                    llistarLlibresEnPrestec();
                    break;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho");
            }
            esperarEnter();
        }
    }
    
    public static void gestionaMenuPrincipal(Scanner scanner) {
        ArrayList<String> menuPrincipal = menuPrincipal();
    
        while (true) {
            clearScreen();
            dibuixarLlista(menuPrincipal);
    
            System.out.print("Escull una opció: ");
            String opcio = scanner.nextLine();
            switch (opcio.toLowerCase().replace("é", "e")) {
                case "0":
                case "sortir":
                    return;
                case "1":
                case "llibres":
                    gestionaMenuLlibres(scanner);
                    break;
                case "2":
                case "usuaris":
                    // Aquí se llama a la funcion para ir a Usuarios
                    break;
                case "3":
                case "préstecs":
                    gestionaMenuPrestecs(scanner);
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