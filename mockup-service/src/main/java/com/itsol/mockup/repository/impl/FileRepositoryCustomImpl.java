package com.itsol.mockup.repository.impl;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.repository.FileRepositoryCustom;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.utils.HibernateUtil;
import com.itsol.mockup.utils.PageBuilder;
import com.itsol.mockup.utils.SQLBuilder;
import com.itsol.mockup.web.dto.file.FileDTO;
import com.itsol.mockup.web.dto.file.FileSearchDTO;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
@Repository
public class FileRepositoryCustomImpl extends BaseRepo implements FileRepositoryCustom {
    @Value("${spring.datasource.url}")
    private String DB_URL;
    @Value("${spring.datasource.username}")
    private String USER_NAME;
    @Value("${spring.datasource.password}")
    private String PASSWORD;
    @Value("${spring.datasource.driver-class-name}")
    private String CLASS_NAME;

    @Override
    public Page<FileEntity> searchForFile(FileSearchDTO fileSearchDTO) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try{
            StringBuilder sb = new StringBuilder();
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_FILE, "select-file"));
            sb.append(" AND MATCH(f.actual_name) AGAINST(:key in boolean mode)");
            SQLQuery query = session.createSQLQuery(sb.toString());


            if (!DataUtils.isNullOrEmpty(fileSearchDTO.getKeyword())) {
                query.setParameter("key",
                        fileSearchDTO.getKeyword().trim()
                                .replace("\\", "\\\\")
                                .replaceAll("%", "\\%")
                                .replaceAll("_", "\\_")
                );
            }

            query.addScalar("fileId", new LongType());
            query.addScalar("fileName", new StringType());
            query.addScalar("actualName", new StringType());
            query.addScalar("nextVer", new LongType());
            query.addScalar("previousVer", new LongType());
            query.addScalar("nextUrl", new StringType());
            query.addScalar("previousUrl", new StringType());
            query.addScalar("uploadedBy", new StringType());
            query.addScalar("lastModifiedBy", new StringType());
            query.addScalar("lastModifiedDate", new TimestampType());
            query.addScalar("uploadedDate", new TimestampType());
            query.addScalar("fileUrl", new StringType());
            query.setResultTransformer(Transformers.aliasToBean(FileEntity.class));
            int count = 0;


            if (fileSearchDTO.getPage() != null && fileSearchDTO.getPageSize() != null) {
                Pageable pageable = PageBuilder.buildPageable(fileSearchDTO);
                if (pageable != null) {
                    logger.info("\n\tpage number {}, page size {}",pageable.getPageNumber(), pageable.getPageSize());
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }
                List<FileEntity> data = query.list();
                if (data.size() > 0) {
                    count = query.list().size();
                }

                Page<FileEntity> dataPage = new PageImpl<>(data, pageable, count);
                return dataPage;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }

        return null;
    }

    @Override
    public Page<FileDTO> searchData(FileSearchDTO fileSearchDTO) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try{
            logger.info("create sql query");
            StringBuilder sb = new StringBuilder();
            boolean hasKeyword = !DataUtils.isNullOrEmpty(fileSearchDTO.getKeyword());
            boolean hasProjectName = !DataUtils.isNullOrEmpty(fileSearchDTO.getProjectName());
            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_FILE, "select-file-join-project"));

//            if(hasKeyword) sb.append(" AND MATCH(f.actual_name) AGAINST(:key in boolean mode)");
//            if(hasProjectName) sb.append(" AND MATCH(p.project_name) AGAINST(:prj in boolean mode)");
            if(hasKeyword) sb.append(" AND f.actual_name like :key");
            if(hasProjectName) sb.append(" AND p.project_name like :prj");
            SQLQuery query = session.createSQLQuery(sb.toString());
