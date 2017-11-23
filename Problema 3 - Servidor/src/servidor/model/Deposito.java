
package servidor.model;

import java.util.LinkedList;

public class Deposito {
    private static int depositos;
    private LinkedList produtos;
    private int x, y, id;
    private double distancia;

    public Deposito(int x, int y) {
        depositos++;
        this.id = depositos;
        this.produtos = null;
        this.x = x;
        this.y = y;
        distancia = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
