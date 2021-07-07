package com.example.BoardSystem.Repository;

import com.example.BoardSystem.Entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