//"%"+  +"%"
            if (hasKeyword)  {
                query.setParameter("key", "%"+ DataUtils.escapeSpecialChars(fileSearchDTO.getKeyword())+"%");
            }

            if (hasProjectName) {
                query.setParameter("prj", "%"+ DataUtils.escapeSpecialChars(fileSearchDTO.getProjectName())+"%");
            }

            logger.info("get result");
            query.addScalar("fileId", new LongType());
            query.addScalar("actualName", new StringType());
            query.addScalar("nextVer", new LongType());
            query.addScalar("previousVer", new LongType());
            query.addScalar("nextUrl", new StringType());
            query.addScalar("previousUrl", new StringType());
            query.addScalar("uploadedBy", new StringType());
            query.addScalar("lastModifiedBy", new StringType());
            query.addScalar("lastModifiedDate", new TimestampType());
            query.addScalar("uploadedDate", new TimestampType());
            query.addScalar("fileUrl", new StringType());
            query.addScalar("projectName", new StringType());
            query.setResultTransformer(Transformers.aliasToBean(FileDTO.class));
            int count = 0;

            logger.info("pagination");
            if (fileSearchDTO.getPage() != null && fileSearchDTO.getPageSize() != null) {
                Pageable pageable = PageBuilder.buildPageable(fileSearchDTO);
                if (pageable != null) {
                    query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
                    query.setMaxResults(pageable.getPageSize());
                }

                List<FileDTO> list = query.list();
                if (list.size() > 0) {
                    count = query.list().size();
                }

                Page<FileDTO> dataPage = new PageImpl<>(list, pageable, count);
                return dataPage;
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (null != session) {
                session.close();
            }
        }

        return null;
    }

    public FileEntity test(){

        try {
            Class.forName(CLASS_NAME);
            Connection conn = DriverManager.getConnection(DB_URL,
                    USER_NAME, PASSWORD);
            // crate statement
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            // get data from table 'student'
            String query = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_FILE, "select-file");
            logger.info(query);
            ResultSet rs = stmt.executeQuery(query);
            // getting the record of 3rd row
            rs.absolute(3);
            System.out.println(rs.getInt(1) + "  " + rs.getString(2)
                    + "  " + rs.getString(3));
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }

        return null;
    }

//    @Override
//    public FileEntity getFileByFileIdAndTeamId(FileShareDTO fileShareDTO) {
//        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        FileEntity fileEntity = null;
//        try{
//            StringBuilder sb = new StringBuilder();
//            sb.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_FILE, "select-file-join-project-team"));
//            sb.append(" and f.file_id = :f_id");
//            sb.append(" and t.team_id = :t_id");
//            SQLQuery query = session.createSQLQuery(sb.toString());
//
//            query.setParameter("f_id",
//                    fileShareDTO.getFileId()
//            );
//            query.setParameter("t_id",
//                    fileShareDTO.getTeamId()
//            );
//
//            query.addScalar("file_id", new LongType());
//            query.addScalar("file_name", new StringType());
//            query.addScalar("actual_name", new StringType());
//            query.addScalar("file_url", new StringType());
//            query.addScalar("uploaded_by", new StringType());
//            query.addScalar("uploaded_date", new TimestampType());
//            query.addScalar("last_modified_by", new StringType());
//            query.addScalar("last_modified_date", new TimestampType());
//            query.addScalar("previous_ver", new LongType());
//            query.addScalar("previous_url", new StringType());
//            query.addScalar("next_ver", new LongType());
//            query.addScalar("next_url", new StringType());
//            query.addScalar("project_id", new LongType());
//            Object[] result = (Object[]) query.uniqueResult();
//            fileEntity = convertObjToFileEntity(result);
//            return fileEntity;
//
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        } finally {
//            if (null != session) {
//                session.close();
//            }
//        }
//
//        return null;
//    }

    public FileEntity convertObjToFileEntity(Object[] inp){
        FileEntity fileEntity = new FileEntity();
        if (inp != null) {
            fileEntity.setFileId((Long) inp[0]);
            fileEntity.setFileName((String) inp[1]);
            fileEntity.setActualName((String) inp[2]);
            fileEntity.setFileUrl((String) inp[3]);
            fileEntity.setUploadedBy((String) inp[4]);
            fileEntity.setUploadedDate((Timestamp) inp[5]);
            fileEntity.setLastModifiedBy((String) inp[6]);
            fileEntity.setLastModifiedDate((Timestamp) inp[7]);
            fileEntity.setPreviousVer((Long) inp[8]);
            fileEntity.setPreviousUrl((String) inp[9]);
            fileEntity.setNextVer((Long) inp[10]);
            fileEntity.setNextUrl((String) inp[11]);
            Long projectId = (Long) inp[12];
            logger.info("\na\t"+projectRepository.getProjectEntityByProjectId(projectId).toString());
//            fileEntity.setProject(projectRepository.getProjectEntityByProjectId(projectId));
        }
        return fileEntity;
    }

}
