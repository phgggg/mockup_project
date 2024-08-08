package com.itsol.mockup.repository;

import com.itsol.mockup.entity.FileTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileTypeRepository extends JpaRepository<FileTypeEntity,Long> {
    List<FileTypeEntity> findAll();
}
