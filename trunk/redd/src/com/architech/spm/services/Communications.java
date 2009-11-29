package com.architech.spm.services;

import java.util.ArrayList;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebService;

import com.architech.spm.communications.Forum;
import com.architech.spm.communications.ForumMessage;
import com.architech.spm.communications.InboxMessage;

/**
 * A service class that provides methods related to different types of communications, including
 * forums and user inboxes. This service class is exposed using JAX-WS and is named
 * <code>SPM Communications</code> in the application server.
 * @author nate
 *
 */
@WebService(name="SPM Communications")
@RolesAllowed("user")
public class Communications {
	
	///////////////////////////////
	//          FORUMS           //
	///////////////////////////////
	
	/**
	 * List all available forums.
	 * @return A list of forums.
	 */
	public ArrayList<Forum> listForums() {
		ArrayList<Forum> forums = new ArrayList<Forum>();
		return forums;
	}
	
	/**
	 * List all messages on this forum.
	 * @param forumId
	 * @return A list of messages posted to this forum.
	 */
	public ArrayList<ForumMessage> listForumMessages(int forumId) {
		ArrayList<ForumMessage> messages = new ArrayList<ForumMessage>();
		return messages;
	}
	
	/**
	 * Create a new forum message or save changes to an existing forum message.
	 * @param message The new message
	 * @return The ID of the new (or existing) forum message.
	 */
	public int saveForumMessage(ForumMessage message) {
		return message.id;
	}
	

	///////////////////////////////
	//           INBOX           //
	///////////////////////////////
	
	
	/**
	 * List all the messages for the current user.
	 * @return A list of inbox messages.
	 */
	public ArrayList<InboxMessage> listInbox() {
		// TODO: pull user id from security/auth context
		ArrayList<InboxMessage> messages = new ArrayList<InboxMessage>();
		return messages;
	}
	
	/**
	 * Create a new inbox message for another user. (Note while most other CRUD-type services
	 * contain a save[Type] function that both creates or updates, this method only creates new
	 * records - updates are not possible.)
	 * @param message
	 * @return The ID of the new inbox message.
	 */
	public int sendMessage(InboxMessage message) {
		return message.id;
	}
	
	/**
	 * Delete an existing inbox message.
	 * @param id
	 * @return True, if deletion was successful, otherwise false.
	 */
	public boolean deleteMessage(int id) {
		return true;
	}
	
}
