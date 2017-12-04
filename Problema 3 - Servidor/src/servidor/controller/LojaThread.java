
package servidor.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.model.Deposito;

public class LojaThread implements Runnable{
    private ServidorController controlador;
    private ObjectOutputStream outloja;
    private ObjectInputStream inloja;
    private String recebe;
    private Socket socket;
    private byte[] bufrecebe, bufenvia;
    private Deposito deposito;
    

    public LojaThread(ServidorController controlador, Socket socket) throws IOException{
        this.deposito = null;
        this.controlador = controlador;
        this.socket = socket;
        this.outloja = new ObjectOutputStream(socket.getOutputStream());
        this.inloja = new ObjectInputStream(socket.getInputStream());
    }
    @Override
    public void run() {
        try {
            while(!socket.isClosed()){

                recebe = (String) inloja.readObject();
                String[] aux = recebe.split("#");
                System.out.println(recebe);
                switch(aux[1]){
                    case "L":
                        boolean confirma = controlador.loginLoja(aux[2], aux[3]);
                        outloja.writeObject(confirma);
                        break;
                    case "R":
                        outloja.writeObject(controlador.retornaProdutos());
                        break;
                    case "F":
                        LinkedList carrinho = (LinkedList) inloja.readObject();
                        float frete = controlador.calculaFrete(Integer.parseInt(aux[2]), Integer.parseInt(aux[3]), carrinho);
                        outloja.writeObject(frete);
                        break;
                    case "C":
                        LinkedList produtos = (LinkedList) inloja.readObject();
                        boolean compra = false;
                        if(deposito != null){
                            compra = controlador.efetuaCompra(deposito, produtos);
                        }
                        outloja.writeObject(compra);
                        break;
                }
            }
        }catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(LojaThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
