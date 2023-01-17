package com.example.batchprocessing;

public class Person {

	private String lastName;
	private String firstName;
	private String birthDate;

	public Person() {
	}

	public Person(String firstName, String lastName, String birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	@Override
	public String toString() {
		return "firstName: " + firstName + ", lastName: " + lastName+ ", birthDate: " + birthDate;
	}

}
