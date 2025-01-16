package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public static void main(String[] args) {
        // Especificar la ruta del archivo de usuarios
        String filePathUsuaris = "./data/usuaris.json";
        
        // Cargar los datos de los usuarios
        List<Map<String, String>> usuaris = cargarUsuaris(filePathUsuaris);

        // Menú principal
        //menuPrincipal(filePathUsuaris, usuaris);
    }

    // Cargar usuarios desde el archivo JSON
    public static List<Map<String, String>> cargarUsuaris(String filePath) {
        List<Map<String, String>> usuaris = new ArrayList<>();

        try {
            // Leer el contenido del archivo JSON
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            
            // Convertir el contenido a un array JSON
            JSONArray jsonArray = new JSONArray(content);

            // Recorrer el array JSON y almacenar los usuarios en la lista
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Map<String, String> usuari = new HashMap<>();
                usuari.put("Id", String.valueOf(obj.getInt("Id")));
                usuari.put("Nom", obj.getString("Nom"));
                usuari.put("Cognoms", obj.getString("Cognoms"));
                usuari.put("Telèfon", obj.getString("Telèfon"));
                usuaris.add(usuari);
            }
        } catch (IOException | JSONException e) {
            System.out.println("Error al cargar los datos del archivo: " + e.getMessage());
        }

        return usuaris;
    }

    // Guardar usuarios en el archivo JSON
    public static void guardarUsuaris(String filePath, List<Map<String, String>> usuaris) {
        JSONArray jsonArray = new JSONArray();

        // Recorrer la lista de usuarios y convertirla a un formato JSON
        for (Map<String, String> usuari : usuaris) {
            JSONObject obj = new JSONObject();
            obj.put("Id", Integer.parseInt(usuari.get("Id")));
            obj.put("Nom", usuari.get("Nom"));
            obj.put("Cognoms", usuari.get("Cognoms"));
            obj.put("Telèfon", usuari.get("Telèfon"));
            jsonArray.put(obj);
        }

        // Guardar el array JSON en el archivo
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4)); // Formato con indentación de 4 espacios
            file.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar los datos en el archivo: " + e.getMessage());
        }
    }// Menú principal
    public static void menuPrincipal(String filePathUsuaris, List<Map<String, String>> usuaris) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nGestió de biblioteca");
            System.out.println("1. Llibres");
            System.out.println("2. Usuaris");
            System.out.println("3. Préstecs");
            System.out.println("0. Sortir");
            System.out.print("Escull una opció: ");
            
            String opcio = scanner.nextLine();

            switch (opcio) {
                case "1":
                    // Crida a menuLlibres (pendent d'implementar)
                    break;
                case "2":
                    menuUsuaris(filePathUsuaris, usuaris);
                    break;
                case "3":
                    // Crida a menuPrestecs (pendent d'implementar)
                    break;
                case "0":
                    System.out.println("Sortint del programa...");
                    return;
                default:
                    System.out.println("Opció no vàlida.");
            }
        }
    }

    // Menú de gestió d'usuaris
    public static void menuUsuaris(String filePath, List<Map<String, String>> usuaris) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nGestió de usuaris");
            System.out.println("1. Afegir");
            System.out.println("2. Modificar");
            System.out.println("3. Eliminar");
            System.out.println("4. Llistar");
            System.out.println("0. Tornar al menú principal");
            System.out.print("Escull una opció: ");
            
            String opcio = scanner.nextLine();

            switch (opcio) {
                case "1":
                    //afegirUsuari(filePath, usuaris);
                    break;
                case "2":
                    //modificarUsuari(filePath, usuaris);
                    break;
                case "3":
                    //eliminarUsuari(filePath, usuaris);
                    break;
                case "4":
                    //llistarUsuaris(usuaris);
                    break;
                case "0":
                    return; // Torna al menú principal
                default:
                    System.out.println("Opció no vàlida. Intenta-ho de nou.");
            }
        }
    }


}