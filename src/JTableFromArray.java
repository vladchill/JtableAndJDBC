import javax.swing.*;
import java.awt.*;

public class JTableFromArray {

    public static void createFrame(){
        Object[] columnHeaders = {"Фамилия", "Имя", "Отчество", "email"}; // массив с названиями столбцов

        //Массив с данными для таблицы
        Object[][] tableData = {
                {"Иванов", "Иван", "Иванович", "test1@mail.ru"},
                {"Петров", "Олег", "Петрович", "test2@mail.ru"},
                {"Алексеев", "Алексей", "Алексеевич", "test3@mail.ru"},
                {"Сидоров", "Сидор", "Сидорович", "test4@mail.ru"},
                {"Олегов", "Олег", "Олегович", "test5@mail.ru"}
        };


        JFrame frame = new JFrame("Пример данных из массива");

        frame.getContentPane().setLayout(new FlowLayout());

        frame.setSize(400, 200);
//        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JTable jtableFIO = new JTable(tableData, columnHeaders);

        jtableFIO.setRowHeight(30);

//        jtableFIO.setDefaultRenderer(Object.class, new MyTableRenderer());
//
//        jtableFIO.addMouseListener(new MouseAdapter() {
//
//            public void mouseClicked(MouseEvent e) {
//                if (e.getClickCount() == 1) {
//                    JTable target = (JTable) e.getSource();
//                    String value = target.getValueAt(target.getSelectedRow(), target.getSelectedColumn()).toString();
//
//                    try {
//                        if (value.contains("@")) {
//                            Desktop.getDesktop().mail(new URI("mailto:" + value + "?SUBJECT=Служебное%20письмо&body=Текст%20письма"));
//                        }
//
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        jtableFIO.addMouseMotionListener(new MouseMotionAdapter() {
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                JTable target = (JTable) e.getSource();
//                if (target.columnAtPoint(e.getPoint())==3){
//                    target.setCursor(new Cursor(Cursor.HAND_CURSOR));
//                }else{
//                    target.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//                }
//            }
//        });


        JScrollPane scroll = new JScrollPane(jtableFIO);

        // размеры прокручиваемой области
        jtableFIO.setPreferredScrollableViewportSize(new Dimension(400, 200));

        frame.getContentPane().add(scroll);

        frame.setVisible(true);
    }
}
