
package servidor.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LojaThread implements Runnable{
    ServidorController controlador;
    private ObjectOutputStream outloja;
    private ObjectInputStream inloja;
    String recebe;
    Socket socket;

    public LojaThread(ServidorController controlador, Socket socket, ObjectOutputStream outloja, ObjectInputStream inloja, String recebe) throws IOException{
        this.controlador = controlador;
        this.socket = socket;
        this.outloja = outloja;
        this.inloja = inloja;
        this.recebe = recebe;
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
        }
    }
    
}
