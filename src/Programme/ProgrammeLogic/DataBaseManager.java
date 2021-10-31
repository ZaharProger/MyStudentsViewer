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

    public static synchronized ProgrammeResult<DataBaseManager> openDataBase(){
        ProgrammeResult<DataBaseManager> result = new ProgrammeResult<>();
        try{
            if (manager == null)
                manager = new DataBaseManager();
            result.setMessage("База данных успешно открыта!");
            result.setSuccessStatus(true);
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Неверный адрес базы данных!");
            result.setSuccessStatus(false);
        }
        result.setValue(manager);

        return result;
    }

    public ProgrammeResult<TableData<Student>> readStudentsData(){
        ArrayList<Student> list = new ArrayList<>();
        ProgrammeResult<TableData<Student>> result = new ProgrammeResult<>("Чтение успешно завершено!", true, new TableData<>());
        try (Statement statement = connection.createStatement()) {
            ResultSet data = statement.executeQuery("SELECT students.id, student_name, student_surname, student_birthday, group_name " +
                                                        "FROM students INNER JOIN groups ON students.student_group = groups.id;");
            result.getValue().createTableHead(data.getMetaData());
            while (data.next()){
                list.add(new Student(data.getInt("id"), data.getString("student_name"), data.getString("student_surname"),
                                     data.getString("student_birthday"), data.getString("group_name")));
            }
            if (list.isEmpty()){
                System.out.println(2);
                result.setMessage("База данных пуста!");
                result.setSuccessStatus(false);
            }
            else{
                result.getValue().setData(list);
            }
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Ошибка при чтении базы данных!");
            result.setSuccessStatus(false);
        }

        return result;
    }

    public ProgrammeResult<TableData<Group>> readGroupsData(){
        ArrayList<Group> list = new ArrayList<>();
        ProgrammeResult<TableData<Group>> result = new ProgrammeResult<>("Чтение успешно завершено!", true, new TableData<>());
        try (Statement statement = connection.createStatement()) {
            ResultSet data = statement.executeQuery("SELECT * FROM groups;");
            result.getValue().createTableHead(data.getMetaData());
            while (data.next()){
                list.add(new Group(data.getInt("id"), data.getString("group_name")));
            }
            if (list.isEmpty()){
                result.setMessage("База данных пуста!");
                result.setSuccessStatus(false);
            }
            else{
                result.getValue().setData(list);
            }
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Ошибка при чтении базы данных!");
            result.setSuccessStatus(false);
        }

        return result;
    }

    public ProgrammeResult<Integer> addStudent(Student student){
        ProgrammeResult<Integer> result = new ProgrammeResult<>("Студент успешно добавлен!", true, 0);
        int groupID = 0;
        try (Statement statement = connection.createStatement()){
            ResultSet data = statement.executeQuery(String.format("SELECT groups.id FROM groups WHERE groups.group_name = '%s' OR groups.id = '%s';",
                                                        student.getGroup(), student.getGroup()));
            while (data.next()){
                groupID = data.getInt("id");
            }
            if (groupID != 0){
                statement.execute(String.format("INSERT INTO students ('id', 'student_name', 'student_surname', 'student_birthday', 'student_group')" +
                        " VALUES (%d, '%s', '%s', '%s', %d);", student.getId(), student.getName(), student.getSurname(), student.getBirthday(), groupID));
                data = statement.executeQuery(String.format("SELECT id FROM students WHERE id = %s", student.getId()));
                while (data.next()){
                    result.setValue(data.getInt("id"));
                }
            }
            else{
                result.setMessage(String.format("Группы %s не существует в таблице групп!", student.getGroup()));
                result.setSuccessStatus(false);
            }
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Ошибка при добавлении записи!");
            result.setSuccessStatus(false);
        }

        return result;
    }

    public ProgrammeResult<Integer> addGroup(Group group){
        ProgrammeResult<Integer> result = new ProgrammeResult<>("Группа успешно добавлена!", true, 0);
        try (Statement statement = connection.createStatement()){
            statement.execute(String.format("INSERT INTO groups ('id', 'group_name') VALUES (%d, '%s');", group.getId(), group.getName()));
            ResultSet data = statement.executeQuery(String.format("SELECT id FROM groups WHERE group_name = '%s'", group.getName()));
            while (data.next()){
                result.setValue(data.getInt("id"));
            }
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Ошибка при добавлении записи!");
            result.setSuccessStatus(false);
        }

        return result;
    }

    public ProgrammeResult<String> removeStudent(int id){
        ProgrammeResult<String> result = new ProgrammeResult<>();
        try (Statement statement = connection.createStatement()){
            ResultSet data = statement.executeQuery(String.format("SELECT id FROM students WHERE id = %d;", id));
            if (data.next()){
                statement.execute(String.format("DELETE FROM students WHERE id = %d;", id));
                result.setMessage("Информация о студенте успешно удалена!");
                result.setSuccessStatus(true);
            }
            else{
                result.setMessage("Информация о студенте не найдена!");
                result.setSuccessStatus(false);
            }
            result.setValue("");
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return result;
    }

    public ProgrammeResult<String> removeGroup(int id){
        ProgrammeResult<String> result = new ProgrammeResult<>();
        try (Statement statement = connection.createStatement()){
            ResultSet data = statement.executeQuery(String.format("SELECT id FROM groups WHERE id = %d;", id));
            if (data.next()){
                statement.execute(String.format("DELETE FROM groups WHERE id = %d;", id));
                statement.execute(String.format("UPDATE students SET group_name = 0 WHERE group_name = %d;", id));
                result.setMessage("""
                        Информация о группе успешно удалена!
                        Студенты принадлежащие данной группе скрыты
                        Для их отображения определите их в существующую группу!""");
                result.setSuccessStatus(true);
            }
            else{
                result.setMessage("Информация о группе не найдена!");
                result.setSuccessStatus(false);
            }
            result.setValue("");
        }
        catch(SQLException exception){
            System.out.println(exception.getMessage());
        }

        return result;
    }

    public ProgrammeResult<TableData<Student>> readHiddenData(){
        ProgrammeResult<TableData<Student>> result = new ProgrammeResult<>("Чтение успешно завершено!", true, new TableData<>());
        ArrayList<Student> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet data = statement.executeQuery("SELECT students.id, student_name, student_surname, student_birthday, student_group " +
                    "FROM students WHERE student_group = 0;");
            result.getValue().createTableHead(data.getMetaData());
            while (data.next()){
                list.add(new Student(data.getInt("id"), data.getString("student_name"), data.getString("student_surname"),
                        data.getString("student_birthday"), "Не указано"));
            }
            if (list.isEmpty()){
                System.out.println(2);
                result.setMessage("В базе данных нет скрытых записей!");
                result.setSuccessStatus(false);
            }
            else{
                result.getValue().setData(list);
            }
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Ошибка при чтении базы данных!");
            result.setSuccessStatus(false);
        }

        return result;
    }

    public ProgrammeResult<String> closeDataBase(){
        ProgrammeResult<String> result = new ProgrammeResult<>("База данных успешно закрыта!", true, "");
        try{
            connection.close();
        }
        catch (SQLException exception){
            System.out.println(exception.getMessage());
            result.setMessage("Не найдено открытой базы данных!");
            result.setSuccessStatus(false);
        }

        return result;
    }

    //Метод для проверки существования БД
    public void checkIfOpen() throws NullPointerException{
        manager.toString();
    }

}
