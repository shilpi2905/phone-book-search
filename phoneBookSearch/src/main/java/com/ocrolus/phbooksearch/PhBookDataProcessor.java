package com.ocrolus.phbooksearch;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ocrolus.phbooksearch.exception.PhBookSearchException;
import com.ocrolus.phbooksearch.model.PhoneBookData;

import au.com.bytecode.opencsv.CSVReader;

public class PhBookDataProcessor {

	private final static Logger LOG = Logger.getLogger(PhBookDataProcessor.class.getName());

	List<String> statesInUS = Collections.unmodifiableList(Arrays.asList("Alabama", "Alaska", "American Samoa",
			"Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia",
			"Florida", "Georgia", "Guam", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
			"Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Minor Outlying Islands",
			"Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico",
			"New York", "North Carolina", "North Dakota", "Northern Mariana Islands", "Ohio", "Oklahoma", "Oregon",
			"Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas",
			"U.S. Virgin Islands", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin",
			"Wyoming"));

	@SuppressWarnings("resource")
	public List<PhoneBookData> loadCSVFile(String filename) throws PhBookSearchException {
		try {
			CSVReader reader = new CSVReader(new FileReader(filename));
			String[] nextLine;
			List<PhoneBookData> dataList = new ArrayList<PhoneBookData>();
			while ((nextLine = reader.readNext()) != null) {
				if (nextLine != null && validateRow(nextLine)) {
					PhoneBookData data = new PhoneBookData();
					data.setFirstName(nextLine[0].trim());
					data.setLastName(nextLine[1].trim());
					if (statesInUS.contains(nextLine[2].trim())) {
						data.setState(nextLine[2].trim());
						data.setPhoneNo(nextLine[3].trim());
					} else {
						data.setState(nextLine[3].trim());
						data.setPhoneNo(nextLine[2].trim());
					}
					dataList.add(data);
				} else {
					LOG.warning("Invalid row: " + Arrays.toString(nextLine));
					continue;
				}

			}
			return dataList;
		} catch (IOException e) {
			throw new PhBookSearchException(e.getMessage());
		}
	}

	private boolean validateRow(String[] row) {
		int flag = 0;
		if (row.length != 4) {
			return false;
		}
		if (!statesInUS.contains(row[0].trim()) && !validatePhoneNumber(row[0].trim())
				&& row[0].trim().matches("[A-Z][a-zA-Z]*")) {
			flag++;
		}
		if (!statesInUS.contains(row[1].trim()) && !validatePhoneNumber(row[1].trim())
				&& row[1].trim().matches("[A-Z][a-zA-Z]*")) {
			flag++;
		}
		if (statesInUS.contains(row[2].trim()) && row[3].trim().matches("\\(\\d{3}\\)\\s\\d{3}-\\d{4}")) {
			flag += 2;
		} else if (validatePhoneNumber(row[2].trim()) && statesInUS.contains(row[3].trim())) {
			flag += 2;
		}
		if (flag == 4) {
			return true;
		}
		return false;
	}

	private boolean validatePhoneNumber(String phoneNo) {
		if (phoneNo.matches("\\d{10}"))
			return true;
		else if (phoneNo.matches("\\(\\d{3}\\)\\s\\d{3}-\\d{4}"))
			return true;
		return false;
	}

	public void searchByLastName(String filename, List<PhoneBookData> phoneDataList) throws PhBookSearchException {
		List<String> lastNames = new ArrayList<String>();
		try {
			lastNames = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
			BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
			if (!lastNames.isEmpty()) {
				for (String lastName : lastNames) {
					String lName = lastName.trim();
					writer.write("Matches for: " + lName);
					writer.write("\n");
					List<PhoneBookData> filterByLastName = phoneDataList.stream()
							.filter(data -> data.getFirstName().equalsIgnoreCase(lName)
									|| data.getLastName().equalsIgnoreCase(lName))
							.collect(Collectors.toList());
					if (filterByLastName.isEmpty()) {
						writer.write("No results found");
					} else {
						writeToTxtFile(lName, filterByLastName, writer);
					}
				}

			} else {
				LOG.warning("query.txt file is empty");
			}
			writer.close();
		}

		catch (IOException e) {
			throw new PhBookSearchException(e.getMessage());
		}
	}

	private void writeToTxtFile(String lastName, List<PhoneBookData> filteredList, BufferedWriter writer)
			throws IOException {
		int count = 1;
		String fName, lName;
		for (PhoneBookData data : filteredList) {
			if (data.getFirstName().equalsIgnoreCase(lastName)) {
				fName = data.getLastName();
				lName = data.getFirstName();
			} else {
				lName = data.getLastName();
				fName = data.getFirstName();
			}
			writer.write("Result " + count + ": " + lName + ", " + fName + ", " + data.getState() + ", "
					+ data.getPhoneNo());
			writer.write("\n");
			count++;
		}
	}
}
