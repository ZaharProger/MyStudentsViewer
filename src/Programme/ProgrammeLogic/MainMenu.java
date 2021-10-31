package Programme.ProgrammeLogic;

import Programme.DataBaseModel.Group;
import Programme.DataBaseModel.Student;

import java.util.Scanner;

//Главное меню программы
public class MainMenu {
    public static byte runMenu(){
        Scanner in = new Scanner(System.in);
        byte isFinished = 0;
        DataBaseManager manager = null;
        while (isFinished == 0){
            try {
                System.out.print("""
                                        
                        MY STUDENTS VIEWER - идеальное решение для получения информации о ваших студентах
                        Введите номер команды:
                        1. Открытие базы данных студентов
                        2. Вывод информации о студентах
                        3. Вывод информации о группах
                        4. Добавление студента
                        5. Добавление группы
                        0. Выход
                        """);
                System.out.print("Введите здесь: ");
                String choice = in.nextLine();
                if (Byte.parseByte(choice) == 1) {
                    ProgrammeResult<DataBaseManager> openingResult = DataBaseManager.openDataBase();
                    manager = openingResult.getValue();
                    System.out.println(openingResult.getMessage());
                }
                else if (Byte.parseByte(choice) == 2) {
                    manager.checkIfOpen();
                    ProgrammeResult<TableData<Student>> readingResult = manager.readStudentsData();
                    if (readingResult.getSuccessStatus()){
                        System.out.println(readingResult.getValue().getTableHead());
                        for (Student student : readingResult.getValue().getData()) {
                            System.out.println(student.toString());
                        }
                    }
                    else{
                        System.out.println(readingResult.getMessage());
                    }
                }
                else if (Byte.parseByte(choice) == 3) {
                    manager.checkIfOpen();
                    ProgrammeResult<TableData<Group>> readingResult = manager.readGroupsData();
                    if (readingResult.getSuccessStatus()){
                        System.out.println(readingResult.getValue().getTableHead());
                        for (Group group : readingResult.getValue().getData()) {
                            System.out.println(group.toString());
                        }
                    }
                    else{
                        System.out.println(readingResult.getMessage());
                    }
                }
                else if (Byte.parseByte(choice) == 4){
                    manager.checkIfOpen();
                    String userData;
                    System.out.println("Заполните информацию о студенте!");
                    Student student = new Student();
                    System.out.print("ID: ");
                    userData = in.nextLine();
                    student.setId(Integer.parseInt(userData));
                    System.out.print("Имя: ");
                    userData = in.nextLine();
                    student.setName(userData);
                    System.out.print("Фамилия: ");
                    userData = in.nextLine();
                    student.setSurname(userData);
                    System.out.print("Дата рождения (дд мм гггг через пробел или точку): ");
                    userData = in.nextLine();
                    ProgrammeResult<String> analyzerResult = new DateAnalyzer().analyze(userData);
                    if (analyzerResult.getSuccessStatus()) {
                        student.setBirthday(analyzerResult.getValue());
                        System.out.print("Группа: ");
                        userData = in.nextLine();
                        student.setGroup(userData);
                        ProgrammeResult<Integer> insertionResult = manager.addStudent(student);
                        if (insertionResult.getSuccessStatus()){
                            System.out.printf("%s Его id: %d%n", insertionResult.getMessage(), insertionResult.getValue());
                        }
                        else {
                            System.out.println(insertionResult.getMessage());
                        }
                    }
                    else{
                        System.out.println(analyzerResult.getMessage());
                    }
                }
                else if (Byte.parseByte(choice) == 5){
                    manager.checkIfOpen();
                    String userData;
                    System.out.println("Заполните информацию о группе!");
                    Group group = new Group();
                    System.out.print("ID: ");
                    userData = in.nextLine();
                    group.setId(Integer.parseInt(userData));
                    System.out.print("Название группы: ");
                    userData = in.nextLine();
                    group.setName(userData);
                    ProgrammeResult<Integer> insertionResult = manager.addGroup(group);
                    if (insertionResult.getSuccessStatus()){
                        System.out.printf("%s Её id: %d%n", insertionResult.getMessage(), insertionResult.getValue());
                    }
                    else {
                        System.out.println(insertionResult.getMessage());
                    }
                }
                else if (Byte.parseByte(choice) == 0) {
                    if (manager != null){
                        System.out.println(manager.closeDataBase().getMessage());
                    }
                    isFinished = 1;
                }
                else {
                    System.out.println("Несуществующая команда!");
                }
            }
            catch (NumberFormatException exception){
                System.out.println("Необходимо ввести числовое значение!");
            }
            catch (NullPointerException exception){
                System.out.println("База данных не открыта!");
            }
        }

        return isFinished;
    }
}
