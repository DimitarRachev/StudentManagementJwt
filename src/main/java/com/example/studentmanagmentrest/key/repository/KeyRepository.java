package com.example.studentmanagmentrest.key.repository;

import com.example.studentmanagmentrest.key.model.SecredKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends JpaRepository<SecredKey,Long> {
}
