package servidor.model;

import java.util.LinkedList;

public class Cliente {
    private String login, senha;
    private LinkedList carrinho;

    public Cliente(String login, String senha, LinkedList carrinho) {
        this.login = login;
        this.senha = senha;
        this.carrinho = carrinho;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LinkedList getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(LinkedList carrinho) {
        this.carrinho = carrinho;
    }
    
    public void addProduto(Produto produto){
        carrinho.add(produto);
    }
    
    public void removeProduto(Produto produto){
        carrinho.remove(produto);
    }
}
