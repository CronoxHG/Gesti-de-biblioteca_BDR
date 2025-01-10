package com.project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void afegir_prestec() {
        Scanner scanner = new Scanner(System.in);
        String filePath = "./data/prestecs.json";
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONArray prestecs = new JSONArray(content);
        // no puc preguntar l'id del prèstec hauria de ser autoincrement.
        System.out.print("Introdueix l'ID del llibre que vols: ");// podriem fer-lo també amb títol.
        String IdLlibre = scanner.nextLine();

        // verificar que el idLlibre es un número.
        try {
            Integer.parseInt(IdLlibre);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + IdLlibre + "no és un id vàlid.");
            return;
        }

        Integer idLlibreInteger = Integer.parseInt(IdLlibre);
        for (int i = 0; i < prestecs.length(); i++) {
            JSONObject prestec = prestecs.getJSONObject(i);
            // si el libro está prestado no puedo prestarlo.
            if (prestec.getInt("IdLlibre") == idLlibreInteger) {
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

        System.out.print("Introdueix l'ID del usuari: ");// podriem fer-lo també amb títol.
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
            if (prestec.getInt("IdUsuari") == idUsuariInteger){
                cnt += 1;                
            }
        }
        if (cnt>=4){
            try{
                throw new RuntimeException();
            } catch (RuntimeException e){
                System.out.println("El llibre amb id " + idLlibreInteger + " ja està prestat.");
                return;
            }
        }
    }

    // verificar que existeixin el llibre i l'usuari.

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
