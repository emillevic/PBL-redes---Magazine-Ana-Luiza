
package distribuidor.controller;

import distribuidor.model.Servidor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DistribuidorController {
    private LinkedList servidores;
    private static int proximo;
    
    public DistribuidorController(){
        servidores = new LinkedList();
        proximo = 0;
    }
    
    //Esse método inicia o servidor e a thread principal
    public void iniciaServidor(int porta) throws IOException {
        ServerSocket servidor = new ServerSocket(porta);
        System.out.println("Servidor Iniciado!");

        while(true){//fica esperando uma conexão
            Socket cliente = servidor.accept();
            ThreadPrincipal tr = new ThreadPrincipal(cliente);
            Thread t = new Thread(tr);
            t.start();
        }
    }
    
    /*Este método percorre a lista de servidores até encontrar um servidor que tenha
    a localização igual à localização do paciente, quando ele encontra ele retorna
    um array de String com o endereço (ip e porta) do mesmo.*/
    public Object[] selecionaServidor(){
        int aux = servidores.size();
        while(true){
            if(aux == 0){
                Object[] address = new Object[1];
                address[0] = null;
                return address;
            }
            else if(proximo<aux){
                Servidor server = (Servidor) servidores.get(proximo);
                Object[] address = new Object[2];
                System.out.println(server.getId());
                address[0] = server.getPorta();
                address[1] = server.getIp();
                proximo++;
                return address;
            }
            else{
                proximo = 0;
            }
        }
    }
    
    private class ThreadPrincipal implements Runnable{

        private Socket s;
        private ObjectOutputStream othread;
        private ObjectInputStream ithread;
        private String recebe;

        //Recebe o socket como parâmetro e instancia o input e output
        public ThreadPrincipal(Socket socket) throws IOException{
            this.s = socket;
            othread = new ObjectOutputStream(s.getOutputStream()); 
            ithread = new ObjectInputStream(s.getInputStream());
        }

        @Override
        public void run() {
            try {
                recebe = (String) ithread.readObject();
                System.out.println(recebe);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(DistribuidorController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String[] aux = recebe.split("#");
            switch(aux[0]){
                case "L":
                    Object[] address = selecionaServidor();
                    System.out.println(address.toString());
                    try {
                        othread.writeObject(address);
                    } catch (IOException ex) {
                        Logger.getLogger(DistribuidorController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "S":
                    Servidor servidor = new Servidor(Integer.parseInt(aux[1]), aux[2]);
                    System.out.println(servidor);
                    servidores.add(servidor);
                    System.out.println(servidores);
                    break;
            }
        }
    
    }
}

