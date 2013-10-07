import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <p>
 * The WalmartCrawler is an application which scrapes information from
 * the walmart.com database using a url representing a site search. This 
 * url is passed through a web browser simulator provided by the HtmlUtil
 * libarary. The HtmlPage is then converted to an xml format and parsed 
 * using the Jsoup Html parsing library. 
 * </p>
 * <p>
 * The information used to parse the Html was obtained by careful inspection 
 * of the walmart.com products results page source. 
 * </p>
 * <p>
 * WalmartCrawler can be invoked using two methods
 * <li>java WalmartCrawler.jar "var1 var2 ..."</li>
 * <li>java WalmartCrawler.jar "var1 var2 ..." int</li>
 * </p>
 * <p>
 * The first method prints the number of results for the search variables specified
 * while the second method prints the search results including the name and associated price.
 * </p>
 * <p>
 * --KNOWN BUGS--
 * 
 * <li>Currently there is an error with an search query including the term baby. Inspection of the 
 *     stack trace has isolated the problem due to adds on the page that HtmlUnit cannot handle</li>
 *     
 * <li>Some broad search terms cause a page redirect. This case could not be handled in the limited assignment time</li>
 * 
 * <li>Some search results may vary from different web browsers by +-10 results</li>
 * </p>
 * <p>
 * --Successfull Tests--
 *
 * The following is a short list of successful test cases
 * 
 * <li>"camera"</li>
 * <li>"camera" 3</li>
 * <li>"camera" 10</li>
 * <li>"digital camera"</li>
 * <li>"camera digital"</li>
 * <li>"digital camera" 5</li>
 * <li>"camera digital" 6</li>
 * <li>"folding table"</li>
 * <li>"blinds" 7</li>
 * <li>"apple"</li>
 * <li>"apple" 6</li>
 * <li>"dictionary"</li>
 * <li>"dictionary" 1</li>
 * </p>
 * @author Johnathon Ender
 *
 */
public class WalmartCrawler
{
	private static int numberOfArguments = 0; 
	private static List<String> query;
	private static int pageNumber = 1;
	private final static String argumentError = "Fatal Error: Incorrect number of arguments";
	private final static String firstArgumentInvalidError = "Fatal Error: First argument is empty";
	private final static String secondArgumentInvalidError = "Fatal Error: Second argument is not a positive integer greater than 0";
	private final static String formatHint = "Invoke using: \"var1 var2 ...\" or \"var1 var2 ...\" int";


	public static void main(String[] args)
	{
		final WebServiceClient client = new WebServiceClient();
		WalmartRequest request = new WalmartRequest();
		query = new ArrayList<String>();
		WalmartResponse response;

		//Silence the logging and process the command line arguments. 
		silenceLogging();
		processCommandLineArguments(args);
		
		//Set the request values 
		request.setQuery(query);
		request.setPageNumber(pageNumber);

		//Get the response from the WebServiceClient
		response = client.getWalmartResponse(request);

		//Print the appropriate pieces of information based on command line arguments
		if(numberOfArguments == 1)
		{
			System.out.println(response.getNumberOfResults());
		}
		else
		{
			List<Product> products = response.getProductList();
			
			for(Product p : products)
			{
				System.out.println(p.getName() + "\n" + p.getPrice() + "\n");
			}
		}

	}

	/**
	 * HtmlUnit is extraordinarily verbose with error logging
	 * This function limits the logging to Fatal and Severe levels
	 *
	 */
	private static void silenceLogging()
	{
		org.apache.log4j.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(org.apache.log4j.Level.FATAL);
		org.apache.log4j.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript").setLevel(org.apache.log4j.Level.FATAL);
		org.apache.log4j.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(org.apache.log4j.Level.FATAL);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.SEVERE);
	}

	/**
	 * Processes the command line arguments to verify correctness
	 * of operation. This function constructs the query list and 
	 * sets the appropriate pieces of information if the arguments
	 * match what is intended. 
	 *
	 */
	private static void processCommandLineArguments(String[] args)
	{
		numberOfArguments = args.length;

		if(numberOfArguments == 0 || numberOfArguments > 2)
		{
			System.out.println(argumentError);
			System.out.println(formatHint);
			System.exit(-1);
		}

		if(args[0].isEmpty())
		{
			System.out.println(firstArgumentInvalidError);
			System.out.println(formatHint);
			System.exit(-1);
		}
		else
		{
		     StringTokenizer st = new StringTokenizer(args[0]);
		     while (st.hasMoreTokens()) {
		         query.add(st.nextToken());
		     }
		}

		if(numberOfArguments == 2)
		{
			if(!isInteger(args[1]))
			{
				System.out.println(secondArgumentInvalidError);
				System.out.println(formatHint);
				System.exit(-1);
			}
			else 
			{
				pageNumber = Integer.parseInt(args[1]);
				
				if(pageNumber < 1)
				{
					System.out.println(secondArgumentInvalidError);
					System.out.println(formatHint);
					System.exit(-1);
				}
			}
		}
	}
	
	/**
	 * A simple function to check if a string is numeric. 
	 * 
	 * @return A boolean value if the string is numeric
	 */
	private static boolean isInteger( String input )  
	{  
		try  
		{  
			Integer.parseInt( input );  
			return true;  
		}  
		catch(Exception e)  
		{  
			return false;  
		}  
	}

}
