package entrada;

import java.util.Scanner;

public class MatrizController {

    /**
     * Solicita al usuario el tamaño y los valores de la matriz de adyacencia.
     * @return int[][] Matriz cuadrada lista para ser enviada al modelo.
     */
    public int[][] obtenerMatriz() {
        Scanner scanner = new Scanner(System.in);
        int n = 0;

        System.out.println("--- CONFIGURACIÓN DEL GRAFO ---");

        // Solicitar y validar la cantidad de nodos (tamaño de la matriz n x n)
        while (n <= 0) {
            System.out.print("Ingrese la cantidad de nodos (tamaño de la matriz cuadrada): ");
            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
                if (n <= 0) {
                    System.out.println("Error: El número de nodos debe ser mayor a 0.");
                }
            } else {
                System.out.println("Error: Por favor, ingrese un número entero válido.");
                scanner.next(); // Limpiar la entrada incorrecta del buffer
            }
        }

        // Inicializar la matriz cuadrada
        int[][] matriz = new int[n][n];

        System.out.println("\n--- INGRESO DE ARISTAS ---");
        System.out.println("Ingrese los valores de la matriz de adyacencia.");
        System.out.println("(Nota: Un valor mayor a 0 indica conexión y su peso. Ingrese 0 si no hay conexión).");

        // Llenar la matriz celda por celda
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print("Peso de la conexión del Nodo " + i + " al Nodo " + j + ": ");

                // Validación para asegurar que el usuario ingrese números enteros
                while (!scanner.hasNextInt()) {
                    System.out.println("Entrada inválida. Ingrese un número entero.");
                    scanner.next(); // Limpiar la entrada incorrecta
                    System.out.print("Peso de la conexión del Nodo " + i + " al Nodo " + j + ": ");
                }

                matriz[i][j] = scanner.nextInt();
            }
        }

        System.out.println("\nMatriz capturada con éxito.");
        return matriz;
    }
}
