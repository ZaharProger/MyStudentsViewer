package Programme.ProgrammeLogic;

import Programme.DataBaseModel.Group;
import Programme.DataBaseModel.Student;
import org.sqlite.JDBC;
import java.util.ArrayList;
import java.sql.*;

//Менеджер по работе с БД (логика работы с БД)
public class DataBaseManager {
    private static final String CONNECTION_ADDRESS = "jdbc:sqlite:C:./students_database.db";
    private static DataBaseManager manager = null;
    private Connection connection;

    private DataBaseManager() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        connection = DriverManager.getConnection(CONNECTION_ADDRESS);
    }

    public static synchronized ProgrammeResult<DataBaseManager> openDataBase() throws SQLException{
        if (manager == null)
            manager = new DataBaseManager();

        return new ProgrammeResult<>("База данных успешно открыта!", true, manager);
    }

    //Включение/Выключение шапки таблицы
    public String getTableHead(String tableName) throws SQLException{
        String tableHead = "|    ";
        try (Statement statement = connection.createStatement()){
            ResultSetMetaData data = statement.executeQuery(String.format("SELECT * FROM %s;", tableName)).getMetaData();
            for (int columnNumber = 1; columnNumber <= data.getColumnCount(); ++columnNumber){
                tableHead += String.format("%s    |", data.getColumnName(columnNumber));
                tableHead += (columnNumber != data.getColumnCount())? "    " : '\n';
            }
            int headLength = tableHead.length();
            for (int i = 0; i < headLength; ++i){
                tableHead += "-";
            }
        }

        return tableHead;
    }

    public ArrayList<Student> readStudentsData() throws SQLException{
        ArrayList<Student> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet data = statement.executeQuery("SELECT students.id, student_name, student_surname, student_birthday, groups.group_name" +
                                                        " FROM students INNER JOIN groups ON students.student_group = groups.id;");
            while (data.next()){
                list.add(new Student(data.getInt("id"), data.getString("student_name"), data.getString("student_surname"),
                                     data.getString("student_birthday"), data.getString("group_name")));
            }
        }

        return list;
    }

    public ArrayList<Group> readGroupsData() throws SQLException{
        ArrayList<Group> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet data = statement.executeQuery("SELECT * FROM groups;");
            while (data.next()){
                list.add(new Group(data.getInt("id"), data.getString("group_name")));
            }
        }

        return list;
    }

    public ArrayList<Student> addStudent() throws SQLException{

    }

    public void closeDataBase() throws SQLException{
        connection.close();
    }

}
