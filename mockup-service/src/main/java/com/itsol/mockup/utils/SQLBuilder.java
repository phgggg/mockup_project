package com.itsol.mockup.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author anhvd_itsol
 */
public class SQLBuilder {
    //Folder
    public static final String SQL_MODULE_USERS = "users";
    public static final String SQL_MODULE_FILE = "file";
    public static final String SQL_MODULE_SUBTASK = "subtask";
    //File
    public static final String SQL_FILE_FROM_SUBTASK = "from-subtask";
    public static final String SQL_FILE_FROM_SUBTASK_MONTH = "from-subtask-month";
    public static final String SQL_FILE_SELECT_ESTIMATED_HOURS = "select-estimated_hours";
    public static final String SQL_FILE_SELECT_HOURS_SPENT = "select-hours_spent";
    public static String getSqlQueryById(String module,
                                         String queryId) {
        File folder = null;
        try {
            folder = new ClassPathResource(
                    "sql" + File.separator + module + File.separator + queryId + ".sql").getFile();
            // Read file
            if (folder.isFile()) {
                String sql = new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath())));
//                System.out.println("\n aaaaaaaaaa"+sql+"\naaaaaaaaa");
                return sql;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
