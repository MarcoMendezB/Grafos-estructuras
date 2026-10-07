package entrada;

import modelo.Grafo;
import modelo.Nodo;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MatrizController {

    /**
     * Solicita al usuario el tamaño y los valores de la matriz.
     */
    public int[][] obtenerMatriz() {

        Scanner scanner = new Scanner(System.in);
        int n = 0;

        System.out.println("--- CONFIGURACIÓN DEL GRAFO ---");

        // Solicitar cantidad de nodos
        while (n <= 0) {

            System.out.print(
                    "Ingrese la cantidad de nodos (tamaño de la matriz cuadrada): "
            );

            if (scanner.hasNextInt()) {

                n = scanner.nextInt();

                if (n <= 0) {
                    System.out.println(
                            "Error: El número de nodos debe ser mayor a 0."
                    );
                }

            } else {

                System.out.println(
                        "Error: Por favor, ingrese un número entero válido."
                );

                scanner.next();
            }
        }

        // Crear matriz
        int[][] matriz = new int[n][n];

        System.out.println("\n--- INGRESO DE ARISTAS ---");

        System.out.println(
                "Ingrese los valores de la matriz de adyacencia."
        );

        System.out.println(
                "(Un valor mayor a 0 indica conexión y su peso. "
                        + "Ingrese 0 si no hay conexión)."
        );

        // Llenar matriz
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                System.out.print(
                        "Peso de la conexión del Nodo "
                                + i + " al Nodo " + j + ": "
                );

                while (!scanner.hasNextInt()) {

                    System.out.println(
                            "Entrada inválida. Ingrese un número entero."
                    );

                    scanner.next();

                    System.out.print(
                            "Peso de la conexión del Nodo "
                                    + i + " al Nodo " + j + ": "
                    );
                }

                matriz[i][j] = scanner.nextInt();
            }
        }

        System.out.println("\nMatriz capturada con éxito.");

        return matriz;
    }

    // =========================================================
    // MOSTRAR LISTA DE ADYACENCIA
    // =========================================================

    public void mostrarListaAdyacencia(Grafo grafo) {

        System.out.println("\n=================================");
        System.out.println("       LISTA DE ADYACENCIA");
        System.out.println("=================================");

        Map<Nodo, List<Nodo>> lista =
                grafo.obtenerListaAdyacencia();

        for (Map.Entry<Nodo, List<Nodo>> entrada : lista.entrySet()) {

            System.out.print(
                    "Nodo " + entrada.getKey().getId() + " -> "
            );

            List<Nodo> vecinos = entrada.getValue();

            if (vecinos.isEmpty()) {

                System.out.print("sin conexiones");

            } else {

                for (int i = 0; i < vecinos.size(); i++) {

                    System.out.print(
                            vecinos.get(i).getId()
                    );

                    if (i < vecinos.size() - 1) {
                        System.out.print(", ");
                    }
                }
            }

            System.out.println();
        }
    }

    // =========================================================
    // MOSTRAR RECORRIDO EN PROFUNDIDAD
    // =========================================================

    public void mostrarProfundidad(Grafo grafo) {

        System.out.println("\n=================================");
        System.out.println("    RECORRIDO EN PROFUNDIDAD");
        System.out.println("              DFS");
        System.out.println("=================================");

        if (grafo.getNodos().isEmpty()) {
            System.out.println("El grafo no tiene nodos.");
            return;
        }

        // Comenzar desde el nodo 0
        Nodo inicio = grafo.getNodos().get(0);

        List<Nodo> recorrido =
                grafo.recorridoProfundidad(inicio);

        System.out.print("Recorrido: ");

        for (int i = 0; i < recorrido.size(); i++) {

            System.out.print(
                    recorrido.get(i).getId()
            );

            if (i < recorrido.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
    }

    // =========================================================
    // MOSTRAR RECORRIDO EN ANCHURA
    // =========================================================

    public void mostrarAnchura(Grafo grafo) {

        System.out.println("\n=================================");
        System.out.println("      RECORRIDO EN ANCHURA");
        System.out.println("              BFS");
        System.out.println("=================================");

        if (grafo.getNodos().isEmpty()) {
            System.out.println("El grafo no tiene nodos.");
            return;
        }

        // Comenzar desde el nodo 0
        Nodo inicio = grafo.getNodos().get(0);

        List<Nodo> recorrido =
                grafo.recorridoAnchura(inicio);

        System.out.print("Recorrido: ");

        for (int i = 0; i < recorrido.size(); i++) {

            System.out.print(
                    recorrido.get(i).getId()
            );

            if (i < recorrido.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
    }

    // =========================================================
    // MOSTRAR TODO
    // =========================================================

    public void mostrarInformacionGrafo(Grafo grafo) {

        mostrarListaAdyacencia(grafo);
        mostrarProfundidad(grafo);
        mostrarAnchura(grafo);
    }
}
