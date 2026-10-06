package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grafo {

    // Lista de vértices del grafo
    private List<Nodo> nodos;

    // Lista de conexiones entre los vértices
    private List<Arista> aristas;

    // Constructor vacío: crea el grafo sin nodos ni aristas
    public Grafo() {
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }

    // Llena el grafo a partir de una matriz de adyacencia
    public void construirDesdeMatriz(int[][] matriz) {
        validarMatriz(matriz);

        // Borra los datos anteriores
        nodos.clear();
        aristas.clear();

        int n = matriz.length;

        // Crea un nodo por cada fila (ids de 0 a n-1)
        for (int i = 0; i < n; i++) {
            nodos.add(new Nodo(i, 0, 0));
        }

        // Si la celda es mayor que 0, hay conexión de i a j y el valor es el peso
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriz[i][j] > 0) {
                    aristas.add(new Arista(nodos.get(i), nodos.get(j), matriz[i][j]));
                }
            }
        }
    }

    // Revisa que la matriz no sea nula, no esté vacía y sea cuadrada
    private void validarMatriz(int[][] matriz) {
        if (matriz == null || matriz.length == 0) {
            throw new IllegalArgumentException("La matriz no puede ser nula ni estar vacía.");
        }

        int n = matriz.length;

        for (int i = 0; i < n; i++) {
            if (matriz[i] == null || matriz[i].length != n) {
                throw new IllegalArgumentException(
                        "La matriz debe ser cuadrada: la fila " + i + " no tiene " + n + " columnas.");
            }
        }
    }

    // Devuelve los nodos (solo lectura)
    public List<Nodo> getNodos() {
        return Collections.unmodifiableList(nodos);
    }

    // Devuelve las aristas (solo lectura)
    public List<Arista> getAristas() {
        return Collections.unmodifiableList(aristas);
    }
}