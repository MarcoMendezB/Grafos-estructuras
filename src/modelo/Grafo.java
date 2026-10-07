package modelo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Grafo {

    private List<Nodo> nodos;
    private List<Arista> aristas;

    public Grafo() {
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
    }

    // =========================================================
    // CONSTRUIR GRAFO DESDE MATRIZ
    // =========================================================

    public void construirDesdeMatriz(int[][] matriz) {
        validarMatriz(matriz);

        nodos.clear();
        aristas.clear();

        int n = matriz.length;

        // Crear nodos
        for (int i = 0; i < n; i++) {
            nodos.add(new Nodo(i, 0, 0));
        }

        // Crear aristas
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (matriz[i][j] > 0) {
                    aristas.add(
                            new Arista(
                                    nodos.get(i),
                                    nodos.get(j),
                                    matriz[i][j]
                            )
                    );
                }
            }
        }
    }

    // =========================================================
    // VALIDAR MATRIZ
    // =========================================================

    private void validarMatriz(int[][] matriz) {

        if (matriz == null || matriz.length == 0) {
            throw new IllegalArgumentException(
                    "La matriz no puede ser nula ni estar vacía."
            );
        }

        int n = matriz.length;

        for (int i = 0; i < n; i++) {

            if (matriz[i] == null || matriz[i].length != n) {
                throw new IllegalArgumentException(
                        "La matriz debe ser cuadrada: la fila "
                                + i + " no tiene " + n + " columnas."
                );
            }
        }
    }

    // =========================================================
    // LISTA DE ADYACENCIA
    // =========================================================

    public Map<Nodo, List<Nodo>> obtenerListaAdyacencia() {

        Map<Nodo, List<Nodo>> listaAdyacencia =
                new LinkedHashMap<>();

        // Crear una lista vacía para cada nodo
        for (Nodo nodo : nodos) {
            listaAdyacencia.put(nodo, new ArrayList<>());
        }

        // Agregar los destinos
        for (Arista arista : aristas) {

            Nodo origen = arista.getNodoOrigen();
            Nodo destino = arista.getNodoDestino();

            listaAdyacencia.get(origen).add(destino);
        }

        return listaAdyacencia;
    }

    // =========================================================
    // OBTENER VECINOS
    // =========================================================

    public List<Nodo> obtenerVecinos(Nodo nodo) {

        List<Nodo> vecinos = new ArrayList<>();

        for (Arista arista : aristas) {

            if (arista.getNodoOrigen() == nodo) {
                vecinos.add(arista.getNodoDestino());
            }
        }

        return vecinos;
    }

    // =========================================================
    // RECORRIDO EN PROFUNDIDAD - DFS
    // =========================================================

    public List<Nodo> recorridoProfundidad(Nodo inicio) {

        List<Nodo> recorrido = new ArrayList<>();

        if (inicio == null) {
            return recorrido;
        }

        Set<Nodo> visitados = new HashSet<>();

        dfs(inicio, visitados, recorrido);

        return recorrido;
    }

    private void dfs(
            Nodo actual,
            Set<Nodo> visitados,
            List<Nodo> recorrido) {

        visitados.add(actual);
        recorrido.add(actual);

        for (Nodo vecino : obtenerVecinos(actual)) {

            if (!visitados.contains(vecino)) {
                dfs(vecino, visitados, recorrido);
            }
        }
    }

    // =========================================================
    // RECORRIDO EN ANCHURA - BFS
    // =========================================================

    public List<Nodo> recorridoAnchura(Nodo inicio) {

        List<Nodo> recorrido = new ArrayList<>();

        if (inicio == null) {
            return recorrido;
        }

        Set<Nodo> visitados = new HashSet<>();
        Queue<Nodo> cola = new ArrayDeque<>();

        visitados.add(inicio);
        cola.add(inicio);

        while (!cola.isEmpty()) {

            Nodo actual = cola.poll();

            recorrido.add(actual);

            for (Nodo vecino : obtenerVecinos(actual)) {

                if (!visitados.contains(vecino)) {

                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }

        return recorrido;
    }

    // =========================================================
    // OBTENER NODOS Y ARISTAS
    // =========================================================

    public List<Nodo> getNodos() {
        return Collections.unmodifiableList(nodos);
    }

    public List<Arista> getAristas() {
        return Collections.unmodifiableList(aristas);
    }
}
