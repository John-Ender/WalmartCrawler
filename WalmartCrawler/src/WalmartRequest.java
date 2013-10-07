import java.util.ArrayList;
import java.util.List;

/**
 * The Walmart request builds the walmart.com url to search 
 * for products defined by the query list and page number
 * 
 * @author Johnathon Ender
 *
 */
public class WalmartRequest extends WebRequest
{
	private final int numOfProductInList = 16;
	private final String c_walmart = 
			"http://www.walmart.com/search/search-ng.do?" +
			"search_query=%s" +
			"&ic=16_%s" +
			"&Find=Find&search_constraint=0";
	
	private int pageNumber = 0;
	private List<String> query = new ArrayList<String>();
	
	/**
	 * Returns the list of query strings
	 *
	 *@return A list of strings representing the queries to be made
	 */
	public List<String> getQuery()
	{
		return query;
	}
	
	/**
	 * Returns the page number that will be retrieved
	 *
	 *@return An integer representing the page number
	 */
	public int getPageNumber()
	{
		return pageNumber + 1;
	}
	
	/**
	 * Sets the list of query strings
	 * 
	 *@param query A list of strings representing the query parameters
	 */
	public void setQuery(List<String> query)
	{
		this.query = query;
	}
	
	/**
	 * Sets the page number
	 * 
	 *@param pageNumber An int representing the page number
	 */
	public void setPageNumber(int pageNumber)
	{
		this.pageNumber = pageNumber - 1;
	}
	
	/**
	 * Builds the Walmart.com url based on the query strings and page number
	 *
	 *@return A string representing the url
	 */
	protected String BuildUri() 
	{
		StringBuilder sb = new StringBuilder();
		int index = numOfProductInList * pageNumber;
		
		for(int i = 0; i < query.size(); i++)
		{
			if(i != 0)
			{
				sb.append('+');
			}
			
			sb.append(query.get(i));
		}
		
		return String.format(c_walmart, sb.toString(), String.valueOf(index));
	}

}
