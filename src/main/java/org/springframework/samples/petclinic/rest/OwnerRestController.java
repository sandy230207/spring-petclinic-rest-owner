/*
 * Copyright 2016-2017 the original author or authors.
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

package org.springframework.samples.petclinic.rest;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;
import java.util.Comparator;
import java.util.stream.Collectors;

import java.text.SimpleDateFormat;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Vitaliy Fedoriv
 *
 */

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/owners")
public class OwnerRestController {

	@Autowired
	private ClinicService clinicService;

	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/*/lastname/{lastName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Owner>> getOwnersList(@PathVariable("lastName") String ownerLastName) {
		if (ownerLastName == null) {
			ownerLastName = "";
		}
		Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
		if (owners.isEmpty()) {
			return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Owner>> getOwners() {
		Collection<Owner> owners = this.clinicService.findAllOwners();
		if (owners.isEmpty()) {
			return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/{ownerId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Owner> getOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = null;
		owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Owner>(owner, HttpStatus.OK);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Owner> addOwner(@RequestBody @Valid Owner owner, BindingResult bindingResult,
			UriComponentsBuilder ucBuilder) {
		HttpHeaders headers = new HttpHeaders();
		if (bindingResult.hasErrors() || owner.getId() != null) {
            BindingErrorsResponse errors = new BindingErrorsResponse(owner.getId());
			errors.addAllErrors(bindingResult);
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Owner>(headers, HttpStatus.BAD_REQUEST);
		}
		this.clinicService.saveOwner(owner);
		headers.setLocation(ucBuilder.path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
		return new ResponseEntity<Owner>(owner, headers, HttpStatus.CREATED);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/{ownerId}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Owner> updateOwner(@PathVariable("ownerId") int ownerId, @RequestBody @Valid Owner owner,
			BindingResult bindingResult, UriComponentsBuilder ucBuilder) {
	    boolean bodyIdMatchesPathId = owner.getId() == null || ownerId == owner.getId();
		if (bindingResult.hasErrors() || !bodyIdMatchesPathId) {
            BindingErrorsResponse errors = new BindingErrorsResponse(ownerId, owner.getId());
			errors.addAllErrors(bindingResult);
            HttpHeaders headers = new HttpHeaders();
			headers.add("errors", errors.toJSON());
			return new ResponseEntity<Owner>(headers, HttpStatus.BAD_REQUEST);
		}
		Owner currentOwner = this.clinicService.findOwnerById(ownerId);
		if (currentOwner == null) {
			return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
		}
		currentOwner.setAddress(owner.getAddress());
		currentOwner.setCity(owner.getCity());
		currentOwner.setFirstName(owner.getFirstName());
		currentOwner.setLastName(owner.getLastName());
		currentOwner.setTelephone(owner.getTelephone());
		this.clinicService.saveOwner(currentOwner);
		return new ResponseEntity<Owner>(currentOwner, HttpStatus.NO_CONTENT);
	}

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/{ownerId}", method = RequestMethod.DELETE, produces = "application/json")
	@Transactional
	public ResponseEntity<Void> deleteOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deleteOwner(owner);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
	@RequestMapping(value = "/appointments/{ownerId}/{date}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Visit>> getAppointmentByDate(@PathVariable("ownerId") int ownerId, @PathVariable("date") String date) {
		Date targetDate = null;
		if (date.equals("now")){
			targetDate = new Date();
		} else {
			date += " 08:00:00";
			try {
				targetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			} catch (Exception e) {
				return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
			}
		}
		
		Owner owner = null;
		owner = this.clinicService.findOwnerById(ownerId);
		if (owner == null) {
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}

		List<Pet> pets = owner.getPets();
		Collection<Visit> visits = new ArrayList<Visit>();
		
		for(int i = 0; i < pets.size(); i++){
			for(int j = 0; j < pets.get(i).getVisits().size(); j++){
				Visit visit = pets.get(i).getVisits().get(j);
				if (visit.getDate().after(targetDate)){
					visits.add(visit);
				}
			}
		}
		if (visits.isEmpty()) {
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}

		List<Visit> visitList = new ArrayList<Visit>();
		visitList.addAll(visits);

		visitList = visitList.stream()
        .sorted(Comparator.comparing(Visit::getDate))
		.collect(Collectors.toList());
		
		visits = new ArrayList<Visit>();
		visits.addAll(visitList);
		return new ResponseEntity<Collection<Visit>>(visits, HttpStatus.OK);
	}

	@PreAuthorize( "hasRole(@roles.VET_ADMIN)" )
	@RequestMapping(value = "/appointments/{date}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Visit>> getAllAppointmentByDate(@PathVariable("date") String date) {
		Date targetDate = null;
		if (date.equals("now")){
			targetDate = new Date();
		} else {
			date += " 08:00:00";
			try {
				targetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			} catch (Exception e) {
				return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
			}
		}

		Collection<Owner> owners = new ArrayList<Owner>();
		owners = this.clinicService.findAllOwners();
		if (owners.isEmpty()) {
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}

		List<Pet> pets = new ArrayList<Pet>();
		for (Owner owner : owners) {
			pets.addAll(owner.getPets());
		}

		Collection<Visit> visits = new ArrayList<Visit>();
		for(int i = 0; i < pets.size(); i++){
			for(int j = 0; j < pets.get(i).getVisits().size(); j++){
				Visit visit = pets.get(i).getVisits().get(j);
				if (visit.getDate().after(targetDate)){
					visits.add(visit);
				}
			}
		}
		if (visits.isEmpty()) {
			return new ResponseEntity<Collection<Visit>>(HttpStatus.NOT_FOUND);
		}

		List<Visit> visitList = new ArrayList<Visit>();
		visitList.addAll(visits);

		visitList = visitList.stream()
        .sorted(Comparator.comparing(Visit::getDate))
		.collect(Collectors.toList());
		
		visits = new ArrayList<Visit>();
		visits.addAll(visitList);
		
		return new ResponseEntity<Collection<Visit>>(visits, HttpStatus.OK);
	}

}
