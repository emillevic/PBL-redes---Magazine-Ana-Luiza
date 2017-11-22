
package distribuidor.view;

import distribuidor.controller.DistribuidorController;
import distribuidor.util.Console;
import java.io.IOException;

public class DistribuidorView {
    public static void main(String[] args) throws IOException {
        System.out.println("Distribuidor");
        System.out.println("Digite a porta:");
        int porta = Console.readInt();

        
        DistribuidorController controlador = new DistribuidorController();
        controlador.iniciaServidor(porta);
    }
}
