package com.architech.spm.communications;

import java.util.Date;

/**
 * A transfer object that represents a single message in an inbox.
 * @author nate
 *
 */
public class InboxMessage {

	/**
	 * The unique ID for this message.
	 */
	public int id;

	/**
	 * The user ID who is the recipient of this message.
	 */
	public int userId;
	
	/**
	 * The user ID that wrote this message.
	 */
	public int fromUserId;
	
	/**
	 * The user name that wrote this message.
	 */
	public String fromUserName;
	
	/**
	 * The date and time this message was sent.
	 */
	public Date sentDate;
	
	/**
	 * The subject of the message.
	 */
	public String subject;
	
	/**
	 * A flag indicating if this message has been read.
	 */
	public boolean isRead;
	
	/**
	 * The main body of the message.
	 */
	public String content;
	
}
