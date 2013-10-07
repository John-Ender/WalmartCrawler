import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


/**
 * The WebServiceClient contains methods to process WebRequests and 
 * return the relative WebResponses. 
 * 
 * Executes page requests using HtmlUnit 2.12, which simulates a web browser
 * 
 * Writes exceptions to ErrorLog.txt
 * 
 * @author Johnathon Ender
 *
 */
public class WebServiceClient 
{
	private PrintWriter writer;
	private String error = "Severe Exception: Log written to ErrorLog.txt";
	
	public WebServiceClient()
	{
		try {
			writer = new PrintWriter("ErrorLog.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File Not Found: " +  e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported Encoding: " +  e.getMessage());
		}
	}
	
	/**
	 * Executes the WalmartRequest and returns a WalmartResponse
	 *
	 *@param request The request to be executed
	 *@return The WalmartResponse 
	 */
	public WalmartResponse getWalmartResponse(WalmartRequest request)
	{
		return execute(request, WalmartResponse.class);
	}

	/**
	 * Executes the WebRequest and returns the corresponding WebResponse
	 *
	 *@param request The request to be executed
	 *@param clazz The class of the WebResponse subclass to be returned
	 *@return The WebResponse subclass 
	 *
	 *@throws IllegalArgumentException if the request is null
	 */
	protected <T extends WebResponse> T execute(WebRequest request, Class<T> clazz) throws IllegalArgumentException 
	{
		HtmlPage page = null;
		T Response = null;
		
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_17);

		if (request == null) {
			throw new IllegalArgumentException(
					"Invalid Request: request is null");
		}

		String url = request.BuildUri();

		try {
			page = webClient.getPage(url);
			
			Response = clazz.newInstance();
			Response.setPage(page);
			
		} catch (FailingHttpStatusCodeException e) {
			writer.append(e.getMessage());
			System.out.println(error);
		} catch (MalformedURLException e) {
			writer.append(e.getMessage());
			System.out.println(error);
		} catch (IOException e) {
			writer.append(e.getMessage());
			System.out.println(error);
		} catch (InstantiationException e) {
			writer.append(e.getMessage());
			System.out.println(error);
		} catch (IllegalAccessException e) {
			writer.append(e.getMessage());
			System.out.println(error);
		}
		
		writer.close();
		return Response;
	}
}
