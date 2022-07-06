package set.sockets;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Servidor {
 
 public static void main(String[] args) {

  MarcoServidor mimarco = new MarcoServidor();

  mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
 }
}

class MarcoServidor extends JFrame implements Runnable {

 public MarcoServidor() {
  
  setBounds(1200, 300, 280, 350);
  
  JPanel milamina = new JPanel();
  
  milamina.setLayout(new BorderLayout());
  
  areatexto = new JTextArea();
  
  milamina.add(areatexto, BorderLayout.CENTER);
  
  add(milamina);
  
  setVisible(true);

  Thread miHilo = new Thread(this);

  miHilo.start();
 
 }

 private JTextArea areatexto;

 @Override
 public void run() {
  // TODO Auto-generated method stub
  // System.out.println("Estoy a la escucha");
  try {
   ServerSocket servidor = new ServerSocket(9999);

   String nick, ip, mensaje;

   ArrayList <String>  listaIp = new ArrayList<String>();

   PaqueteEnvio paquete_recibido;

   while (true) {

    Socket miSocket = servidor.accept();

    ObjectInputStream paquete_datos = new ObjectInputStream(miSocket.getInputStream());

    paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();

    nick = paquete_recibido.getNick();

    ip = paquete_recibido.getIp();

    mensaje = paquete_recibido.getMensaje();

    /*DataInputStream flujo_entrada = new DataInputStream(miSocket.getInputStream());
    
    String mensaje_texto = flujo_entrada.readUTF();
    
    areatexto.append("\n" + mensaje_texto);*/

    if (!mensaje.equals(" online")) {
     
     
     areatexto.append("\n" + nick + ":" + mensaje + " para " + ip);
     
     //Creo un puente de comunicacion por donde fluiran los datos
     Socket enviaDestinatario = new Socket(ip, 9090);
     //Crear en el servidor un objeto objectoutputstream para poder enviar el paquete a traves del socket
     ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
     //se envia el paquete que se recibio
     paqueteReenvio.writeObject(paquete_recibido);
     //Se cierra el flujo de datos
     paqueteReenvio.close();
     //una vez que el paquete llega a su destino se cierra el socket
     enviaDestinatario.close();
     
     miSocket.close();
     
    } else {
     //---------------- Detecta Online----------------------

     InetAddress localizacion = miSocket.getInetAddress();

     String ipRemota = localizacion.getHostAddress();

     System.out.println("Online: " + ipRemota);

     listaIp.add(ipRemota);

     for (String z : listaIp) {
      System.out.println("Array: " + z);
     }

     //----------------------------------------------------
    }
   }

  } catch (IOException | ClassNotFoundException e) {
   //TODO: handle exception
   e.printStackTrace();
  }
 }
}