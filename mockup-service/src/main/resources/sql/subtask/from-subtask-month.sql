FROM subtask st JOIN timesheet t ON st.timesheet_id = t.timesheet_id
WHERE (t.actual_finish_date IS NULL OR (MONTH(t.actual_finish_date) = :month AND YEAR(t.actual_finish_date) = :year))
AND MONTH(t.actual_start_date) <= :month
AND st.assigned_user_id = :id
