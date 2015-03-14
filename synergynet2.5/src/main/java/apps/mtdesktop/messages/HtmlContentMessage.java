package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class HtmlContentMessage extends ContentMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2441838213774752568L;

	private String html;
	
	public HtmlContentMessage(){
		super();
	}
	
	public HtmlContentMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId, String html){
		super(targetClass, targetTableId, contentId);
		this.setContentId(contentId);
		this.setHtmlContent(html);
	}
	
	public void setHtmlContent(String html){
		this.html = html;
	}
	
	public String getHtmlContent(){
		return html;
	}
}
