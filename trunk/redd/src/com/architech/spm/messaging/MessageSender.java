package com.architech.spm.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

public class MessageSender {
	
	private ConnectionFactory topicFactory;
	private Topic topic;
	
	public MessageSender() {
	}
	
	public void sendMessage() {
		 Connection topicConnection = null;
		 Session session = null;
		 MapMessage message = null;
		 MessageProducer producer = null;

		 try {
			 topicConnection = topicFactory.createConnection();
			 session = topicConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			 producer = session.createProducer(topic);
			 message = session.createMapMessage();
			 
			 message.setString("lastname", "Smith");
			 message.setString("firstname", "John");
			 message.setString("id", "0100");
	
			 producer.send(message);

			 session.close();
			 topicConnection.close();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
}
