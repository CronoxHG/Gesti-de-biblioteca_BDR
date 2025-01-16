package com.project;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    // Método para esperar que el usuario presione Enter
    public static void esperarEnter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Prem Enter per continuar...");
        scanner.nextLine();
    }

    // Método para limpiar la pantalla dependiendo del sistema operativo
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

    // Método para formatear las entradas, capitalizando la primera letra
    public static String formatarEntrada(String entrada) {
        if (entrada == null || entrada.trim().isEmpty()) {
            return "";
        }
        return entrada.trim().substring(0, 1).toUpperCase() + entrada.trim().substring(1).toLowerCase();
    }

    // Método para cargar los usuarios desde un archivo JSON
    public static List<JSONObject> carregarUsuaris(String filePath) {
        List<JSONObject> usuaris = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                usuaris.add(obj);
            }
        } catch (NoSuchFileException e) {
            System.out.println("El fitxer '" + filePath + "' no existeix. Crea el fitxer abans d'executar el programa.");
        } catch (IOException e) {
            System.out.println("Error al llegir el fitxer: " + e.getMessage());
        }

        return usuaris;
    }

    // Método para guardar los usuarios en el archivo JSON
    public static void guardarUsuaris(String filePath, List<JSONObject> usuaris) {
        JSONArray jsonArray = new JSONArray();

        for (JSONObject usuari : usuaris) {
            jsonArray.put(usuari);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4));
            file.flush();
        } catch (IOException e) {
            System.out.println("Error al guardar els usuaris: " + e.getMessage());
        }
    }

    // Método para obtener el próximo id disponible (reutiliza ids eliminados)
    public static int obtenirProximid(List<JSONObject> usuaris) {
        List<Integer> idsOcupats = new ArrayList<>();

        // Recopilar todos los ids ocupados
        for (JSONObject usuari : usuaris) {
            idsOcupats.add(usuari.getInt("id"));
        }

        // Buscar el primer id disponible (el primero que no esté en la lista de ids ocupados)
        int idDisponible = 1;
        while (idsOcupats.contains(idDisponible)) {
            idDisponible++;
        }

        return idDisponible;
    }

    // Método para agregar un nuevo usuario
    public static void afegirUsuari(String filePath, List<JSONObject> usuaris) {
        Scanner scanner = new Scanner(System.in);

        // Obtener el próximo id disponible
        int nuevoid = obtenirProximid(usuaris);

        JSONObject nouUsuari = new JSONObject();

        // Asignar automáticamente el id
        nouUsuari.put("id", nuevoid);

        // Solicitar el resto de la información
        System.out.print("Introdueix el nom de l'usuari: ");
        nouUsuari.put("nom", formatarEntrada(scanner.nextLine()));
        System.out.print("Introdueix els cognoms de l'usuari: ");
        nouUsuari.put("cognoms", formatarEntrada(scanner.nextLine()));
        System.out.print("Introdueix el telèfon de l'usuari: ");
        nouUsuari.put("telefon", scanner.nextLine().trim());

        usuaris.add(nouUsuari);
        guardarUsuaris(filePath, usuaris);
        System.out.println("Usuari afegit correctament amb l'id: " + nuevoid);
    }

    // Método para modificar un usuario
    public static void modificarUsuari(String filePath, List<JSONObject> usuaris) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix l'id de l'usuari a modificar: ");
        String id = scanner.nextLine().trim();

        for (JSONObject usuari : usuaris) {
            if (usuari.getInt("id") == Integer.parseInt(id)) {
                System.out.print("Nou nom (actual: " + usuari.getString("nom") + "): ");
                usuari.put("nom", formatarEntrada(scanner.nextLine()));
                System.out.print("Noves cognoms (actual: " + usuari.getString("cognoms") + "): ");
                usuari.put("cognoms", formatarEntrada(scanner.nextLine()));
                System.out.print("Nou telèfon (actual: " + usuari.getString("telefon") + "): ");
                usuari.put("telefon", scanner.nextLine().trim());

                guardarUsuaris(filePath, usuaris);
                System.out.println("Usuari modificat correctament.");
                return;
            }
        }
        System.out.println("No s'ha trobat cap usuari amb aquest id.");
    }

    // Método para eliminar un usuario
    public static void eliminarUsuari(String filePath, List<JSONObject> usuaris) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix l'id de l'usuari a eliminar: ");
        String id = scanner.nextLine().trim();

        for (int i = 0; i < usuaris.size(); i++) {
            if (usuaris.get(i).getInt("id") == Integer.parseInt(id)) {
                usuaris.remove(i);
                guardarUsuaris(filePath, usuaris);
                System.out.println("Usuari eliminat correctament.");
                return;
            }
        }
        System.out.println("No s'ha trobat cap usuari amb aquest id.");
    }

    // Método para listar los usuarios
    public static void llistarUsuaris(List<JSONObject> usuaris) {
        System.out.println("\n=== Llistat d'usuaris ===");
        System.out.println("id\tnom\tcognoms\tTelèfon");
        for (JSONObject usuari : usuaris) {
            System.out.println(usuari.getInt("id") + "\t" + usuari.getString("nom") + "\t" + usuari.getString("cognoms") + "\t" + usuari.getString("telefon"));
        }
    }

    // Menú principal para la gestión de usuarios
    public static void menuUsuaris(String filePath, List<JSONObject> usuaris) {
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

    public static void main(String[] args) {
        String filePathUsuaris = "usuaris.json";
        List<JSONObject> usuaris = carregarUsuaris(filePathUsuaris);

        menuUsuaris(filePathUsuaris, usuaris);
    }
}
