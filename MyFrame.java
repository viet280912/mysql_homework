import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyFrame extends JFrame {
    List<Student> studentList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField classField;
    private JButton btn_add;
    private JButton btn_del;
    private JButton btn_update;
    private JButton btn_search;
    private JButton btn_refresh;

    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void showData(){
        studentList = StudentModify.findAll();
        tableModel.setRowCount(0);

        for (Student student : studentList) {
            tableModel.addRow(new Object[]{tableModel.getRowCount() + 1,student.getName(),student.getAge(),student.getClassName()});
        }
    }
    public MyFrame(){

        setTitle("Student Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Age");
        tableModel.addColumn("Class");

        studentTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        classField = new JTextField();
        btn_add = new JButton("Add");
        btn_del = new JButton("Delete");
        btn_search = new JButton("Search");
        btn_update = new JButton("Update");
        btn_refresh = new JButton("Refresh");

        JPanel inputPanel = new JPanel(new GridLayout(4,2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Class:"));
        inputPanel.add(classField);

        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        buttonPanel.add(btn_add);
        buttonPanel.add(btn_update);
        buttonPanel.add(btn_del);
        buttonPanel.add(btn_refresh);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(btn_search, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        getContentPane().add(inputPanel,BorderLayout.NORTH);
        getContentPane().add(buttonPanel,BorderLayout.SOUTH);
        getContentPane().add(searchPanel,BorderLayout.WEST);

        btn_refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setText("");
                ageField.setText("");
                classField.setText("");
            }
        });


                btn_add.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        int age = Integer.parseInt(ageField.getText());
                        String Class = classField.getText();

                        Student student = new Student(age, name, Class);
                        StudentModify.insert(student);

                        JOptionPane.showMessageDialog(null, "Add successful");
                        showData();
                        nameField.setText("");
                        ageField.setText("");
                        classField.setText("");
                    }
                });

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0){
//                    idField.setText(studentTable.getValueAt(selectedRow,0) + "");
                    nameField.setText(studentTable.getValueAt(selectedRow,1) + "");
                    ageField.setText(studentTable.getValueAt(selectedRow,2) + "");
                    classField.setText(studentTable.getValueAt(selectedRow,3) + "");
                }
            }
        });

        btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student student = new Student();
                student.setName(nameField.getText());
                student.setAge(Integer.parseInt(ageField.getText()));
                student.setClass(classField.getText());

                StudentModify.update(student);
                JOptionPane.showMessageDialog(null,"Updated!");
                showData();
            }
        });

        btn_del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Student student = studentList.get(selectedRow);
                    int option = JOptionPane.showConfirmDialog(btn_del, "Do you want to delete this ?");

                    if (option == 0) {
                        StudentModify.delete(student.getID());
                        JOptionPane.showMessageDialog(null, "Deleted !");
                        showData();
                    }
                }
            }
        });
        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(this,"Enter fullname to search");
                if (input != null && input.length() > 0){
                    studentList = StudentModify.findByName(input);

                    tableModel.setRowCount(0);
                    studentList.forEach(student -> {
                        tableModel.addRow(new Object[]{
                                tableModel.getRowCount() + 1, student.getName(), student.getAge(),student.getClassName()
                        });
                    });
                } else {
                    showData();
                }
            }
        });
    }

}