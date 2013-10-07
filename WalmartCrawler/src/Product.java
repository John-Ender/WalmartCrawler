
/**
 * The Product class is a container for information representing a product. 
 * This includes the name of the product and the price
 * 
 * @author Johnathon Ender
 *
 */
public class Product 
{
	private String name = "";
	private String price = "";
	
	/**
	 * Returns the name of the product
	 * 
	 * @return A string representing the name of the product
	 *
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns the price of the product
	 * 
	 * @return A string representing the price of the product
	 *
	 */
	public String getPrice()
	{
		return price;
	}
	
	/**
	 * Sets the name of the product
	 *
	 *@param name A string representing the name of the product
	 */
	protected void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Sets the price of the product
	 *
	 *@param price A string representing the price of the product
	 */
	protected void setPrice(String price)
	{
		this.price = price;
	}
}
