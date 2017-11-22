
package deposito.controller;

import deposito.model.Produto;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.LinkedList;

public class DepositoController {
    
    private InetAddress grupo;
    private MulticastSocket socket;
    private String ip;
    private int porta;
    private DatagramSocket s;
    private InetAddress group;
    private byte[] buf;
    private LinkedList produtos;
    
    public DepositoController(String ip, int porta) throws IOException{
        this.ip = ip;
        this.porta = porta;
        produtos = new LinkedList();
        grupo = InetAddress.getByName(ip);
        socket = new MulticastSocket(porta);
        socket.joinGroup(grupo);
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
        
//        DatagramPacket pacote = new DatagramPacket(output.getBytes(), output.length(),
//                             grupo, porta);
//        socket.send(pacote);
//        
//        byte[] buf = new byte[1000];
//        DatagramPacket recv = new DatagramPacket(buf, buf.length);
//        socket.receive(recv);
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
