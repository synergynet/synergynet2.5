package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class HtmlContentMessage.
 */
public class HtmlContentMessage extends ContentMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2441838213774752568L;

	/** The html. */
	private String html;
	
	/**
	 * Instantiates a new html content message.
	 */
	public HtmlContentMessage(){
		super();
	}
	
	/**
	 * Instantiates a new html content message.
	 *
	 * @param targetClass the target class
	 * @param targetTableId the target table id
	 * @param contentId the content id
	 * @param html the html
	 */
	public HtmlContentMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId, String html){
		super(targetClass, targetTableId, contentId);
		this.setContentId(contentId);
		this.setHtmlContent(html);
	}
	
	/**
	 * Sets the html content.
	 *
	 * @param html the new html content
	 */
	public void setHtmlContent(String html){
		this.html = html;
	}
	
	/**
	 * Gets the html content.
	 *
	 * @return the html content
	 */
	public String getHtmlContent(){
		return html;
	}
}
