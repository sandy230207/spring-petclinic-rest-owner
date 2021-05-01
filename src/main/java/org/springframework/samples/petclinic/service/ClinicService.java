/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;


/**
 * Mostly used as a facade so all controllers have a single point of entry
 *
 * @author Michael Isvy
 * @author Vitaliy Fedoriv
 */
public interface ClinicService {

	Pet findPetById(int id) throws DataAccessException;
	Collection<Pet> findAllPets() throws DataAccessException;
	void savePet(Pet pet) throws DataAccessException;
	void deletePet(Pet pet) throws DataAccessException;

	Collection<Visit> findVisitsByPetId(int petId);
	// Collection<Visit> findVisitByOwnerId(int ownerId);
	Visit findVisitById(int visitId) throws DataAccessException;
	Collection<Visit> findAllVisits() throws DataAccessException;
	void saveVisit(Visit visit) throws DataAccessException;
	void deleteVisit(Visit visit) throws DataAccessException;
	// void deleteVisitByPetId(int petId) throws DataAccessException;
	
	Owner findOwnerById(int id) throws DataAccessException;
	Collection<Owner> findAllOwners() throws DataAccessException;
	void saveOwner(Owner owner) throws DataAccessException;
	void deleteOwner(Owner owner) throws DataAccessException;
	Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException;

	PetType findPetTypeById(int petTypeId);
	Collection<PetType> findAllPetTypes() throws DataAccessException;
	Collection<PetType> findPetTypes() throws DataAccessException;
	void savePetType(PetType petType) throws DataAccessException;
	void deletePetType(PetType petType) throws DataAccessException;
	
}
