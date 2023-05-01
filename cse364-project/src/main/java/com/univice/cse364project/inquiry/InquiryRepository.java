package com.univice.cse364project.inquiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends MongoRepository<Inquiry, String> {
}
