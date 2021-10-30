package Programme.ProgrammeLogic;

import Programme.DataBaseModel.Group;
import Programme.DataBaseModel.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

//Главное меню программы
public class MainMenu {
    public static byte runMenu(){
        byte isFinished = 0;
        DataBaseManager manager = null;
        while (isFinished == 0){
            try {
                Scanner in = new Scanner(System.in);
                System.out.print("""
                                        
                        MY STUDENTS VIEWER - идеальное решение для получения информации о ваших студентах
                        Введите номер команды:
                        1. Открытие базы данных студентов
                        2. Вывод информации о студентах
                        3. Вывод информации о группах
                        4. Добавление студента
                        5. Добавление группы
                        0. Выход
                        Введите здесь:\040""");
                byte choice = in.nextByte();

                if (choice == 1) {
                    ProgrammeResult<DataBaseManager> openingResult = DataBaseManager.openDataBase();
                    manager = openingResult.getValue();
                    System.out.println(openingResult.getMessage());
                }
                else if (choice == 2) {
                    ArrayList<Student> list = manager.readStudentsData();
                    String tableHead = manager.getTableHead("students");
                    System.out.println(tableHead);
                    for (Student student : list) {
                        System.out.println(student.toString());
                    }
                }
                else if (choice == 3) {
                    ArrayList<Group> list = manager.readGroupsData();
                    String tableHead = manager.getTableHead("groups");
                    System.out.println(tableHead);
                    for (Group group : list) {
                        System.out.println(group.toString());
                    }
                }
                else if (choice == 4){
                    System.out.println("Заполните информацию о студенте!");
                    Student student = new Student();
                    System.out.print("Имя: ");
                    System.out.print("Фамилия: ");
                    System.out.print("Дата рождения: ");
                    System.out.print("Группа: ");
                }
                else if (choice == 0) {
                    if (manager != null)
                        manager.closeDataBase();
                    isFinished = 1;
                }
                else {
                    System.out.println("Несуществующая команда!");
                }
            }
            catch (InputMismatchException exception){
                System.out.println("Необходимо ввести числовое значение!");
            }
            catch (SQLException exception){
                System.out.printf("Ошибка при обращении к базе данных: %s%n", exception.getMessage());
            }
            catch (NullPointerException exception){
                System.out.println("Перед работой откройте базу данных!");
            }
        }

        return isFinished;
    }
}
