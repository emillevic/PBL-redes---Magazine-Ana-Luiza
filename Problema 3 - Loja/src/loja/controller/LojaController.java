package loja.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class LojaController {
    
    private ObjectOutputStream outcliente;
    private ObjectInputStream incliente;
    LinkedList carrinho;
    Socket socket;
    String ip;
    int porta;
    
    public LojaController(String ip, int porta) throws IOException{
        carrinho = new LinkedList();
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
    
    public boolean adicionarCarrinho(int id, int qtd) throws IOException, ClassNotFoundException{
        String add = "L#A#" + id + "#" + qtd + "#";
        
        outcliente.writeObject(add);
        
        carrinho.add(incliente.readObject());
        
        return (boolean) incliente.readObject();
    }
    
    public boolean confirmaCompra() throws IOException, ClassNotFoundException{
        String confirma = "L#C#";
        outcliente.writeObject(confirma);
        outcliente.writeObject(carrinho);
        
        return (boolean) incliente.readObject();
    }
}
