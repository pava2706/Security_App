package com.Security_App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Security_App.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
