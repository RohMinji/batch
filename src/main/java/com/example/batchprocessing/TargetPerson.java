package com.example.batchprocessing;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "TARGET_PERSON")
public class TargetPerson {
	@Id
	private int id;
	private String lastName;
	private String firstName;
	private Date birthDate;

	public TargetPerson() {
	}

	public TargetPerson(int id, String firstName, String lastName, Date birthDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "id: " + id + "firstName: " + firstName + ", lastName: " + lastName+ ", birthDate: " + birthDate;
	}

}
