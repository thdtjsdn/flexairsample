package com.architech.spm.dashboards;

import java.util.Date;

/**
 * A transfer object that represents a single support article. Support articles can be any type of
 * documentation that can be viewed by a user, typically from a "help" action.
 * @author nate
 *
 */
public class SupportArticle {
	
	/**
	 * The unique ID for this forum.
	 */
	public int id;
	
	/**
	 * The named application area related to this article. Articles are shown by application area, and
	 * searched by content.
	 */
	public String appArea;
	
	/**
	 * The main body of this support article.
	 */
	public String content;
	
	/**
	 * The date and time this article was created.
	 */
	public Date createDate;
	
	/**
	 * The date and time this article was updated.
	 */
	public Date updateDate;
	
}
