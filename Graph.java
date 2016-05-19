/**
 * Created by Daniela Serna Escobar, Mauricio Hoyos Ardila, Diego Perez Gutierrez on 19/05/2016.
 */

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * esta es la clase principal en la cual se llevan a cabo todos los procesos para encontrar la ruta optima aproximada
 * sacada de http://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java
 * presenta algunas modificaciones
 */
class Grafo {
    public final Map<String, Vertices> grafo;

    /**
     * esta es la clase que se encarga de representar los arcos
     */
    public static class Arco {
        public final String inicioA, finalA;
        public final float distancia;

        /**
         * este es el constructor de la clase arco
         * @param inicioA   es el vertice del cual sale o inicia el arco
         * @param finalA    es el vertice de destino o de llegada del arco
         * @param distancia es la distancia o peso que tiene este arco dentro del grafo
         */
        public Arco(String inicioA, String finalA, float distancia) {
            this.inicioA = inicioA;
            this.finalA = finalA;
            this.distancia = distancia;
        }
    }
/**
 * esta clase es la implementacion de cada vertice
 */
    /** One vertex of the grafo, complete with mappings to neighbouring vertices */
    public static class Vertices implements Comparable<Vertices> {
        public final String nombre;
        public float peso=0;
        public Float distancia = Float.MAX_VALUE; // MAX_VALUE assumed to be infinity
        public Vertices anterior = null;
        public final Map<Vertices, Float> sucesores = new HashMap<>();
        public String x,y;

        /**
         * es el constructor de la clase Vertices
         * @param x es la cordenada x del vertice
         * @param y es la cordenada y del vertice
         * @param nombre    es el nombre del vertice
         */
        public Vertices(String x, String y,String nombre) {
            this.nombre = nombre;
            this.x=x;
            this.y=y;
        }

        /**
         * este metodo saca el peso que se encuentra de un vertice que tiene que ser
         * visitado a otro que tambien tiene que ser visitado
         * @param devolver
         * @return el peso del vertice sobre el cual esta parado hasta un vertice expecificado
         */
        private Float ruta(boolean devolver) {
            if(devolver=true)
            {
                this.peso=0;
            }
            if (this == this.anterior) {
                return peso;
            } else if (this.anterior == null) {
            } else {
                this.anterior.ruta(false);
                peso=peso+this.distancia;
                return peso;
            }
            return peso;
        }

        /**
         * este metodo se encarga de comparar cual de los vertices es mas cercano a si mismo
         * @param other
         * @return  int
         */
        public int compareTo(Vertices other) {
            return Float.compare(distancia, other.distancia);
        }
    }

    /**
     * este metodo es el constructor del grafo
     * @param arcos es el arreglo de todos los arcos que existen dentro del grafo
     * @param vertices  es el arreglo de vertices que existen dentro del grafo
     */
    /** Builds a grafo from a set of arcos */
    public Grafo(Arco[] arcos, Vertices[] vertices) {
        grafo = new HashMap<>(vertices.length);

        //one pass to find all vertices
        for (Vertices e: vertices) {

            if (!grafo.containsKey(e.nombre))
            {
                grafo.put(e.nombre,e);
            }
        }

        //another pass to set neighbouring vertices
        for (Arco e : arcos) {
            if(e.distancia!=0f) {
                grafo.get(e.inicioA).sucesores.put(grafo.get(e.finalA), e.distancia);
            }
        }
    }

    /**
     * permite ejecutar el algoritmo de dijkstra desde un vertice en expecifico
     * @param comienzo es el vertice del cual se desea partir
     */
    /** Runs dijkstra using a specified source vertex */
    public void dijkstra(String comienzo) {
        if (!grafo.containsKey(comienzo)) {
            System.err.printf("Graph doesn't contain start vertex \"%s\"\n", comienzo);
            return;
        }
        final Vertices source = grafo.get(comienzo);
        NavigableSet<Vertices> q = new TreeSet<>();

        // set-up vertices
        for (Vertices v : grafo.values()) {
            v.anterior = v == source? source : null;
            v.distancia = v == source ? 0 : Float.MAX_VALUE;
            q.add(v);
        }

        dijkstra(q);
    }


    /** Implementation of dijkstra's algorithm using a binary heap. */
    private void dijkstra(final NavigableSet<Vertices> q) {
        Vertices u, v;
        while (!q.isEmpty()) {

            u = q.pollFirst(); // vertex with shortest distanciaance (first iteration will return source)
            if (u.distancia == Float.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable

            //look at distanciaances to each neighbour
            for (Map.Entry<Vertices, Float> a : u.sucesores.entrySet()) {
                v = a.getKey(); //the neighbour in this iteration

                final Float alternateDist = u.distancia + a.getValue();
                if (alternateDist < v.distancia) { // shorter path to neighbour found
                    q.remove(v);
                    v.distancia = alternateDist;
                    v.anterior = u;
                    q.add(v);
                }
            }
        }
    }

    /**
     * este metodo saca un camino a un determinado vertice
     * @param nombreFinal es el nombre del vertice sobre el cual se desea saber el camino
     * @param devolver  esta variable nos permite saber el si podemos devolver el peso del camino total al destino
     */
    /** Prints a path from the source to the specified vertex */
    public Float ruta(String nombreFinal, boolean devolver) {
        if (!grafo.containsKey(nombreFinal)) {
            System.err.printf("Graph doesn't contain end vertex \"%s\"\n", nombreFinal);
            return 0f;
        }
       // System.out.println();
        return grafo.get(nombreFinal).ruta(devolver);

    }
}