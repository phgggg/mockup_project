package com.itsol.mockup.repository;

import com.itsol.mockup.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
    FileEntity findFileEntitiesByFileId(Long id);

    @Query(value = "SELECT * FROM file where file_name = :actualFileName limit 1", nativeQuery = true)
    FileEntity findFileEntityByActualFileName(String actualFileName);

}
