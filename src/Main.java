import entrada.MatrizController;
import modelo.Grafo;
import visual.GrafoPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        //Pedir la matriz por consola
        MatrizController controller = new MatrizController();
        int[][] matriz = controller.obtenerMatriz();

        //Construir el modelo del grafo
        Grafo grafo = new Grafo();
        grafo.construirDesdeMatriz(matriz);

        //Desplegar la ventana gráfica
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Visualizador de Grafos");
            GrafoPanel panel = new GrafoPanel(grafo);

            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(600, 600);
            ventana.setLocationRelativeTo(null); // Centra la ventana en pantalla
            ventana.add(panel);
            ventana.setVisible(true);
        });
    }
}
