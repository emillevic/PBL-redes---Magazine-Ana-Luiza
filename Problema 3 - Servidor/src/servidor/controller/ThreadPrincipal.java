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
public class ThreadPrincipal implements Runnable{
    private Socket socket;
    private ObjectOutputStream outthread;
    private ObjectInputStream inthread;
    private ServidorController controlador;
    
    public ThreadPrincipal(ServidorController controlador, Socket socket) throws IOException{
        this.socket = socket;
        outthread = new ObjectOutputStream(socket.getOutputStream()); 
        inthread = new ObjectInputStream(socket.getInputStream());
        this.controlador = controlador;
    }

    @Override
    public void run() {
        
            try {
                /* Recebe a string de comunicação do cliente e verifica
                 * se inicia com SENSOR ou MEDICO para thread desejada
                 * ser criada.*/
                String recebe = (String) inthread.readObject();

                String[] aux = recebe.split("#");

                System.out.println(aux);
                switch(aux[0]){
                    /* Caso a requisição tenha sido enviada do sensor, ele
                     * cria uma thread do sensor.*/
                    case "D":
                        System.out.println("Conexão Deposito");
                        DepositoThread depositoThread = new DepositoThread(controlador, socket, outthread, inthread, recebe);
                        Thread t1 = new Thread(depositoThread);
                        t1.start();
                    break;
                     /* Caso a requisição tenha sido enviada do medico, ele
                      * cria uma thread do medico.*/
                    case "L":
                        System.out.println("Conexão Loja");
                        LojaThread lojaThread = new LojaThread(controlador, socket, outthread, inthread, recebe);
                        Thread t2 = new Thread(lojaThread);
                        t2.start();
                    break;
                }
            }
            catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ThreadPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
    
}
