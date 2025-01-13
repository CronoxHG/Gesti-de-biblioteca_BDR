package com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static String filePathPrestecs = "./data/prestecs.json";
    public static String filePathLlibres = "./data/llibres.json";
    public static String filePathUsuaris = "./data/usuaris.json";
    
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

    public static boolean digit(String num) {
        for (char numSeparat : num.toCharArray()) {
            if (!Character.isDigit(numSeparat)) {
                return false;
            }
        }
        return true;
    }

    public static boolean dataValida(String data) {
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

    public static void afegir_prestec() {
        Scanner scanner = new Scanner(System.in);
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
            System.out.println("Error: " + idLlibre + "no és un id vàlid.");
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

        // agafat la informació de l'arxiu llibres.json
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
                System.out.println("El llibre amb id " + idLlibreInteger + " ja està prestat.");
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

        System.out.print("Inserta la data de prèstec (aaaa-mm-dd): ");
        String data = scanner.nextLine();
        if (!dataValida(data)) {
            System.out.println("La data no és vàlida. Ha de ser amb el format aaaa-mm-dd");
            return;
        }
        String[] dataStringArray = data.split("-");
        Integer any = Integer.parseInt(dataStringArray[0]);
        Integer mes = Integer.parseInt(dataStringArray[1]);
        Integer dia = Integer.parseInt(dataStringArray[2]);
        String dataInsertar = (String.format("%04d-%02d-%02d", any, mes, dia));

        Integer numMesGran = 0;
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            if (prestec.getInt("id") > numMesGran) {
                numMesGran = prestec.getInt("id");
            }
        }

        // afegir el prestec dintre del fitxer json.
        JSONObject nouPrestec = new JSONObject();

        nouPrestec.put("id", numMesGran + 1); // TODO acabar
        nouPrestec.put("idLlibre", idLlibreInteger);
        nouPrestec.put("idUsuari", idUsuariInteger);
        nouPrestec.put("dataPrestec", dataInsertar);
        // hi ha que calcular la data de quan s'acaba la devolució.
        // prestecs.put("idUsuari",idUsuariInteger);

        prestecs.put(nouPrestec);
        String textoJson = prestecs.toString(4);
        try {
            Files.write(Paths.get(filePath), textoJson.getBytes());
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        scanner.close();
    }

    public static void esborrarPrestecs(){
        Scanner scanner = new Scanner(System.in);
        JSONArray prestecs = null;
        try {
            prestecs = prestecs();
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            scanner.close();
            return;
        }
        //mostrar la llista de llibres en prestec.
        llistarLlibresEnPrestec();

        System.out.print("Inserta l'id del préstec que vols esborrar: ");
        String idEsborrar = scanner.nextLine();
        System.out.println();
        scanner.close();

        if (!digit(idEsborrar)){
            System.out.println("L'id ha de ser un número.");
            return;
        }
        boolean idExistent = false;
        for (int i = 0;i<prestecs.length();i++){
            JSONObject prestec = prestecs.getJSONObject(i); 
            if (prestec.getInt("id") == Integer.parseInt(idEsborrar)){
                idExistent = true;
                prestecs.remove(i);
                String textoJson = prestecs.toString(4);
                try {
                    Files.write(Paths.get(filePathPrestecs), textoJson.getBytes());
                } catch (IOException e) {
                    System.out.println("Ha surgit un error inesperat en el fitxer.");
                    return;
                }
                llistarLlibresEnPrestec();
                System.out.println();
                System.out.println("S'ha esborrar el préstec.");
                return;
            }

        }
        if (!idExistent){
            System.out.println("L'id " + idEsborrar + " no existeix.");
            return;
        }
    }
    public static void llistatDePrestec() { // llista normal
        String filePathPrestecs = "./data/prestecs.json";
        String contentPrestecs = null;
        try {
            contentPrestecs = new String(Files.readAllBytes(Paths.get(filePathPrestecs)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray prestecs = new JSONArray(contentPrestecs);

        String filePathLlibres = "./data/llibres.json";
        String contentLlibres = null;
        try {
            contentLlibres = new String(Files.readAllBytes(Paths.get(filePathLlibres)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray llibres = new JSONArray(contentLlibres);

        String filePathUsuaris = "./data/usuaris.json";
        String contentUsuaris = null;
        try {
            contentUsuaris = new String(Files.readAllBytes(Paths.get(filePathUsuaris)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray usuaris = new JSONArray(contentUsuaris);

        System.out.println("-".repeat(118));
        System.out.println(String.format("| %-8s | %-35s | %25s | %12s | %12s |", "Id Prestec", "Títol del Llibre",
                "Nom i cognoms", "Data de prèstec", "Data de devolució"));
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            for (int j = 0; j < llibres.length(); j++) {
                JSONObject llibre = llibres.getJSONObject(j);
                for (int k = 0; k < usuaris.length(); k++) {
                    JSONObject usuari = usuaris.getJSONObject(k);
                    if (prestec.getInt("id") == llibre.getInt("id")) {
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
        Scanner scanner = new Scanner(System.in);
        // input de l'usuari verificant quin es el l'usuari que vol mirar.
        // en el cas de que l'usuari no existeixi o no tingui llibres en prestec,
        // imprimir un missatge i fer un return.
        String filePathUsuari = "./data/usuaris.json";
        String contentUsuaris = null;
        try {
            contentUsuaris = new String(Files.readAllBytes(Paths.get(filePathUsuari)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray usuaris = new JSONArray(contentUsuaris);

        String filePathPrestecs = "./data/prestecs.json";
        String contentPrestecs = null;
        try {
            contentPrestecs = new String(Files.readAllBytes(Paths.get(filePathPrestecs)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray prestecs = new JSONArray(contentPrestecs);

        String filePathLlibres = "./data/llibres.json";
        String contentLlibres = null;
        try {
            contentLlibres = new String(Files.readAllBytes(Paths.get(filePathLlibres)));
        } catch (IOException e) {
            System.out.println("Ha surgit un error inesperat en el fitxer.");
            return;
        }
        JSONArray llibres = new JSONArray(contentLlibres);

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
            System.out.println("L'usuari amb id " + idUsuari + " no té prèstecs.");
            return;
        }
    }

    public static void llistarLlibresEnPrestec() {
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
        String llibresEnPrestecs = "Llibres en prèstecs";
        Integer padding = 162 - llibresEnPrestecs.length();
        Integer espaiEsquerra = padding / 2;
        Integer espaiDreta = padding -espaiEsquerra;
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
                    if (llibre.getInt("id") == prestec.getInt("idLlibre")){
                        if (usuari.getInt("id") == prestec.getInt("idUsuari")){
                            System.out.println(String.format("| %-10s | %-35s | %-35s | %35s | %15s | %17s |", prestec.getInt("id"),
                            usuari.getString("nom") + " " + usuari.getString("cognoms"), llibre.getString("titol"),
                            llibre.getJSONArray("autor").join(", "), prestec.getString("dataPrestec"),
                            prestec.getString("dataDevolucio")));
                            System.out.println("-".repeat(166));
                        }
                    }
                }
            }
        }

    }

    public static void gestio_prestecs() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Escull una opció: ");
        String opc = scanner.nextLine();
        switch (opc.toLowerCase()) {
            case "1":
            case "afegir":
                afegir_prestec();
                break;
            case "2":
            case "modificar":
                // modificar
                break;
            case "3":
            case "esborrar":
                esborrarPrestecs();
                break;
            case "4":
            case "llistar":
                System.out.print("Escull una opció: ");
                opc = scanner.nextLine();
                switch (opc.toLowerCase()) {
                    case "1":
                    case "Llistat de préstecs":
                        llistatDePrestec();
                        break;
                    case "2":
                    case "Llistat de préstecs d'un usuari":
                        llistarPrestecsUsuari();
                        break;
                    case "3":
                    case "Llistat de llibres en préstec":
                        llistarLlibresEnPrestec();
                        break;
                        // default:
                        // break;
                }

                break;
            case "0":
            case "sortir":
                // sortir
                break;
            default:
                System.out.println("Opció invalida.");
                break;
        }
        scanner.close();
    }

    // mvn clean test-compile exec:java -P"runMain"
    // -D"exec.mainClass=com.project.Main"
    public static void main(String[] args) {
        System.out.println("Hello world!");
        gestio_prestecs();
    }

}
