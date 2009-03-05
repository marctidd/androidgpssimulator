/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package androidgpssimulator.kml;

import androidgpssimulator.locationSender.Location;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author JuanmaSP
 */
public class KMLToRute {
    public static List<Location> KMLToRute(File file) throws FileNotFoundException, IOException{
        List<Location> locations = new LinkedList<Location>();

        BufferedReader bf = new BufferedReader(new FileReader(file));

        String line;
        while((line = bf.readLine()) != null){
            if(line.contains("<coordinates>")){
                line = bf.readLine();
                int pos = line.indexOf("</coordinates>");
                line = line.substring(0, pos);
                separarCoordenadas(line, locations);
            }
        }

        bf.close();

        return locations;
    }

    private static void separarCoordenadas(String texto, List<Location> lista){
        String[] localizaciones = texto.split(" ");
        for(int i = 0; i < localizaciones.length; i ++){
            String[] numeros = localizaciones[i].split(",");
            Location loc;
            try{
                double longitud = Double.parseDouble(numeros[0]);
                double latitud = Double.parseDouble(numeros[1]);
                int altitud = Integer.parseInt(numeros[2]);

                loc = new Location(latitud, longitud, altitud);
                lista.add(loc);
            }
            catch(NumberFormatException e){
                System.out.println("Error al parsear coordenadas: " + e.getMessage());
            }
        }
    }
}
