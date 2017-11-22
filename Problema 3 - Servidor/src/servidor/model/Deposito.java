
package servidor.model;

import java.util.LinkedList;

public class Deposito {
    private LinkedList produtos;
    private int x, y;
    private double distancia;

    public Deposito(LinkedList produtos, int x, int y) {
        this.produtos = produtos;
        this.x = x;
        this.y = y;
        distancia = 0;
    }

    public LinkedList getProdutos() {
        return produtos;
    }

    public void setProdutos(LinkedList produtos) {
        this.produtos = produtos;
    }
    
    public void addProduto(Produto produto){
        produtos.add(produto);
    }
    
    public void removeProduto(Produto produto){
        produtos.remove(produto);
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }
    
    
}
