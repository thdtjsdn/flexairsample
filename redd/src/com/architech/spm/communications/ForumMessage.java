package com.architech.spm.communications;

import java.util.Date;

/**
 * A transfer object that represents a single message in a forum.
 * @author nate
 * @see Forum
 */
public class ForumMessage {

	/**
	 * The forum in which this message exists.
	 */
	public int forumId;
	
	/**
	 * The unique ID for this message.
	 */
	public int id;

	/**
	 * The user ID that wrote this message.
	 */
	public int userId;
	
	/**
	 * The user name that wrote this message.
	 */
	public String userName;

	/**
	 * If this message is a reply to another message, this field contains the original message ID.
	 */
	public int replyToMessageId;
	
	/**
	 * The date and time this message was created.
	 */
	public Date createDate;
	
	/**
	 * The date and time this message was updated.
	 */
	public Date updateDate;
	
	/**
	 * The main body of the message.
	 */
	public String content;
	
}
