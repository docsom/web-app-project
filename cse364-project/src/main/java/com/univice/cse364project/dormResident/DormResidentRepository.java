package com.univice.cse364project.dormResident;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DormResidentRepository extends MongoRepository<DormResident, String> {
}
