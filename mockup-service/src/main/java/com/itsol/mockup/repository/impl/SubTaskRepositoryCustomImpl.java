package com.itsol.mockup.repository.impl;

import com.itsol.mockup.repository.SubTaskRepositoryCustom;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.utils.HibernateUtil;
import com.itsol.mockup.utils.SQLBuilder;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class SubTaskRepositoryCustomImpl extends BaseRepo implements SubTaskRepositoryCustom {
    @Override
    public Long getSumOfEstimatedHoursByUserId(Long id, Timestamp mon, Timestamp sat) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_SELECT_ESTIMATED_HOURS));
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_FROM_SUBTASK));
            SQLQuery query = session.createSQLQuery(sb.toString());

            if (!DataUtils.isNullOrEmpty(String.valueOf(mon))) {
                query.setParameter("mon",
                        String.valueOf(mon).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(sat))) {
                query.setParameter("sat",
                        String.valueOf(sat).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(id))) {
                query.setParameter("id",
                        String.valueOf(id).trim());
            }
            query.addScalar("estimated_hours", new LongType());
            Long estimatedHours = (Long) query.uniqueResult();
            return estimatedHours != null ? estimatedHours : 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return 0L;
    }

    @Override
    public Long getSumOfHoursSpentPerTaskDoneByUserId(Long id, Timestamp mon, Timestamp sat) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_SELECT_ESTIMATED_HOURS));
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_FROM_SUBTASK));
            sb.append("AND st.status = 'done'");
            SQLQuery query = session.createSQLQuery(sb.toString());

            if (!DataUtils.isNullOrEmpty(String.valueOf(mon))) {
                query.setParameter("mon",
                        String.valueOf(mon).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(sat))) {
                query.setParameter("sat",
                        String.valueOf(sat).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(id))) {
                query.setParameter("id",
                        String.valueOf(id).trim());
            }
            query.addScalar("estimated_hours", new LongType());
            Long estimatedHours = (Long) query.uniqueResult();
            return estimatedHours != null ? estimatedHours : 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return 0L;
    }

    @Override
    public Long getSumOfHoursSpentByUserId(Long id, Timestamp mon, Timestamp sat) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_SELECT_HOURS_SPENT));
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_FROM_SUBTASK));
            SQLQuery query = session.createSQLQuery(sb.toString());

            if (!DataUtils.isNullOrEmpty(String.valueOf(mon))) {
                query.setParameter("mon",
                        String.valueOf(mon).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(sat))) {
                query.setParameter("sat",
                        String.valueOf(sat).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(id))) {
                query.setParameter("id",
                        String.valueOf(id).trim());
            }
            query.addScalar("hours_spent", new LongType());
            Long estimatedHours = (Long) query.uniqueResult();
            return estimatedHours != null ? estimatedHours : 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return 0L;
    }

    @Override
    public Long getSumOfMonthlyEstimatedHoursByUserId(Long id, int month, int year) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_SELECT_ESTIMATED_HOURS));
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_FROM_SUBTASK_MONTH));
            SQLQuery query = session.createSQLQuery(sb.toString());

            if (!DataUtils.isNullOrEmpty(String.valueOf(month))) {
                query.setParameter("month",
                        String.valueOf(month).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(year))) {
                query.setParameter("year",
                        String.valueOf(year).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(id))) {
                query.setParameter("id",
                        String.valueOf(id).trim());
            }
            query.addScalar("estimated_hours", new LongType());
            Long estimatedHours = (Long) query.uniqueResult();
            return estimatedHours != null ? estimatedHours : 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return 0L;
    }

    @Override
    public Long getSumOfMonthlyHoursSpentPerTaskDoneByUserId(Long id, int month, int year) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_SELECT_ESTIMATED_HOURS));
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_FROM_SUBTASK_MONTH));
            sb.append("AND st.status = 'done'");
            SQLQuery query = session.createSQLQuery(sb.toString());

            if (!DataUtils.isNullOrEmpty(String.valueOf(month))) {
                query.setParameter("month",
                        String.valueOf(month).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(year))) {
                query.setParameter("year",
                        String.valueOf(year).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(id))) {
                query.setParameter("id",
                        String.valueOf(id).trim());
            }
            query.addScalar("estimated_hours", new LongType());
            Long estimatedHours = (Long) query.uniqueResult();
            return estimatedHours != null ? estimatedHours : 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return 0L;
    }

    @Override
    public Long getSumOfMonthlyHoursSpentByUserId(Long id, int month, int year) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_SELECT_HOURS_SPENT));
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_SUBTASK, SQLBuilder.SQL_FILE_FROM_SUBTASK_MONTH));
            SQLQuery query = session.createSQLQuery(sb.toString());

            if (!DataUtils.isNullOrEmpty(String.valueOf(month))) {
                query.setParameter("month",
                        String.valueOf(month).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(year))) {
                query.setParameter("year",
                        String.valueOf(year).trim().toUpperCase());
            }

            if (!DataUtils.isNullOrEmpty(String.valueOf(id))) {
                query.setParameter("id",
                        String.valueOf(id).trim());
            }
            query.addScalar("hours_spent", new LongType());
            Long estimatedHours = (Long) query.uniqueResult();
            return estimatedHours != null ? estimatedHours : 0L;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }
        return 0L;
    }
}
