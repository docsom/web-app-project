package com.univice.cse364project.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends MongoRepository<User, String> {
}
