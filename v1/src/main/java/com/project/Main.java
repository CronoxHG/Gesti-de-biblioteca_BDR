package com.project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static boolean afegir_prestec() {
        String filePath = "./data/prestecs.json";
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray prestecs = new JSONArray(content);

        content.getJSONObject("0");
    if (prestecs.has("IdLlibre")){// existe el libro

        System.out.print("");
        prestecs.put("nom", nomLlibre)

    }
}

    public static String gestio_prestecs() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Escull una opció: ");
        String opc = scanner.nextLine();
        switch (opc) {
            case 1:
            case opc.equalIgnoreCase("afegir"):
                // afegir
                break;
            case 2:
            case opc.equalsIgnoreCase("modificar"):
                // modificar
                break;
            case 3:
            case opc.equalsIgnoreCase("eliminar"):
                // eliminar
                break;
            case 4:
            case opc.equalsIgnoreCase("llistar"):
                // eliminar
                break;
            case 0:
            case opc.equalsIgnoreCase("sortir"):
                // eliminar
                break;
            default:

        }
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

}
