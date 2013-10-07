import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Abstract class for a WebResponse. Stores the HtmlUtil HtmlPage 
 * for use by subclasses. 
 * 
 * @author Johnathon Ender
 *
 */
public abstract class WebResponse 
{
	private HtmlPage page;
	
	/**
	 * Returns the HtmlPage for this WebResponse
	 *
	 *@return The HtmlPage object
	 */
	public HtmlPage getPage()
	{
		return page;
	}
	
	/**
	 *Sets the HtmlPage for this WebResponse
	 *
	 */
	protected void setPage(HtmlPage page)
	{
		this.page = page;
	}
}
