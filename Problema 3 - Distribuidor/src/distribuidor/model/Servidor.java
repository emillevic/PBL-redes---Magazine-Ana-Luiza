
package distribuidor.model;

public class Servidor {
    private static int servidores;
    private int id, qtd;
    private Integer porta;
    private String ip;

    public Servidor(int porta, String ip) {
        servidores++;
        this.id = servidores;
        this.qtd = 0;
        this.porta = porta;
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    
}