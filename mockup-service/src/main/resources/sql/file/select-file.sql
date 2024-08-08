SELECT
    f.file_id fileId,
    f.file_name fileName,
    f.actual_name actualName,
    f.file_url fileUrl,
    f.next_ver nextVer,
    f.previous_ver previousVer,
    f.next_url nextUrl,
    f.previous_url previousUrl,
    f.uploaded_by uploadedBy,
    f.last_modified_by lastModifiedBy,
    f.last_modified_date lastModifiedDate,
    f.uploaded_date uploadedDate
FROM file f
WHERE 1 = 1