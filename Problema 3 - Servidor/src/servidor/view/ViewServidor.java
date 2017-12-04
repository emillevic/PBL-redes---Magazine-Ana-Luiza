
package servidor.view;

import java.io.IOException;
import servidor.controller.ServidorController;
import servidor.util.Console;

public class ViewServidor {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Distribuidor");
        System.out.println("Digite o ip:");
        String ip = Console.readString();
        
        System.out.println("Digite a porta:");
        int porta = Console.readInt();
        
        System.out.println("Multicast");
        System.out.println("Digite o ip:");
        String ipMulticast = Console.readString();
        
        System.out.println("Digite a porta:");
        int portaMulticast = Console.readInt();
        
        System.out.println("Servidor");
        System.out.println("Digite a porta:");
        int portaLoja = Console.readInt();
        
        ServidorController controlador = new ServidorController(ipMulticast, portaMulticast, ip, porta, portaLoja);
        
        controlador.conexaoMulticast(portaMulticast);
        controlador.conexaoLoja(portaLoja);
        
    }
}
