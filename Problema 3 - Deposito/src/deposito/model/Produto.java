
package deposito.model;

import java.io.Serializable;

public class Produto implements Serializable{
    private static int produtos;
    private int qtd, id;
    private String nome;
    private float peso, preco;

    public Produto(int id, int qtd, String nome, float peso, float preco) {
        produtos++;
        this.id = id;
        this.qtd = qtd;
        this.nome = nome;
        this.peso = peso;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }
}
