/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.model.Produto;

/**
 *
 * @author Cliente
 */
public class DepositoThread implements Runnable{
    private ServidorController controlador;
    private String recebe;
    private InetAddress ip;
    private MulticastSocket socket;
    private byte[] bufrecebe, bufenvia;
    private int porta, id;
    
    
    public DepositoThread(ServidorController controlador, MulticastSocket socket) throws IOException{
        this.controlador = controlador;
        this.socket = socket;
        bufrecebe = new byte[256];
    }
    @Override
    public void run() {
            try{
                DatagramPacket packet = new DatagramPacket(bufrecebe, bufrecebe.length);
                socket.receive(packet);
                recebe = new String(
                packet.getData(), 0, packet.getLength());
                    
                String[] aux = recebe.split("#");
                System.out.println(recebe);

                switch(aux[1]){
                    case "L":
                        int id = controlador.loginDeposito(Integer.parseInt(aux[4]), Integer.parseInt(aux[5]));
                        ip = InetAddress.getByName(aux[2]);
                        porta = Integer.parseInt(aux[3]);
                        
                        bufenvia = Integer.toString(id).getBytes();
                        DatagramPacket pacote = new DatagramPacket(bufenvia, bufenvia.length, ip, porta);
                        socket.send(pacote);
                        break;
                    case "A":
                        Produto produto = new Produto(Integer.parseInt(aux[3]), aux[4], Float.parseFloat(aux[5]), Float.parseFloat(aux[6]));
                        
                        boolean confirma = controlador.adicionaProdutoDeposito(produto, Integer.parseInt(aux[2]));
                        
                        bufenvia = Boolean.toString(confirma).getBytes();
                        DatagramPacket pacote1 = new DatagramPacket(bufenvia, bufenvia.length, ip, porta);
                        socket.send(pacote1);
                        break;
                    case "R":
                        LinkedList produtosDeposito = controlador.retornaProdutosDeposito(Integer.parseInt(aux[2]));
                        Produto produto1 = null;
                        for(Object o: produtosDeposito){
                            produto1 = (Produto) o;
                            if(produto1.getId() == Integer.parseInt(aux[3]));
                        }
                        boolean confirmar = controlador.removeProdutoDeposito(produto1, Integer.parseInt(aux[2]));
                        
                        bufenvia = Boolean.toString(confirmar).getBytes();
                        DatagramPacket pacote2 = new DatagramPacket(bufenvia, bufenvia.length, ip, porta);
                        socket.send(pacote2);
                        break;
                }


            } catch(IOException ex){
                Logger.getLogger(DepositoThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
