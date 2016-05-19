/**
 * Created by Daniela Serna Escobar, Mauricio Hoyos Ardila, Diego Perez Gutierrez on 19/05/2016.
 */


import java.awt.Desktop;
import java.net.*;
import java.io.IOException;
import java.util.*;
import java.io.*;

public class Main {
    static ArrayList<Grafo.Vertices> vertices;
    static ArrayList<Grafo.Arco> edges;
    static Grafo.Vertices[] vertices1;
    static Grafo.Arco[] edges1;

    /**
     * este es el main del proyecto
     * @param args
     */
    public static void main(String[] args) {
        try {
            long TInicio, TFin, tiempo;
            TInicio = System.currentTimeMillis();
            vertices = new ArrayList<Grafo.Vertices>();
            edges = new ArrayList<Grafo.Arco>();
            String[] arreglo = cargarDatos("D:\\semestre 2016-1\\estructuras de datos y algoritmos 2\\Entregas\\src\\Medellin.txt",
                    cargarVisi("D:\\semestre 2016-1\\estructuras de datos y algoritmos 2\\Entregas\\src\\destinos.txt"));
            vertices1 = vertices.toArray(new Grafo.Vertices[vertices.size()]);
            edges1 = edges.toArray(new Grafo.Arco[edges.size()]);
            Grafo g = new Grafo(edges1, vertices1);
            //Scanner input = new Scanner(System.in);
            //System.out.println("ingrese el numero de nodos que desea visitar incluyendo la tienda");

            int i = 0;
            Float peso;
            Float menorpeso = 0f;
            int menorpesoindex = 0;
            int j = 0;
            int iterador = arreglo.length;
            boolean resetear = false;
            while (i < arreglo.length) {
                resetear = true;
                if (i != arreglo.length - 2) {
                    j = i + 1;
                } else {
                    break;
                }
                g.dijkstra(arreglo[i]);
                while (j < iterador) {
                    peso = g.ruta(arreglo[j], resetear);
                    if (j == i + 1) {
                        menorpeso = peso;
                        menorpesoindex = j;
                    }
                    if (resetear == true) {
                        resetear = false;
                    }
                    if (peso < menorpeso) {
                        menorpeso = peso;
                        menorpesoindex = j;
                    }

                    j++;
                }
                String temp = arreglo[i + 1];
                arreglo[i + 1] = arreglo[menorpesoindex];
                arreglo[menorpesoindex] = temp;
                i++;
            }
            mostrar(g,arreglo);
            TFin = System.currentTimeMillis();
            tiempo = TFin - TInicio;
            System.out.println("Tiempo de ejecución en milisegundos: " + tiempo);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    /**
     * carga el grafo del archivo txt para poderlo utilizar.
     * @param direccion esta es la direccion donde se encuentra el archivo.
     * @throws IOException
     */

    public static String [] cargarDatos(String direccion, ArrayList<String> cordenadas) throws IOException {
        String linea;
        int aux;
        String [] arreglo = new String[cordenadas.size()/2];
        String[] linea_dividida;
        BufferedReader buffered = new BufferedReader(new FileReader(direccion));
        linea = buffered.readLine();
        while ((linea = buffered.readLine()) != null) {
            if (!linea.isEmpty()) {
                linea_dividida = linea.split(" ");
                if (linea_dividida.length == 3) {
                    vertices.add(new Grafo.Vertices(linea_dividida[2], linea_dividida[1], linea_dividida[0]));
                    if (cordenadas.contains(linea_dividida[2])){
                        if (cordenadas.get(cordenadas.indexOf(linea_dividida[2])+1).equals(linea_dividida[1])){
                            aux = cordenadas.indexOf(linea_dividida[2])/2;
                            arreglo[aux] = linea_dividida[0];
                        }
                    }
                }
            } else {
                break;
            }
        }
        linea = buffered.readLine();
        while ((linea = buffered.readLine()) != null) {
            if (!linea.isEmpty()) {
                linea_dividida = linea.split(" ");
                edges.add(new Grafo.Arco(linea_dividida[0], linea_dividida[1], Float.parseFloat(linea_dividida[2])));
            } else {
                break;
            }
        }
        buffered.close();
        return arreglo;
    }

    /**
     * este metodo carga los vertices que tenemos que visitar
     * @param direccion es la direccion donde se encuentra la lista
     * @return retorna un arraylist con las cordenadas de los vertices
     * @throws IOException
     */
    public static ArrayList<String> cargarVisi (String direccion)throws IOException{
        String linea;
        ArrayList<String> cordenadas = new ArrayList<String>();
        String[] linea_dividida;
        BufferedReader buffered = new BufferedReader(new FileReader(direccion));
        while ((linea = buffered.readLine()) != null) {
            if (!linea.isEmpty()) {
                linea_dividida = linea.replace(",","").split(" ");
                cordenadas.add(linea_dividida[1]);
                cordenadas.add(linea_dividida[0]);
            } else {
                break;
            }
        }
        return cordenadas;
    }

    /**
     *este metodo se encarga de mostrar la ruta con Google maps.
     * @param arreglo es el arreglo de la ruta que debe de segir el grafo.
     * @param g este es el grafo sobre el cual se obtienen las cordenadas de cada vertice.
     */
    public static void mostrar(Grafo g, String[] arreglo) {
        try {
            String url = new String("https://www.google.es/maps/dir/");
            for (int k = 0; k <= arreglo.length; k++) {
                if (k != arreglo.length) {
                    url = url + g.grafo.get(arreglo[k]).y + "," + g.grafo.get(arreglo[k]).x + "/";
                } else {
                    url = url +g.grafo.get(arreglo[0]).y + "," +
                            g.grafo.get(arreglo[0]).x + "/@" + g.grafo.get(arreglo[0]).y + "," +
                            g.grafo.get(arreglo[0]).x + ",11z/data=!3m1!4b1";
                }
            }
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                }

            }
        } catch (Exception e) {
            System.out.print(e);
        }

    }

}
