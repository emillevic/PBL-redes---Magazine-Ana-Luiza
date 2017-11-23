
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class LojaThread implements Runnable{
    ServidorController controlador;
    private ObjectOutputStream outloja;
    private ObjectInputStream inloja;
    String recebe;
    MulticastSocket socket;
    private byte[] bufrecebe, bufenvia;
    

    public LojaThread(ServidorController controlador, MulticastSocket socket, String recebe) throws IOException{
        this.controlador = controlador;
        this.socket = socket;
        this.outloja = outloja;
        this.inloja = inloja;
        this.recebe = recebe;
        bufrecebe = new byte[256];
    }
    @Override
    public void run() {
        
        while(!socket.isClosed()){
            String[] aux = recebe.split("#");
            System.out.println(recebe);
            switch(aux[1]){
                case "L":
                    break;
                case "A":
                    break;
                case "C":
                    break;
            }
            DatagramPacket packet = new DatagramPacket(bufrecebe, bufrecebe.length);
            try {
                socket.receive(packet);
            } catch (IOException ex) {
                Logger.getLogger(LojaThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            String received = new String(
              packet.getData(), 0, packet.getLength());
            System.out.println(received);
            recebe = received;
        }
    }
    
}
