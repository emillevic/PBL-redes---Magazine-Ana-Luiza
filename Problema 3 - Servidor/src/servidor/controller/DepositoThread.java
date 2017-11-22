/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private Socket socket;
    
    public DepositoThread(ServidorController controlador, Socket socket, ObjectOutputStream outdeposito, ObjectInputStream indeposito, String recebe) throws IOException{
        this.controlador = controlador;
        this.socket = socket;
        this.outdeposito = outdeposito;
        this.indeposito = indeposito;
        this.recebe = recebe;
    }
    @Override
    public void run() {
        while(!socket.isClosed()){
            String[] aux = recebe.split("#");
            System.out.println(recebe);
            
            switch(aux[1]){
                case "A":
                    break;
                case "R":
                    break;
            }
            
            try {
                recebe = (String)indeposito.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(DepositoThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
}
