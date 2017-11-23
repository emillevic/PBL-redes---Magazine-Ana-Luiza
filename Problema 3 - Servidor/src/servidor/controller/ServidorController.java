
package servidor.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import servidor.model.Deposito;
import servidor.model.Produto;

public class ServidorController {
    
    private String ipMulticast, ipBalanceador;
    private int portaMulticast, portaBalanceador, portaLoja;
    private boolean conectado;
    private MulticastSocket socket;
    private byte[] buf;
    private LinkedList depositos;
    private Socket socketBalanceador;
    private ObjectOutputStream outserver;
    private ObjectInputStream inserver;
    
 
    
    public ServidorController(String ipMulticast, int portaMulticast, String ipBalanceador, int portaBalanceador, int portaLoja) throws IOException{
        this.ipMulticast = ipMulticast;
        this.portaMulticast = portaMulticast;
        this.ipBalanceador = ipBalanceador;
        this.portaBalanceador = portaBalanceador; 
        this.conectado = false;
        depositos = new LinkedList();
        this.portaLoja = portaLoja;
        this.socket = null;
        socketBalanceador = new Socket(ipBalanceador, portaBalanceador);
        outserver = new ObjectOutputStream(socketBalanceador.getOutputStream()); 
        inserver = new ObjectInputStream(socketBalanceador.getInputStream());
        this.buf = new byte[256];
    }
    
    public void conexaoMulticast(int porta) throws IOException{
        socket = new MulticastSocket(portaMulticast);
        InetAddress group = InetAddress.getByName(ipMulticast);
        socket.joinGroup(group);
        outserver.writeObject("S#" + portaLoja + "#" + InetAddress.getLocalHost().getHostAddress() + "#");
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
              packet.getData(), 0, packet.getLength());
            System.out.println(received);
            if(received.startsWith("D")){
                LojaThread thread = new LojaThread(this, socket, received);
            }
        }
    }
    
    public void conexaoLoja(int porta) throws IOException, ClassNotFoundException{
        ServerSocket servidor = new ServerSocket(porta);
        System.out.println("Servidor Iniciado!");

        while(true){//fica esperando uma conexão
            Socket cliente = servidor.accept();
            ThreadPrincipal tr = new ThreadPrincipal(this, cliente);
            Thread t = new Thread(tr);
            t.start();
        }
    }
    
    public int loginDeposito(int x, int y){
        Deposito deposito = new Deposito(x, y);
        depositos.add(deposito);
        return deposito.getId();
    }
    
    public void adicionaProdutoDeposito(Produto produto, int x, int y){
        for(Object o: depositos){
            Deposito deposito = (Deposito) o;
            if(deposito.getX() == x && deposito.getY() == y){
                deposito.addProduto(produto);
            }
        }
    }
    
    public void removeProdutoDeposito(Produto produto, int x, int y){
        for(Object o: depositos){
            Deposito deposito = (Deposito) o;
            if(deposito.getX() == x && deposito.getY() == y){
                deposito.removeProduto(produto);
            }
        }
    }
    
    public boolean loginLoja(String login, String senha) throws FileNotFoundException, IOException{

        /*  Comando L(Login) para fazer o login do paciente, verifica
         *  se ja existe uma linha iniciando com o nome e terminando
            com a senha no arquivo ja existente para fazer o login.
         */
        FileInputStream stream = new FileInputStream("usuarios.txt");
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);

        //Percorre o arquivo procurando o login
        String linha;
        linha = br.readLine();

        while(linha!=null){
            System.out.println(linha);
            if(linha.startsWith(login) && linha.endsWith(senha)){
                //Caso ache uma linha que comece com o mesmo nome e acabe com a senha
                //ele envia uma mensagem de logado e sai do loop
                return true;
            }
            linha = br.readLine();
        }
        //Caso não ache nenhum login, envia uma mensagem de usuario inexistente.
        return false;
    }
    
    public Integer[] depositoPerto(int x, int y){
        LinkedList depositos = new LinkedList();
        int xd = 0, yd = 0;
        double dist = Double.MAX_VALUE;
        for(Object obj: depositos){
            Deposito deposito = (Deposito) obj;
            int xdeposito = deposito.getX();
            int ydeposito = deposito.getY();
            if(Math.sqrt((x-xdeposito)^2+(y-ydeposito)^2) <= dist){
                xd = xdeposito;
                yd = ydeposito;
            }
        }
        Integer[] coordenada = new Integer[2];
        coordenada[0] = xd;
        coordenada[1] = yd;
        return coordenada;
    }

    public boolean efetuaCompra(Deposito deposito, LinkedList produtos){
        if(possuiProdutos(deposito, produtos) == true){
            LinkedList produtosDeposito = deposito.getProdutos();
            for(Object obj: produtos){
                Produto produto = (Produto) obj;
                produtosDeposito.remove(produto);
            }
            return true;
        }
        return false;
    }
    
    private boolean possuiProdutos(Deposito deposito, LinkedList produtos){
        LinkedList produtosDeposito = deposito.getProdutos();
        for(Object obj: produtos){
            Produto produto = (Produto) obj;
            for(Object obj1: produtosDeposito){
                Produto produto1 = (Produto) obj1;
                if(produto.getId() == produto1.getId() && produto.getQtd() <= produto1.getQtd()){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean conexao(){
        return conectado;
    }
}
