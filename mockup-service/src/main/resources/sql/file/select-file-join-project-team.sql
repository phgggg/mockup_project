select
    f.file_id,
    f.file_name,
    f.actual_name,
    f.file_url,
    f.uploaded_by,
    f.uploaded_date,
    f.allowed_user,
    f.last_modified_by,
    f.last_modified_date,
    f.previous_ver,
    f.previous_url,
    f.next_ver,
    f.next_url,
    f.project_id
from file f
join project p on f.project_id = p.project_id
join team t on p.project_id = t.project_id
where 1 = 1
