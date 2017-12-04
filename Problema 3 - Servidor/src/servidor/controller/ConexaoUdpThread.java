/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emille
 */
public class ConexaoUdpThread implements Runnable{
    private byte[] buf;
    private MulticastSocket socket;
    private ServidorController controlador;
    
    public ConexaoUdpThread(byte[] buf, MulticastSocket socket, ServidorController controlador){
        this.buf = buf;
        this.socket = socket;
        this.controlador = controlador;
    }
    @Override
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(ConexaoUdpThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            String received = new String(
              packet.getData(), 0, packet.getLength());
            System.out.println(received);
            
            DepositoThread thread = null;
            try {
                thread = new DepositoThread(controlador, socket, received);
            } catch (IOException ex) {
                Logger.getLogger(ConexaoUdpThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread t1 = new Thread(thread);
            t1.start();
        }
    }
    
}
