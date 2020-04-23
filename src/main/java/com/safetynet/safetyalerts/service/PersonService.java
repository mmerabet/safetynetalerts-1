package com.safetynet.safetyalerts.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safetynet.safetyalerts.dao.MedicalrecordDao;
import com.safetynet.safetyalerts.dao.PersonDao;
import com.safetynet.safetyalerts.dto.PersonInfo;
import com.safetynet.safetyalerts.model.Medicalrecord;
import com.safetynet.safetyalerts.model.Person;
import com.safetynet.safetyalerts.utility.CalculateAge;

@Service
public class PersonService {

	@Autowired
	PersonDao persondao;
	@Autowired
	MedicalrecordDao medicalrecorddao;

	public Collection<String> getCommunityEmail(String city) {

		List<Person> listPerson = persondao.listPersonByCity(city);
		Set<String> listEmails = new HashSet<String>();

		for (Person person : listPerson) {
			listEmails.add("email des habitants de la ville de " + city + " : "
					+ person.getEmail());
		}
		return listEmails;
	}

	public List<String> getPerson() {

		List<Person> listPerson = persondao.listPerson();
		List<String> listPersons = new ArrayList<String>();

		for (Person person : listPerson) {
			listPersons.add("Liste des personnes du fichier : "
					+ person.getFirstName() + " " + person.getLastName());
		}
		return listPersons;
	}

	public List<PersonInfo> getPersonInfo(String lastname, String firstname) {

		List<Person> listPerson = persondao.listPersonInfo(lastname, firstname);

		List<PersonInfo> listPersonInfo = new ArrayList<>();

		for (Person person : listPerson) {

			PersonInfo personInfo = new PersonInfo();
			Medicalrecord personMedicalRecord = medicalrecorddao
					.getMedicalrecordInfo(person.getLastName(),
							person.getFirstName());

			personInfo.setLastName(person.getLastName());
			personInfo.setFirstName(person.getFirstName());
			personInfo.setAddress(person.getAddress());
			personInfo.setEmail(person.getEmail());

			if (personMedicalRecord != null) {
				personInfo.setAge(CalculateAge
						.personBirthDate(personMedicalRecord.getBirthdate()));
				personInfo.setMedications(personMedicalRecord.getMedications());
				personInfo.setAllergies(personMedicalRecord.getAllergies());
			}
			listPersonInfo.add(personInfo);
		}
		return listPersonInfo;
	}
}
