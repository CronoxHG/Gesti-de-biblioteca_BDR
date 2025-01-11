package com.project;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static boolean digit(String num){
        for (char numSeparat : num.toCharArray()){
            if (!Character.isDigit(numSeparat)){
                return false;
            }
        }
        return true;
    }
    public static boolean dataValida(String data){
        String[] dataStringArray = data.split("-");
        //mirar de que tots els elements de l'array siguin numeros sencers.
        if (dataStringArray.length!=3){ //comprobar que la longitut del array que conté la data es correcte.
            return false;
        }
        for (String num : dataStringArray){
            if (!digit(num)){
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

        if (("0"+mes).equals("02") && dia > 29) {
            return false;
        }

        if (!(esBisiesto) && ("0"+mes).equals("02") && dia > 28) {
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

        //agafat la informació de l'arxiu llibres.json
        String filePathLlibres = "./data/llibres.json";
        String contentLlibres = new String(Files.readAllBytes(Paths.get(filePathLlibres)));
        JSONArray llibres = new JSONArray(contentLlibres);
        //agafat la informació de l'arxiu usuaris.json
        String filePathUsuari = "./data/usuaris.json";
        String contentUsuari = new String(Files.readAllBytes(Paths.get(filePathLlibres)));
        JSONArray usuaris = new JSONArray(contentLlibres);
    
        for (int i = 0; i<llibres.length();i++){
            JSONObject llibre = llibres.getJSONObject(i);
            if (!(llibre.getInt("Id") == idLlibreInteger)){
                return;
            }
        }
        for (int j = 0;j<usuaris.length();j++){
            JSONObject usuari = usuaris.getJSONObject(j);
            if (!(usuari.getInt("Id") == idUsuariInteger)){
                return;
            }
        }

        System.out.print("Inserta la data de prèstec: ");
        String data = scanner.nextLine();
        if (!dataValida(data)){
            return;
        }
        String[] dataStringArray = data.split("-");
        Integer any = Integer.parseInt(dataStringArray[0]);
        Integer mes = Integer.parseInt(dataStringArray[1]);
        Integer dia = Integer.parseInt(dataStringArray[2]);
        String dataInsertar = (String.format("%04d-%02d-%02d",any,mes,dia));
        
    }



    // verificar que existeixin el llibre i l'usuari.
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
            case "eliminar":
                // eliminar
                break;
            case "4":
            case "llistar":
                // eliminar
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

    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

}
