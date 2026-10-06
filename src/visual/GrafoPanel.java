package visual;

import modelo.Arista;
import modelo.Grafo;
import modelo.Nodo;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

public class GrafoPanel extends JPanel {

    private Grafo grafo;
    private static final int RADIO_NODO = 20; // Radio del círculo de cada nodo

    public GrafoPanel(Grafo grafo) {
        this.grafo = grafo;
        this.setBackground(Color.WHITE); // Fondo blanco para el panel
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
        repaint(); // Vuelve a pintar el panel si cambia el grafo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (grafo == null) return;

        Graphics2D g2d = (Graphics2D) g;
        // Habilitar suavizado de líneas y bordes (Anti-aliasing)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Nodo> nodos = grafo.getNodos();
        List<Arista> aristas = grafo.getAristas();

        if (nodos.isEmpty()) return;

        // 1. Asignar posiciones (x, y) circulares a los nodos según el tamaño del panel
        calcularPosicionesNodos(nodos);

        // 2. Dibujar las Aristas (líneas y pesos)
        for (Arista arista : aristas) {
            Nodo origen = arista.getNodoOrigen();
            Nodo destino = arista.getNodoDestino();

            int x1 = origen.getX();
            int y1 = origen.getY();
            int x2 = destino.getX();
            int y2 = destino.getY();

            // Dibujar la línea entre origen y destino
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine(x1, y1, x2, y2);

            // Calcular punto medio para escribir el peso de la arista
            int xMedio = (x1 + x2) / 2;
            int yMedio = (y1 + y2) / 2;

            g2d.setColor(Color.RED);
            g2d.drawString(String.valueOf(arista.getPeso()), xMedio, yMedio);
        }

        // 3. Dibujar los Nodos (círculos e ID)
        for (Nodo nodo : nodos) {
            int x = nodo.getX();
            int y = nodo.getY();

            // Relleno del círculo del nodo
            g2d.setColor(new Color(70, 130, 180)); // Azul acero
            g2d.fillOval(x - RADIO_NODO, y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);

            // Borde del círculo
            g2d.setColor(Color.BLACK);
            g2d.drawOval(x - RADIO_NODO, y - RADIO_NODO, 2 * RADIO_NODO, 2 * RADIO_NODO);

            // Texto (ID del nodo) dentro del círculo
            g2d.setColor(Color.WHITE);
            String texto = String.valueOf(nodo.getId());
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x - (fm.stringWidth(texto) / 2);
            int textY = y + (fm.getAscent() / 4);

            g2d.drawString(texto, textX, textY);
        }
    }

    /**
     * Calcula y asigna las coordenadas (x, y) de los nodos para que queden
     * distribuidos equitativamente en forma de círculo.
     */
    private void calcularPosicionesNodos(List<Nodo> nodos) {
        int totalNodos = nodos.size();
        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int radioGrafo = Math.min(centroX, centroY) - 60; // Margen interno

        if (radioGrafo < 40) radioGrafo = 40;

        for (int i = 0; i < totalNodos; i++) {
            double angulo = 2 * Math.PI * i / totalNodos;
            int x = (int) (centroX + radioGrafo * Math.cos(angulo));
            int y = (int) (centroY + radioGrafo * Math.sin(angulo));

            // Actualiza las coordenadas del nodo
            nodos.get(i).setX(x);
            nodos.get(i).setY(y);
        }
    }
}