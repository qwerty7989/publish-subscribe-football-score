/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package publisherfootball;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 *
 * @author koony
 */
public class Main {
@Resource(mappedName = "jms/SimpleJMSTopic")
    private static Topic topic;
    @Resource(mappedName = "jms/ConnectionFactory")
    private static ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/SimpleJMSQueue")
    private static Queue queue;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connection = null;
        Destination dest = null;
        
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;  
        
        String scoreFootball = "";
        
        try {
            dest = (Destination) topic;
        } catch (Exception e) {
            System.err.println("Error setting destination: " + e.toString());
            System.exit(1);
        }

        
        try {
            connection = connectionFactory.createConnection();

            Session session = connection.createSession(
                        false,
                        Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(dest);
            TextMessage message = session.createTextMessage();
            producer.setTimeToLive((30 * 1000));  //message live is set to 30 seconds
            
            inputStreamReader = new InputStreamReader(System.in);
            bufferedReader = new BufferedReader(inputStreamReader);

            while (true) {
                try {
                    System.out.print("Enter Live Score: ");
                    scoreFootball = bufferedReader.readLine();
                    message.setText(scoreFootball);
                    producer.send(message);
                } catch (IOException e) {
                    System.err.println("I/O exception: " + e.toString());
                }
            }
            
        } catch (JMSException e) {
            System.err.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
