package modelo;

public class Arista {
    public Nodo nodoOrigen;
    public Nodo nodoDestino;
    public int peso;

    public Arista(Nodo nodoOrigen, Nodo nodoDestino, int peso) {
        this.nodoOrigen = nodoOrigen;
        this.nodoDestino = nodoDestino;
        this.peso = peso;
    }

    public Nodo getNodoOrigen() {
        return nodoOrigen;
    }

    public Nodo getNodoDestino() {
        return nodoDestino;
    }

    public int getPeso() {
        return peso;
    }

    public void setNodoOrigen(Nodo nodoOrigen) {
        this.nodoOrigen = nodoOrigen;
    }

    public void setNodoDestino(Nodo nodoDestino) {
        this.nodoDestino = nodoDestino;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    @Override
    public String toString() {
        return "Arista{origen=" + nodoOrigen.getId() + ", destino=" + nodoDestino.getId() + ", peso=" + peso + "}";
    }
}
