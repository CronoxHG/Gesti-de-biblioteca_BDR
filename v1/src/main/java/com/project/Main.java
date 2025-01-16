package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
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

    // Validar y formatear entradas
    public static String formatarEntrada(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            return "";
        }
        return entrada.trim().substring(0, 1).toUpperCase() + entrada.trim().substring(1).toLowerCase();
    }

    public static List<Map<String, String>> carregarUsuaris(String filePath) {
        List<Map<String, String>> usuaris = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Map<String, String> usuari = new HashMap<>();
                usuari.put("Id", String.valueOf(obj.getInt("Id")));
                usuari.put("Nom", formatarEntrada(obj.getString("Nom")));
                usuari.put("Cognoms", formatarEntrada(obj.getString("Cognoms")));
                usuari.put("Telefon", obj.getString("Telefon"));
                usuaris.add(usuari);
            }
        } catch (NoSuchFileException e) {
            System.out.println("El fitxer '" + filePath + "' no existeix. Crea el fitxer abans d'executar el programa.");
        } catch (JSONException e) {
            System.out.println("El fitxer JSON té un format incorrecte: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al llegir el fitxer: " + e.getMessage());
        }

        return usuaris;
    }

    public static void guardarUsuaris(String filePath, List<Map<String, String>> usuaris) {
        JSONArray jsonArray = new JSONArray();

        for (Map<String, String> usuari : usuaris) {
            JSONObject obj = new JSONObject();
            obj.put("Id", Integer.parseInt(usuari.get("Id")));
            obj.put("Nom", usuari.get("Nom"));
            obj.put("Cognoms", usuari.get("Cognoms"));
            obj.put("Telefon", usuari.get("Telefon"));
            jsonArray.put(obj);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4));
            file.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar els usuaris: " + e.getMessage());
        }
    }

    public static void menuUsuaris(String filePath, List<Map<String, String>> usuaris) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            clearScreen();
            System.out.println("\n=== Gestió d'usuaris ===");
            System.out.println("1. Afegir Usuari");
            System.out.println("2. Modificar Usuari");
            System.out.println("3. Eliminar Usuari");
            System.out.println("4. Llistar Usuaris");
            System.out.println("0. Tornar");
            System.out.print("Escull una opció: ");

            String opcio = scanner.nextLine().trim();

            switch (opcio) {
                case "1":
                    afegirUsuari(filePath, usuaris);
                    break;
                case "2":
                    modificarUsuari(filePath, usuaris);
                    break;
                case "3":
                    eliminarUsuari(filePath, usuaris);
                    break;
                case "4":
                    llistarUsuaris(usuaris);
                    esperarEnter();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opció no vàlida. Torna a intentar-ho.");
            }
        }
    }

    public static void afegirUsuari(String filePath, List<Map<String, String>> usuaris) {
        Scanner scanner = new Scanner(System.in);
        Map<String, String> nouUsuari = new HashMap<>();

        System.out.print("Introdueix l'ID de l'usuari: ");
        nouUsuari.put("Id", scanner.nextLine().trim());
        System.out.print("Introdueix el nom de l'usuari: ");
        nouUsuari.put("Nom", formatarEntrada(scanner.nextLine()));
        System.out.print("Introdueix els cognoms de l'usuari: ");
        nouUsuari.put("Cognoms", formatarEntrada(scanner.nextLine()));
        System.out.print("Introdueix el telèfon de l'usuari: ");
        nouUsuari.put("Telefon", scanner.nextLine().trim());

        usuaris.add(nouUsuari);
        guardarUsuaris(filePath, usuaris);
        System.out.println("Usuari afegit correctament.");
    }

    public static void modificarUsuari(String filePath, List<Map<String, String>> usuaris) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix l'ID de l'usuari a modificar: ");
        String id = scanner.nextLine().trim();

        for (Map<String, String> usuari : usuaris) {
            if (usuari.get("Id").equals(id)) {
                System.out.print("Nou nom (actual: " + usuari.get("Nom") + "): ");
                usuari.put("Nom", formatarEntrada(scanner.nextLine()));
                System.out.print("Noves cognoms (actual: " + usuari.get("Cognoms") + "): ");
                usuari.put("Cognoms", formatarEntrada(scanner.nextLine()));
                System.out.print("Nou telèfon (actual: " + usuari.get("Telefon") + "): ");
                usuari.put("Telefon", scanner.nextLine().trim());

                guardarUsuaris(filePath, usuaris);
                System.out.println("Usuari modificat correctament.");
                return;
            }
        }
        System.out.println("No s'ha trobat cap usuari amb aquest ID.");
    }

    public static void eliminarUsuari(String filePath, List<Map<String, String>> usuaris) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix l'ID de l'usuari a eliminar: ");
        String id = scanner.nextLine().trim();

        for (int i = 0; i < usuaris.size(); i++) {
            if (usuaris.get(i).get("Id").equals(id)) {
                usuaris.remove(i);
                guardarUsuaris(filePath, usuaris);
                System.out.println("Usuari eliminat correctament.");
                return;
            }
        }
        System.out.println("No s'ha trobat cap usuari amb aquest ID.");
    }

    public static void llistarUsuaris(List<Map<String, String>> usuaris) {
        System.out.println("\n=== Llistat d'usuaris ===");
        System.out.println("ID\tNom\tCognoms\tTelèfon");
        for (Map<String, String> usuari : usuaris) {
            System.out.println(usuari.get("Id") + "\t" + usuari.get("Nom") + "\t" + usuari.get("Cognoms") + "\t" + usuari.get("Telefon"));
        }
    }

    public static void main(String[] args) {
        String filePathUsuaris = "./data/usuaris.json";
        List<Map<String, String>> usuaris = carregarUsuaris(filePathUsuaris);

        menuUsuaris(filePathUsuaris, usuaris);
    }
}
