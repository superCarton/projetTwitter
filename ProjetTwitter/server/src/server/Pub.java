package server;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.*; 
import org.apache.activemq.ActiveMQConnectionFactory;
import java.util.*;

public class Pub{

  private javax.jms.Connection connect = null;
  private javax.jms.Session sendSession = null;
  private Map<String, MessageProducer> sender = new HashMap<String, MessageProducer>();
  private javax.jms.Queue queue = null;
  InitialContext context = null;
  public Pub(){

    try
    {	// Create a connection

      Hashtable properties = new Hashtable();
      properties.put(Context.INITIAL_CONTEXT_FACTORY, 
          "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
      properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

      context = new InitialContext(properties);

      javax.jms.ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
      connect = factory.createConnection();
      sendSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

      //connect.start(); // on peut activer la connection. 
    } catch (javax.jms.JMSException jmse){
      jmse.printStackTrace();
    } catch (NamingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void publier(String tag, String message) throws JMSException{

    MessageProducer newsender = sender.get(tag);
    if(newsender == null){
      try{
        Topic topic = (Topic) context.lookup("dynamicTopics/"+tag);

        newsender = sendSession.createProducer(topic);
      }catch(NamingException e){
        System.out.println(e.getMessage());
      }
      sender.put(tag, newsender);
    }
    //Fabriquer un message
    MapMessage mess = sendSession.createMapMessage();
    mess.setString("message",message);
    //Poster ce message dans la queue
    newsender.send(mess); // equivaut a publier dans le topic
  }

}
