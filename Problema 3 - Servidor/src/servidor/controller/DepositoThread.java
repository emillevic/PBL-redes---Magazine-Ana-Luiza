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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cliente
 */
public class DepositoThread implements Runnable{
    private ServidorController controlador;
    private ObjectOutputStream outdeposito;
    private ObjectInputStream indeposito;
    private String recebe;
    private InetAddress ip;
    private MulticastSocket socket;
    private byte[] bufrecebe, bufenvia;
    private int porta;
    
    
    public DepositoThread(ServidorController controlador, MulticastSocket socket, ObjectOutputStream outdeposito, ObjectInputStream indeposito, String recebe) throws IOException{
        this.controlador = controlador;
        this.socket = socket;
        this.outdeposito = outdeposito;
        this.indeposito = indeposito;
        this.recebe = recebe;
        bufrecebe = new byte[256];
    }
    @Override
    public void run() {
        while(!socket.isClosed()){
            try{
                String[] aux = recebe.split("#");
                System.out.println(recebe);

                switch(aux[1]){
                    case "L":
                        int id = controlador.loginDeposito(Integer.parseInt(aux[2]), Integer.parseInt(aux[3]));
                        ip = InetAddress.getByName(aux[2]);
                        porta = Integer.parseInt(aux[3]);
                        bufenvia = Integer.toString(id).getBytes();
                        DatagramPacket packet = new DatagramPacket(bufenvia, bufenvia.length, ip, porta);
                        socket.send(packet);
                        break;
                    case "A":
                        break;
                    case "R":
                        break;
                }
                DatagramPacket packet = new DatagramPacket(bufrecebe, bufrecebe.length);
                    socket.receive(packet);
                    recebe = (String)indeposito.readObject();

            } catch(IOException | ClassNotFoundException ex){
                Logger.getLogger(DepositoThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
