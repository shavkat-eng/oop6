import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Принцип единственной ответственности (SRP)
// Класс Teacher отвечает только за информацию о преподавателе.
class Teacher {
    private String name;
    private int id;

    public Teacher(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Teacher{id=" + id + ", name='" + name + "'}";
    }
}

// Принцип единственной ответственности (SRP)
// Класс Student отвечает только за информацию о студенте.
class Student {
    private String name;
    private int id;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "'}";
    }
}

// Принцип единственной ответственности (SRP)
// Класс StudyGroup отвечает только за информацию об учебной группе.
class StudyGroup {
    private Teacher teacher;
    private List<Student> students;

    public StudyGroup(Teacher teacher, List<Student> students) {
        this.teacher = teacher;
        this.students = students;
    }

    @Override
    public String toString() {
        return "StudyGroup{teacher=" + teacher + ", students=" + students + "}";
    }
}

// Принцип единственной ответственности (SRP)
// Класс StudyGroupService отвечает только за создание учебной группы.
class StudyGroupService {
    public StudyGroup formStudyGroup(Teacher teacher, List<Student> students) {
        return new StudyGroup(teacher, students);
    }
}

// Принцип единственной ответственности (SRP)
// Класс TeacherService отвечает только за управление преподавателями.
class TeacherService implements ITeacherService {
    private List<Teacher> teachers = new ArrayList<>();
    private int nextId = 1;

    public void createTeacher(String name) {
        Teacher teacher = new Teacher(name, nextId++);
        teachers.add(teacher);
    }

    public void editTeacher(int id, String newName) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                teacher.setName(newName);
                return;
            }
        }
    }

    public List<Teacher> getAllTeachers() {
        return teachers;
    }

    public Teacher getTeacherById(int id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId() == id) {
                return teacher;
            }
        }
        return null;
    }
}

// Принцип единственной ответственности (SRP)
// Класс TeacherView отвечает только за отображение информации о преподавателях.
class TeacherView {
    public void displayTeachers(List<Teacher> teachers) {
        for (Teacher teacher : teachers) {
            System.out.println(teacher);
        }
    }
}

// Принцип единственной ответственности (SRP)
// Класс StudentService отвечает только за управление студентами.
class StudentService implements IStudentService {
    private List<Student> students = new ArrayList<>();
    private int nextId = 1;

    public void createStudent(String name) {
        Student student = new Student(name, nextId++);
        students.add(student);
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
}

// Принцип единственной ответственности (SRP)
// Класс StudentView отвечает только за отображение информации о студентах.
class StudentView {
    public void displayStudents(List<Student> students) {
        for (Student student : students) {
            System.out.println(student);
        }
    }
}

// Принцип разделения интерфейса (ISP)
// Интерфейс для управления преподавателями.
interface ITeacherService {
    void createTeacher(String name);
    void editTeacher(int id, String newName);
    List<Teacher> getAllTeachers();
    Teacher getTeacherById(int id);
}

// Принцип разделения интерфейса (ISP)
// Интерфейс для управления студентами.
interface IStudentService {
    void createStudent(String name);
    List<Student> getAllStudents();
    Student getStudentById(int id);
}

// Класс TeacherController применяет Dependency Inversion Principle (DIP),
// используя интерфейсы ITeacherService и IStudentService.
class TeacherController {
    private ITeacherService teacherService;
    private TeacherView teacherView;
    private IStudentService studentService;
    private Scanner scanner = new Scanner(System.in);

    public TeacherController(ITeacherService teacherService, IStudentService studentService) {
        this.teacherService = teacherService;
        this.teacherView = new TeacherView();
        this.studentService = studentService;
    }

    public void run() {
        while (true) {
            System.out.println("1. Create Teacher");
            System.out.println("2. Edit Teacher");
            System.out.println("3. Display All Teachers");
            System.out.println("4. Create Study Group");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    createTeacher();
                    break;
                case 2:
                    editTeacher();
                    break;
                case 3:
                    displayAllTeachers();
                    break;
                case 4:
                    createStudyGroup();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createTeacher() {
        System.out.print("Enter teacher name: ");
        String name = scanner.nextLine();
        teacherService.createTeacher(name);
    }

    private void editTeacher() {
        System.out.print("Enter teacher ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // consume newline
        System.out.print("Enter new teacher name: ");
        String newName = scanner.nextLine();
        teacherService.editTeacher(id, newName);
    }

    private void displayAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        teacherView.displayTeachers(teachers);
    }

    private void createStudyGroup() {
        System.out.print("Enter teacher ID: ");
        int teacherId = scanner.nextInt();
        scanner.nextLine();  // consume newline

        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher == null) {
            System.out.println("Teacher not found.");
            return;
        }

        List<Student> students = new ArrayList<>();
        while (true) {
            System.out.print("Enter student ID (or -1 to finish): ");
            int studentId = scanner.nextInt();
            scanner.nextLine();  // consume newline

            if (studentId == -1) {
                break;
            }

            Student student = studentService.getStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found.");
            } else {
                students.add(student);
            }
        }

        StudyGroupService studyGroupService = new StudyGroupService();
        StudyGroup studyGroup = studyGroupService.formStudyGroup(teacher, students);
        System.out.println("Created Study Group: " + studyGroup);
    }
}

// Класс Main для тестирования
public class Main {
    public static void main(String[] args) {
        ITeacherService teacherService = new TeacherService();
        IStudentService studentService = new StudentService();
        TeacherController controller = new TeacherController(teacherService, studentService);

        // Создание нескольких студентов и преподавателей для тестирования
        studentService.createStudent("Student1");
        studentService.createStudent("Student2");
        studentService.createStudent("Student3");
        teacherService.createTeacher("Teacher1");
        teacherService.createTeacher("Teacher2");

        controller.run();
    }
}
