
package deposito.controller;

import deposito.model.Produto;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class DepositoController {
    
    private InetAddress grupo;
    private MulticastSocket socket;
    private String ip;
    private int porta, portaDeposito;
    private DatagramSocket s;
    private InetAddress group;
    private byte[] buf;
    private LinkedList produtos;
    private String login;
    
    public DepositoController(String ip, int porta, int portaDeposito) throws IOException{
        this.ip = ip;
        this.porta = porta;
        this.portaDeposito = portaDeposito;
        produtos = new LinkedList();
        grupo = InetAddress.getByName(ip);
        socket = new MulticastSocket(porta);
        socket.joinGroup(grupo);
        login = null;
    }
    
    public void loginDeposito() throws SocketException, UnknownHostException, IOException{
        String output = "D#L#" + InetAddress.getLocalHost().getHostAddress() + "#" + portaDeposito + "#";
        s = new DatagramSocket();
        group = InetAddress.getByName(ip);
        buf = output.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, porta);
        socket.send(packet);
        
        byte[] recebe = new byte[1000];
        DatagramPacket recv = new DatagramPacket(recebe, recebe.length);
        socket.receive(recv);
        String data = new String(recv.getData());
        login = data;
    }

    public void adicionarProduto(int qtd, String nome, String peso, String preco) throws IOException{
        String output = "D#A#" + qtd + "#" + nome + "#" + peso + "#" + preco + "#";
        Produto produto = new Produto(qtd, nome, Float.parseFloat(peso), Float.parseFloat(preco));
        produtos.add(produto);
        s = new DatagramSocket();
        group = InetAddress.getByName(ip);
        buf = output.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, porta);
        socket.send(packet);
        
        
        
    }
    
    public void removeProduto(int id) throws IOException{
        String output = "D#R#" + id + "#";
        for(Object p: produtos){
            Produto prod = (Produto) p;
            if(prod.getId()==id){
                produtos.remove(prod);
                break;
            }
        }
        DatagramPacket pacote = new DatagramPacket(output.getBytes(), output.length(),
                             grupo, porta);
        socket.send(pacote);
        
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        socket.receive(recv);
    }
    
    public LinkedList getProdutos(){
        return produtos;
    }
    
    
}
