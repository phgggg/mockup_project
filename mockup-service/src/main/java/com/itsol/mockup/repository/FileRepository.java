package com.itsol.mockup.repository;

import com.itsol.mockup.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
    FileEntity findFileEntitiesByFileId(Long id);

}
