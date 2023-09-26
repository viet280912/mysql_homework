import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentModify {
    public static List<Student> findAll() {
        List<Student> studentList = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");

            String query = "select * from student_manager";
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String className = resultSet.getString("class");
                studentList.add(new Student(id,age,name,className));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return studentList;
    }
    public static void insert(Student student){
        List<Student> studentList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");

            String query = "insert into student_manager(id,name,age,class) values (?,?,?,?)";
            statement = connection.prepareCall(query);

            statement.setInt(1,student.getID());
            statement.setString(2,student.getName());
            statement.setInt(3,student.getAge());
            statement.setString(4,student.getClassName());

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void update(Student student){
        List<Student> studentList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");

            String query = "update student_manager set name=?,age=?,class=? where name='"+student.getName()+"'";
            statement = connection.prepareCall(query);

            statement.setString(1,student.getName());
            statement.setInt(2,student.getAge());
            statement.setString(3,student.getClassName());


            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static void delete(int id){
        List<Student> studentList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");

            String query = "delete from student_manager where id=?";
            statement = connection.prepareCall(query);

            statement.setInt(1,id);

            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static List<Student> findByName(String name) {
        List<Student> studentList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/test","root","");

            String query = "select * from student_manager where name like ?";
            statement = connection.prepareCall(query);
            statement.setString(1,"%"+name+"%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getInt("age"),
                        resultSet.getString("name"),
                        resultSet.getString("class"));
                studentList.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return studentList;
    }
}
