package testScenarios;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/* AddressSeparator class takes address as input and separates street name and street number. Output will be saved in addressSeparator map.
 * All the addresses to be split into street name and street number to be given as input in addressList class.
 * Assumptions:
 * 1. Numerics if given at the first place in address will be considered as Street Number
 * 2. Any numerics given after "No" will be considered as Street Number
 * 3. Alphabets present after Street Number will be considered as part of street number (If 1 is not the case).
 * 
 * List of variables : 
 * addressSeparator - Output saved as map for easy access
 * addressList - List of input addresses
 */

public class AddressSeparator {

	public static void main(String args[])
	{
		Map<String,String> addressSeparator = new HashMap<String,String>() ;	
		List<String> addressList = Arrays.asList("Winterallee 3","Musterstrasse 45","Blaufeldweg 123B","Am Bächle 23","Auf der Vogelwiese 23 b","4, rue de la revolution","200 Broadway Av","Calle Aduana, 29","Calle 39 No 1540");
		String streetNumber = "";
		String streetName = "";
		Pattern p = Pattern.compile("\\d+");

		for(String address:addressList){
			Matcher m = p.matcher(address);
			while(m.find()){
				//checking if street number is at beginning of address
				if(m.start()==0){
					if(address.contains(",")){
						streetNumber = address.split(",")[0];
						streetName = address.split(",")[1];
					}
					else{
						streetNumber = m.group();
						streetName = address.substring(m.end()).trim();
					}
					break;
				}
				else{
					if(address.contains(",")){
						streetNumber = address.split(",")[1];
						streetName = address.split(",")[0];
					}
					else if(address.contains(" No")){
						streetNumber = address.substring(address.indexOf(" No"));
						streetName = address.split(" No")[0];
					}
					else{
						streetNumber = address.substring(m.start());
						streetName = address.substring(0, m.start());
					}				
				}
			}
			addressSeparator.put(streetName.trim(), streetNumber.trim());
		}
		System.out.println(addressSeparator);
	}

}
