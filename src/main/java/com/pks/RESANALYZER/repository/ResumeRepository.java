package com.pks.RESANALYZER.repository;

import com.pks.RESANALYZER.entity.Resume;
import com.pks.RESANALYZER.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends MongoRepository<Resume, String> {
    List<Resume> findByUser(User user);
}