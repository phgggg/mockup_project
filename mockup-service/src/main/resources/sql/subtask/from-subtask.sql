FROM subtask st JOIN timesheet t
ON st.timesheet_id = t.timesheet_id
WHERE (t.actual_finish_date IS NULL OR (t.actual_finish_date BETWEEN :mon AND :sat))
AND t.actual_start_date <= :sat
AND st.assigned_user_id = :id
