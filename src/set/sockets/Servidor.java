package set.sockets;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.net.*;

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

   while (true) {

    Socket miSocket = servidor.accept();

    DataInputStream flujo_entrada = new DataInputStream(miSocket.getInputStream());

    String mensaje_texto = flujo_entrada.readUTF();

    areatexto.append("\n" + mensaje_texto);

    miSocket.close();

   }

  } catch (IOException e) {
   //TODO: handle exception
   e.printStackTrace();
  }
 }
}