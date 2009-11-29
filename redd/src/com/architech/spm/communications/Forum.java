package com.architech.spm.communications;

import java.util.Date;

/**
 * A transfer object that represents a single forum in the communications area.
 * @author nate
 * @see ForumMessage
 */
public class Forum {

	/**
	 * The unique ID for this forum.
	 */
	public int id;
	
	/**
	 * The user ID that created this forum.
	 */
	public int createdByUserId;
	
	/**
	 * The user name that created this forum.
	 */
	public String createByUserName;
	
	/**
	 * The date and time this forum was created.
	 */
	public Date createDate;
	
	/**
	 * The name of the forum.
	 */
	public String name;
	
	/**
	 * A short description (1-2 sentences) of the purpose of this forum.
	 */
	public String description;
	
}
