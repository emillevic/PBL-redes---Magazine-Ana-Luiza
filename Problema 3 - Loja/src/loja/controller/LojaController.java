package loja.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import loja.model.Produto;

public class LojaController {
    
    private ObjectOutputStream outcliente;
    private ObjectInputStream incliente;
    private LinkedList carrinho, produtos;
    private Socket socket;
    private String ip;
    private int porta;
    
    public LojaController(String ip, int porta) throws IOException{
        carrinho = new LinkedList();
        produtos = null;
        socket = null;
        this.ip = ip;
        this.porta = porta;
    }
    
    public boolean instanciaSocket() throws IOException{
        socket = new Socket(ip, porta);
        outcliente = new ObjectOutputStream(socket.getOutputStream()); 
        incliente = new ObjectInputStream(socket.getInputStream());
        return socket.isConnected();
    }
    
    public Object[] conexaoDistribuidor() throws IOException, ClassNotFoundException{
        outcliente.writeObject("L#CON#");
        Object[] address = (Object[]) incliente.readObject();
        socket.close();
        return address;
    }
    
    public boolean loginCliente(String nick, String senha) throws IOException, ClassNotFoundException{
        String login = "L#L#" + nick + "#" + senha + "#";
        
        outcliente.writeObject(login);
        
        String address[] = (String[]) incliente.readObject();
        
        return (boolean) incliente.readObject();
    }
    
    public LinkedList retornaProdutos() throws IOException, ClassNotFoundException{
        String output = "L#R#";
        outcliente.writeObject(output);
        produtos = (LinkedList) incliente.readObject();
        return produtos;
    }
    
    public LinkedList retornaProdutosCarrinho(){
        return carrinho;
    }
    
    public boolean adicionarCarrinho(String nome) throws IOException, ClassNotFoundException{
        boolean aux = false;
        for(Object o: produtos){
            Produto produto = (Produto) o;
            if(produto.getNome().equals(nome)){
                carrinho.add(produto);
                aux = true;
                break;
            }
        }
        //String add = "L#A#" + nome + "#" + qtd + "#";
        
        //outcliente.writeObject(add);
        
        //carrinho.add(incliente.readObject());
        
        return aux;
    }
    
    public boolean calculaFrete(int x, int y) throws IOException, ClassNotFoundException{
        String frete = "L#F#" + x + "#" + y + "#";
        outcliente.writeObject(frete);
        outcliente.writeObject(carrinho);
        return (boolean) incliente.readObject();
    }
    
    public boolean confirmaCompra() throws IOException, ClassNotFoundException{
        String confirma = "L#C#";
        outcliente.writeObject(confirma);
        outcliente.writeObject(carrinho);
        
        return (boolean) incliente.readObject();
    }
}
