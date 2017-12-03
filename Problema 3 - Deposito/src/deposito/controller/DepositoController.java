
package deposito.controller;

import deposito.model.Produto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private int login;
    
    public DepositoController(String ip, int porta, int portaDeposito) throws IOException{
        this.ip = ip;
        this.porta = porta;
        this.portaDeposito = portaDeposito;
        produtos = new LinkedList();
        grupo = InetAddress.getByName(ip);
        socket = new MulticastSocket(porta);
        socket.joinGroup(grupo);
        login = -1;
    }
    
    public void loginDeposito(int x, int y) throws SocketException, UnknownHostException, IOException{
        String output = "D#L#" + InetAddress.getLocalHost().getHostAddress() + "#" + portaDeposito + "#" + x + "#" + y + "#";
        s = new DatagramSocket(portaDeposito);
        group = InetAddress.getByName(ip);
        buf = output.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, porta);
        socket.send(packet);
        
        byte[] recebe = new byte[1000];
        DatagramPacket recv = new DatagramPacket(recebe, recebe.length);
        s.receive(recv);
        String data = new String(recv.getData(), 0, packet.getLength());
        String[] aux = data.split("#");
        char l = aux[1].charAt(0);
        login = Character.getNumericValue(l);
        System.out.println(login);
    }

    public boolean adicionarProduto(int id, int qtd, String nome, String peso, String preco) throws IOException, ClassNotFoundException{
        String output = "D#A#"+ login + "#" + id + "#" + nome + "#" + peso + "#" + preco + "#" + qtd + "#";
        boolean aux = false;
        Produto produto = null;
        FileOutputStream stream = new FileOutputStream("produtos.txt");
        ObjectOutputStream os = new ObjectOutputStream(stream);
        FileInputStream streami = new FileInputStream("produtos.txt");
        ObjectInputStream is = new ObjectInputStream(streami);
        
       
        for(Object o: produtos){
            produto = (Produto) o;
            if(produto.getId() == id){
                produto.setQtd(qtd);
                produto.setPeso(Float.parseFloat(peso));
                produto.setPreco(Float.parseFloat(preco));
                aux = true;
                removeArquivo(produto);
                os.writeObject(produto);
                break;
            }
        }
        if(aux == false){
            produto = new Produto(id, qtd, nome, Float.parseFloat(peso), Float.parseFloat(preco));
            produtos.add(produto);
            os.writeObject(produto);
        }
        
        s = new DatagramSocket();
        group = InetAddress.getByName(ip);
        buf = output.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, porta);
        socket.send(packet);
        
        byte[] bufrecebe = new byte[256];
        DatagramPacket pacote = new DatagramPacket(bufrecebe, bufrecebe.length);
        socket.receive(pacote);
        String recebe = new String(
        pacote.getData(), 0, pacote.getLength());
        
        return Boolean.parseBoolean(recebe);
    }
    
    public boolean removeProduto(int id, int qtd) throws IOException{
        int n = 0;
        String output = "D#R#" + login + "#" + id + "#" + qtd + "#";
        for(Object p: produtos){
            Produto prod = (Produto) p;
            if(prod.getId()==id){
                if(prod.getQtd() < qtd){
                    return false;
                }
                else if(prod.getQtd()== qtd){
                    produtos.remove(prod);
                }
                else{
                    produtos.remove(prod);
                    prod.setQtd(prod.getQtd()-qtd);
                    produtos.add(n, prod);
                } 
                break;
            }
            n++;
        }
        DatagramPacket pacote = new DatagramPacket(output.getBytes(), output.length(),
                            grupo, porta);
        socket.send(pacote);
        
        byte[] buf = new byte[1000];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        socket.receive(recv);
        
        byte[] bufrecebe = new byte[256];
        DatagramPacket pacote1 = new DatagramPacket(bufrecebe, bufrecebe.length);
        socket.receive(pacote1);
        String recebe = new String(
        pacote1.getData(), 0, pacote1.getLength());
        
        return Boolean.parseBoolean(recebe);
        
    }
    
    private void removeArquivo(Produto produto) throws FileNotFoundException, IOException, ClassNotFoundException{
        FileOutputStream stream = new FileOutputStream("produtos.txt");
        ObjectOutputStream os = new ObjectOutputStream(stream);
        FileInputStream streami = new FileInputStream("produtos.txt");
        ObjectInputStream is = new ObjectInputStream(streami);
        
        FileOutputStream stream2 = new FileOutputStream("produtos2.txt");
        ObjectOutputStream os2 = new ObjectOutputStream(stream2);
        while(is.readObject() != null){
            Produto prod = (Produto) is.readObject();
            if(!prod.equals(produto)){
                os.writeObject(produto);
            }
        }
        File produtos = new File("produtos.txt");
        produtos.delete();
        File arquivo = new File("produtos2.txt");
        arquivo.renameTo(new File("produtos.txt"));
    }
    public LinkedList getProdutos(){
        return produtos;
    } 
}
