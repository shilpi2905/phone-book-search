package com.ocrolus.phbooksearch;

import java.util.List;

import com.ocrolus.phbooksearch.model.PhoneBookData;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        PhBookDataProcessor loader = new PhBookDataProcessor();
        try {
        	List<PhoneBookData> phoneBookData = loader.loadCSVFile("phone_dataset.csv");
        	if(!phoneBookData.isEmpty()) {
        		loader.searchByLastName("query.txt", phoneBookData);
        	}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }
}
