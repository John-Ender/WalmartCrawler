import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The WalmartResponse class builds a list of Products
 * from the HtmlPage associated with it, and contains
 * information on how many results occured on the 
 * WalmartRequest search parameters. 
 * 
 * @author Johnathon Ender
 *
 */
public class WalmartResponse extends WebResponse
{
	private List<Product> productList;
	private String numResults = "";
	
	/**
	 * Returns the list of Products found in the search
	 * 
	 *@return A list of Products
	 */
	public List<Product> getProductList()
	{
		if(productList == null)
		{
			productList = new ArrayList<Product>();
			constructProductList(this.getPage().asXml());
		}

		return productList;
	}

	/**
	 * Returns a string representing the number of results
	 * for the query
	 *
	 *@return A String containing the number of results
	 */
	public String getNumberOfResults()
	{
		if(numResults.isEmpty())
		{
			findNumberOfResults(this.getPage().asXml());
		}
		
		return numResults;
	}
	
	/**
	 * Constructs the product list using information obtained by
	 * reading through the page source provided by a current version 
	 * of Mozilla Firefox
	 * 
	 * Uses Jsoup to parse through the HtmlPage xml
	 *
	 */
	private void constructProductList(String html)
	{
		Document doc = Jsoup.parse(html);
		Elements products = doc.select("div[class=prodInfoBox]"); //Isolate the product info from the rest of the page

		//For each product get the relevant information from the product elements
		for(Element e : products)
		{	
			Product product = new Product(); 
			
			 //prodLink GridItemLink contains the the product name
			Elements ProductTitle = e.getAllElements().select("a[class=prodLink GridItemLink]");
			// PriceContent contains the various price information
			Elements PriceContent = e.getAllElements().select("div.PriceContent"); 

			// The title is used instead of the text due to the text being truncated,
			//while the title is not always the the same as the full text a better solution could not be found within the limited time. 
			product.setName(ProductTitle.first().attr("title")); 

			//These elements contain information if the price is out of stock or in store only
			//Used to differentiate if the List price should be used
			Elements outOfStock = PriceContent.first().getAllElements().select("p.ShelfTextOOS, p[class=ShelfText StorePriceVary]"); 
			
			//These elements contain the price information as shown. 
			Elements Prices = PriceContent.first().getAllElements().select("div.camelPrice, span.camelPrice, div.PriceLBold");

			//The following corner cases were inquired about via jobs.questions@brightedge.com but no answer was received
			//If the outOfStock elements are empty the price either cannot be found or list price should be used
			if(outOfStock.isEmpty())
			{
				if(Prices == null || Prices.isEmpty())
				{
					product.setPrice("Price information not found");
				}
				else
				{
					product.setPrice(Prices.first().text());
				}
			}
			//Else out of stock isn't empty and should be printed as out of stock. 
			else
			{
				product.setPrice(outOfStock.first().text());
			}

			//Finally add the constructed product to the list
			productList.add(product);
		}			
	}
	
	/**
	 * Finds the number of results on the WalmartResponse page
	 * using information obtained by reading through the page
	 * source provided by a current version of Mozilla Firefox
	 * 
	 */
	private void findNumberOfResults(String html)
	{
		Document doc = Jsoup.parse(html);
		
		Elements results = doc.select("span[class=floatLeft numResults mt5]");
		
		//Some pages with broad with broad search terms cause a page redirect. 
		//This situation is currently unhandled due to limited time. 
		if(!results.isEmpty())
		{
			numResults = results.first().text();
		}
		else
		{
			numResults = "No results found, query may be too broad resulting in page redirect";
		}
	}
}
