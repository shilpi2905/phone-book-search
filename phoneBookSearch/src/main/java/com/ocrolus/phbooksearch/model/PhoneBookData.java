package com.ocrolus.phbooksearch.model;

public class PhoneBookData {

	private String firstName;
	private String lastName;
	private String state;
	private String phoneNo;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	@Override
	public String toString() {
		return "PhoneBookData [firstName=" + firstName + ", lastName=" + lastName + ", state=" + state + ", phoneNo="
				+ phoneNo + "]";
	}
}
