package com.phoneBookSearch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.ocrolus.phbooksearch.PhBookDataProcessor;
import com.ocrolus.phbooksearch.exception.PhBookSearchException;
import com.ocrolus.phbooksearch.model.PhoneBookData;

public class PhBookDataProcessorTest {

	PhBookDataProcessor processor = new PhBookDataProcessor();
	@Test
	public void loadCSVFileTestInvalidFileName() throws PhBookSearchException {
		Assertions.assertThrows(PhBookSearchException.class, () -> {
			processor.loadCSVFile("phone.csv");
		});
		
	}
	
	@Test
	public void loadCSVFileTestSuccess() throws PhBookSearchException {
		assertNotNull(processor.loadCSVFile("phone_dataset.csv"));
		
	}
	
	@Test
	public void searchByLastNameSuccess() throws PhBookSearchException, IOException {
		List<PhoneBookData> phoneDataList = new ArrayList<PhoneBookData>();
		PhoneBookData data1 = new PhoneBookData();
		data1.setFirstName("John");
		data1.setLastName("Doe");
		data1.setPhoneNo("(917) 958-1191");
		data1.setState("New York");
		processor.searchByLastName("query.txt", phoneDataList);
		List<String> dataFromOutput = Files.readAllLines(Paths.get("output.txt"), StandardCharsets.UTF_8);
		assertNotNull(dataFromOutput);
		
	}
	
	@Test
	public void searchByLastNameFailure() {
		List<PhoneBookData> phoneDataList = new ArrayList<PhoneBookData>();
		Assertions.assertThrows(PhBookSearchException.class, () -> {
			processor.searchByLastName("quer.txt", phoneDataList);
		});
		
	}
	
}
