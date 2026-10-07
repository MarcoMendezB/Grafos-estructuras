package visual;

import modelo.Arista;
import modelo.Grafo;
import modelo.Nodo;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;
import java.util.List;
import java.util.Map;

public class GrafoPanel extends JPanel {

    private Grafo grafo;

    private static final int RADIO_NODO = 20;

    // Altura reservada para la información inferior
    private static final int ALTURA_INFORMACION = 140;

    public GrafoPanel(Grafo grafo) {
        this.grafo = grafo;
        setBackground(Color.WHITE);
    }

    public void setGrafo(Grafo grafo) {
        this.grafo = grafo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (grafo == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        List<Nodo> nodos = grafo.getNodos();
        List<Arista> aristas = grafo.getAristas();

        if (nodos.isEmpty()) {
            return;
        }

        // =====================================================
        // POSICIONES DE LOS NODOS
        // =====================================================

        calcularPosicionesNodos(nodos);

        // =====================================================
        // DIBUJAR ARISTAS
        // =====================================================

        dibujarAristas(g2d, aristas);

        // =====================================================
        // DIBUJAR NODOS
        // =====================================================

        dibujarNodos(g2d, nodos);

        // =====================================================
        // DIBUJAR INFORMACIÓN
        // =====================================================

        dibujarInformacion(g2d);
    }

    // =========================================================
    // DIBUJAR ARISTAS
    // =========================================================

    private void dibujarAristas(
            Graphics2D g2d,
            List<Arista> aristas) {

        for (Arista arista : aristas) {

            Nodo origen = arista.getNodoOrigen();
            Nodo destino = arista.getNodoDestino();

            int x1 = origen.getX();
            int y1 = origen.getY();

            int x2 = destino.getX();
            int y2 = destino.getY();

            // -----------------------------------------------
            // CASO 1: ARISTA DEL NODO A SÍ MISMO
            // -----------------------------------------------

            if (origen == destino) {

                dibujarLazo(
                        g2d,
                        origen,
                        arista.getPeso()
                );

                continue;
            }

            // -----------------------------------------------
            // VERIFICAR SI EXISTE LA ARISTA CONTRARIA
            // -----------------------------------------------

            boolean existeContraria =
                    existeAristaContraria(
                            aristas,
                            origen,
                            destino
                    );

            g2d.setColor(Color.DARK_GRAY);

            if (existeContraria) {

                // Si existen ambas direcciones,
                // se dibujan curvas diferentes.

                dibujarAristaCurva(
                        g2d,
                        x1,
                        y1,
                        x2,
                        y2,
                        arista.getPeso(),
                        origen.getId() < destino.getId()
                );

            } else {

                // Arista normal
                dibujarAristaRecta(
                        g2d,
                        x1,
                        y1,
                        x2,
                        y2,
                        arista.getPeso()
                );
            }
        }
    }

    // =========================================================
    // ARISTA NORMAL
    // =========================================================

    private void dibujarAristaRecta(
            Graphics2D g2d,
            int x1,
            int y1,
            int x2,
            int y2,
            int peso) {

        g2d.setColor(Color.DARK_GRAY);

        g2d.drawLine(
                x1,
                y1,
                x2,
                y2
        );

        // Flecha
        dibujarFlecha(
                g2d,
                x1,
                y1,
                x2,
                y2
        );

        // Peso
        int xMedio = (x1 + x2) / 2;
        int yMedio = (y1 + y2) / 2;

        dibujarPeso(
                g2d,
                peso,
                xMedio,
                yMedio
        );
    }

    // =========================================================
    // ARISTA CURVA
    // =========================================================

    private void dibujarAristaCurva(
            Graphics2D g2d,
            int x1,
            int y1,
            int x2,
            int y2,
            int peso,
            boolean arriba) {

        double dx = x2 - x1;
        double dy = y2 - y1;

        double distancia =
                Math.sqrt(dx * dx + dy * dy);

        if (distancia == 0) {
            return;
        }

        // Vector perpendicular
        double px = -dy / distancia;
        double py = dx / distancia;

        // Separación entre las dos curvas
        double separacion = 35;

        if (!arriba) {
            separacion = -35;
        }

        double controlX =
                (x1 + x2) / 2.0
                        + px * separacion;

        double controlY =
                (y1 + y2) / 2.0
                        + py * separacion;

        QuadCurve2D curva =
                new QuadCurve2D.Double(
                        x1,
                        y1,
                        controlX,
                        controlY,
                        x2,
                        y2
                );

        g2d.setColor(Color.DARK_GRAY);

        g2d.draw(curva);

        // -----------------------------------------------
        // FLECHA EN LA CURVA
        // -----------------------------------------------

        double t = 0.90;

        double xt =
                Math.pow(1 - t, 2) * x1
                        + 2 * (1 - t) * t * controlX
                        + Math.pow(t, 2) * x2;

        double yt =
                Math.pow(1 - t, 2) * y1
                        + 2 * (1 - t) * t * controlY
                        + Math.pow(t, 2) * y2;

        double dxCurva =
                2 * (1 - t) * (controlX - x1)
                        + 2 * t * (x2 - controlX);

        double dyCurva =
                2 * (1 - t) * (controlY - y1)
                        + 2 * t * (y2 - controlY);

        dibujarFlecha(
                g2d,
                (int) (xt - dxCurva * 0.1),
                (int) (yt - dyCurva * 0.1),
                (int) xt,
                (int) yt
        );

        // -----------------------------------------------
        // PESO
        // -----------------------------------------------

        double tm = 0.5;

        double pesoX =
                Math.pow(1 - tm, 2) * x1
                        + 2 * (1 - tm) * tm * controlX
                        + Math.pow(tm, 2) * x2;

        double pesoY =
                Math.pow(1 - tm, 2) * y1
                        + 2 * (1 - tm) * tm * controlY
                        + Math.pow(tm, 2) * y2;

        dibujarPeso(
                g2d,
                peso,
                (int) pesoX,
                (int) pesoY
        );
    }

    // =========================================================
    // FLECHA
    // =========================================================

    private void dibujarFlecha(
            Graphics2D g2d,
            int x1,
            int y1,
            int x2,
            int y2) {

        double angulo =
                Math.atan2(
                        y2 - y1,
                        x2 - x1
                );

        int largo = 10;

        int xF1 =
                (int) (
                        x2
                                - largo
                                * Math.cos(angulo - Math.PI / 6)
                );

        int yF1 =
                (int) (
                        y2
                                - largo
                                * Math.sin(angulo - Math.PI / 6)
                );

        int xF2 =
                (int) (
                        x2
                                - largo
                                * Math.cos(angulo + Math.PI / 6)
                );

        int yF2 =
                (int) (
                        y2
                                - largo
                                * Math.sin(angulo + Math.PI / 6)
                );

        g2d.drawLine(x2, y2, xF1, yF1);
        g2d.drawLine(x2, y2, xF2, yF2);
    }

    // =========================================================
    // PESO
    // =========================================================

    private void dibujarPeso(
            Graphics2D g2d,
            int peso,
            int x,
            int y) {

        String texto =
                String.valueOf(peso);

        FontMetrics fm =
                g2d.getFontMetrics();

        int ancho =
                fm.stringWidth(texto);

        int alto =
                fm.getHeight();

        // Fondo blanco para que el peso
        // no quede encima de la línea
        g2d.setColor(Color.WHITE);

        g2d.fillRect(
                x - ancho / 2 - 3,
                y - alto + 3,
                ancho + 6,
                alto
        );

        // Texto
        g2d.setColor(Color.RED);

        g2d.drawString(
                texto,
                x - ancho / 2,
                y
        );
    }

    // =========================================================
    // LAZOS
    // =========================================================

    private void dibujarLazo(
            Graphics2D g2d,
            Nodo nodo,
            int peso) {

        int x = nodo.getX();
        int y = nodo.getY();

        g2d.setColor(Color.DARK_GRAY);

        g2d.drawOval(
                x - 30,
                y - 45,
                30,
                30
        );

        g2d.setColor(Color.RED);

        g2d.drawString(
                String.valueOf(peso),
                x - 15,
                y - 50
        );
    }

    // =========================================================
    // COMPROBAR ARISTA CONTRARIA
    // =========================================================

    private boolean existeAristaContraria(
            List<Arista> aristas,
            Nodo origen,
            Nodo destino) {

        for (Arista arista : aristas) {

            if (arista.getNodoOrigen() == destino
                    && arista.getNodoDestino() == origen) {

                return true;
            }
        }

        return false;
    }

    // =========================================================
    // DIBUJAR NODOS
    // =========================================================

    private void dibujarNodos(
            Graphics2D g2d,
            List<Nodo> nodos) {

        for (Nodo nodo : nodos) {

            int x = nodo.getX();
            int y = nodo.getY();

            // Fondo
            g2d.setColor(
                    new Color(70, 130, 180)
            );

            g2d.fillOval(
                    x - RADIO_NODO,
                    y - RADIO_NODO,
                    RADIO_NODO * 2,
                    RADIO_NODO * 2
            );

            // Borde
            g2d.setColor(Color.BLACK);

            g2d.drawOval(
                    x - RADIO_NODO,
                    y - RADIO_NODO,
                    RADIO_NODO * 2,
                    RADIO_NODO * 2
            );

            // ID
            g2d.setColor(Color.WHITE);

            String texto =
                    String.valueOf(nodo.getId());

            FontMetrics fm =
                    g2d.getFontMetrics();

            int textX =
                    x - fm.stringWidth(texto) / 2;

            int textY =
                    y + fm.getAscent() / 4;

            g2d.drawString(
                    texto,
                    textX,
                    textY
            );
        }
    }

    // =========================================================
    // INFORMACIÓN
    // =========================================================

    private void dibujarInformacion(
            Graphics2D g2d) {

        int inicioY =
                getHeight() - ALTURA_INFORMACION;

        // Separador
        g2d.setColor(Color.LIGHT_GRAY);

        g2d.drawLine(
                0,
                inicioY,
                getWidth(),
                inicioY
        );

        // -----------------------------------------------------
        // TÍTULO
        // -----------------------------------------------------

        g2d.setColor(Color.BLACK);

        g2d.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        g2d.drawString(
                "Lista de Adyacencia",
                20,
                inicioY + 25
        );

        // -----------------------------------------------------
        // LISTA DE ADYACENCIA
        // -----------------------------------------------------

        g2d.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        Map<Nodo, List<Nodo>> lista =
                grafo.obtenerListaAdyacencia();

        int x = 20;
        int y = inicioY + 48;

        for (Map.Entry<Nodo, List<Nodo>> entrada
                : lista.entrySet()) {

            StringBuilder texto =
                    new StringBuilder();

            texto.append(
                    "Nodo "
            );

            texto.append(
                    entrada.getKey().getId()
            );

            texto.append(" -> ");

            List<Nodo> vecinos =
                    entrada.getValue();

            if (vecinos.isEmpty()) {

                texto.append("sin conexiones");

            } else {

                for (int i = 0;
                     i < vecinos.size();
                     i++) {

                    texto.append(
                            vecinos.get(i).getId()
                    );

                    if (i < vecinos.size() - 1) {
                        texto.append(", ");
                    }
                }
            }

            g2d.setColor(Color.BLACK);

            g2d.drawString(
                    texto.toString(),
                    x,
                    y
            );

            x += 120;

            // Si no cabe, pasar a otra fila
            if (x > getWidth() - 150) {

                x = 20;
                y += 20;
            }
        }

        // -----------------------------------------------------
        // RECORRIDOS
        // -----------------------------------------------------

        int yRecorrido =
                inicioY + 105;

        if (!grafo.getNodos().isEmpty()) {

            Nodo inicio =
                    grafo.getNodos().get(0);

            List<Nodo> dfs =
                    grafo.recorridoProfundidad(inicio);

            List<Nodo> bfs =
                    grafo.recorridoAnchura(inicio);

            // DFS
            g2d.setColor(
                    new Color(0, 120, 0)
            );

            g2d.setFont(
                    new Font(
                            "Arial",
                            Font.BOLD,
                            13
                    )
            );

            g2d.drawString(
                    "DFS (Profundidad): "
                            + convertirRecorridoTexto(dfs),
                    20,
                    yRecorrido
            );

            // BFS
            g2d.setColor(
                    new Color(0, 0, 160)
            );

            g2d.drawString(
                    "BFS (Anchura): "
                            + convertirRecorridoTexto(bfs),
                    20,
                    yRecorrido + 22
            );
        }
    }

    // =========================================================
    // CONVERTIR RECORRIDO A TEXTO
    // =========================================================

    private String convertirRecorridoTexto(
            List<Nodo> recorrido) {

        StringBuilder texto =
                new StringBuilder();

        for (int i = 0;
             i < recorrido.size();
             i++) {

            texto.append(
                    recorrido.get(i).getId()
            );

            if (i < recorrido.size() - 1) {

                texto.append(
                        " -> "
                );
            }
        }

        return texto.toString();
    }

    // =========================================================
    // POSICIONES DE LOS NODOS
    // =========================================================

    private void calcularPosicionesNodos(
            List<Nodo> nodos) {

        int totalNodos =
                nodos.size();

        int centroX =
                getWidth() / 2;

        // Dejamos espacio abajo
        int centroY =
                (getHeight() - ALTURA_INFORMACION)
                        / 2;

        int radioGrafo =
                Math.min(
                        centroX,
                        centroY
                ) - 60;

        if (radioGrafo < 40) {
            radioGrafo = 40;
        }

        for (int i = 0;
             i < totalNodos;
             i++) {

            double angulo =
                    2 * Math.PI * i
                            / totalNodos;

            int x =
                    (int) (
                            centroX
                                    + radioGrafo
                                    * Math.cos(angulo)
                    );

            int y =
                    (int) (
                            centroY
                                    + radioGrafo
                                    * Math.sin(angulo)
                    );

            nodos.get(i).setX(x);
            nodos.get(i).setY(y);
        }
    }
}
