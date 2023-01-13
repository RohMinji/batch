package com.example.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;


public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(Person person) throws Exception {
		Person per = person;
		final String firstName = per.getFirstName();
		final String lastName = per.getLastName();
		final String birthDate = per.getBirthDate();

		final Person addedPerson = new Person(firstName, lastName, birthDate);

		log.info("Saving information :" + addedPerson + " to target database");

		return addedPerson;
	}

}
