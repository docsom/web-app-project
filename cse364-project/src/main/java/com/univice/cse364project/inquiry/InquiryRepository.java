package com.univice.cse364project.inquiry;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InquiryRepository extends MongoRepository<Inquiry, String> {
}
