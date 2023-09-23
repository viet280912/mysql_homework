import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MyFrame extends JFrame {
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
    private JTextField searchField;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public void showData(){
        try{
            Object[] columnTitle = {"ID","Name","Age","Class"};
            tableModel = new DefaultTableModel(null,columnTitle);
            studentTable.setModel(tableModel);

            Connection connection = Connector.getConnection();
            Statement statement = connection.createStatement();
            tableModel.getDataVector().removeAllElements();

            resultSet = statement.executeQuery("select * from student_manager");
            while (resultSet.next()){
                Object[] data = {
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("age"),
                        resultSet.getString("class")
                };
                tableModel.addRow(data);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
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
        searchField = new JTextField();
        btn_update = new JButton("Update");

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

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.NORTH);
        searchPanel.add(btn_search, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane,BorderLayout.CENTER);
        getContentPane().add(inputPanel,BorderLayout.NORTH);
        getContentPane().add(buttonPanel,BorderLayout.SOUTH);
        getContentPane().add(searchPanel,BorderLayout.WEST);

        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name,age,Class;
                name = nameField.getText();
                age = ageField.getText();
                Class = classField.getText();

                try {
                    preparedStatement = Connector.getConnection().prepareStatement("insert into student_manager (name,age,class) values (?,?,?)");
                    preparedStatement.setString(1,name);
                    preparedStatement.setString(2,age);
                    preparedStatement.setString(3,Class);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Add data successful !");
                    showData();

                    nameField.setText("");
                    ageField.setText("");
                    classField.setText("");
                }catch (SQLException exception){
                    exception.printStackTrace();
                }
            }
        });
        btn_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btn_del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1 ){
                    String id = studentTable.getValueAt(selectedRow,0).toString();
                    try {
                        preparedStatement = Connector.getConnection().prepareStatement("delete from student_manager where id=?");
                        preparedStatement.setString(1,id);
                        preparedStatement.executeUpdate();

                        JOptionPane.showMessageDialog(null,"Delete completed");
                        showData();

                        nameField.setText("");
                        ageField.setText("");
                        classField.setText("");
                        nameField.requestFocus();
                    } catch (Exception exception){
                        exception.printStackTrace();
                    }
                }
            }
        });
        btn_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}