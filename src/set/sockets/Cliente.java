package set.sockets;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;

public class Cliente {
 public static void main(String[] args){
  MarcoCliente mimarco = new MarcoCliente();
  
  mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }
}

/**
 * MarcoCliente extends JFrame
 */
class MarcoCliente extends JFrame {

 public MarcoCliente() {

  setBounds(600, 300, 280, 350);

  LaminaMarcoCliente milamina = new LaminaMarcoCliente();

  add(milamina);

  setVisible(true);

  addWindowListener(new EnvioOnline());

 }
}

class EnvioOnline extends WindowAdapter {
 public void windowOpened(WindowEvent e) {
  try {
   System.out.println("entro a envio online");

   Socket miSocket = new Socket("192.168.1.164", 9999);

   PaqueteEnvio datos = new PaqueteEnvio();

   datos.setMensaje(" online");

   ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());

   paquete_datos.writeObject(datos);

   miSocket.close();

  } catch (Exception e2) {
   //TODO: handle exception
  }
 }
}

/**
 * LaminaMarcoCliente extends JPanel
 */
class LaminaMarcoCliente extends JPanel implements Runnable {
 
 public LaminaMarcoCliente() {

  String nick_usuario = JOptionPane.showInputDialog("Nick: ");

  JLabel n_nick = new JLabel("Nick: ");
 
  add(n_nick);
  
  nick = new JLabel();

  nick.setText(nick_usuario);

  add(nick);
  
  JLabel texto = new JLabel("Online: ");

  add(texto);

  ip = new JComboBox();

  add(ip);

  campochat = new JTextArea(12, 20);

  add(campochat);

  campo1 = new JTextField(20);

  add(campo1);

  miboton = new JButton("Enviar");

  EnviaTexto mievento = new EnviaTexto();

  miboton.addActionListener(mievento);

  add(miboton);

  Thread miHilo = new Thread(this);

  miHilo.start();

 }

 private class EnviaTexto implements ActionListener {
  public void actionPerformed(ActionEvent e) {
   // System.out.println("Hola evento");

   campochat.append("\n"+"yo: "+campo1.getText());

   try {
    //Se abre el socket
    Socket miSocket = new Socket("192.168.1.64", 9999);
    //Se crea un paquete de datos
    PaqueteEnvio datos = new PaqueteEnvio();

    datos.setNick(nick.getText());

    datos.setIp(ip.getSelectedItem().toString());

    datos.setMensaje(campo1.getText());
    //Se crea el flujo para poder enviar el paquete al destinatario
    ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());

    paquete_datos.writeObject(datos);

    miSocket.close();

    /*DataOutputStream flujo_salida = new DataOutputStream(miSocket.getOutputStream());

    flujo_salida.writeUTF(campo1.getText());
    
    flujo_salida.close();*/

   } catch (UnknownHostException e1) {
    e1.printStackTrace();
   } catch (IOException e1) {
    System.out.println(e1.getMessage());
   }
  }
 }
 
 private JTextField campo1;

 private JComboBox ip;

 private JLabel nick;
 
 private JTextArea campochat;

 private JButton miboton;

 @Override
 public void run() {
  // TODO Auto-generated method stub
  try {

   ServerSocket recibe_destinatario = new ServerSocket(9090);

   Socket cliente;

   PaqueteEnvio paqueteRecibido;

   while (true) {
    
    cliente = recibe_destinatario.accept();

    ObjectInputStream datosEntrada = new ObjectInputStream(cliente.getInputStream());

    paqueteRecibido = (PaqueteEnvio) datosEntrada.readObject();

    if (!paqueteRecibido.getMensaje().equals(" online")) {
     campochat.append("\n" + paqueteRecibido.getNick() + ":" + paqueteRecibido.getMensaje());
    } else {
     // campochat.append("\n"+paqueteRecibido.getips());
     ArrayList<String> IpsMenu = new ArrayList<String>();

     IpsMenu = paqueteRecibido.getips();

     ip.removeAllItems();

     for (String z : IpsMenu) {
      ip.addItem(z);
     }
    }
   }
   
  } catch (Exception e) {
   //TODO: handle exception

   System.out.println(e.getMessage());
  }
 }

}

class PaqueteEnvio implements Serializable {
 
 private String nick, ip, mensaje;

 private ArrayList<String> ips;

 public ArrayList<String> getips() {
  return ips;
 }

 public void setips(ArrayList<String> ips) {
  this.ips = ips;
 }

 public String getNick() {
  return nick;
 }

 public void setNick( String nick) {
  this.nick = nick;
 }

 public String getIp() {
  return ip;
 }

 public void setIp(String ip) {
  this.ip = ip;
 }

 public String getMensaje() {
  return mensaje;
 }

 public void setMensaje(String mensaje) {
  this.mensaje = mensaje;
 }

}