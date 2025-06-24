import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OrderHistoryView implements ActionListener {
    private JFrame jf;
    private JPanel jp;
    private JButton backBtn, dltBtn;
    private JTextArea area;
    private JFrame mainFrame;

    public OrderHistoryView(JFrame mainFrame) {
        this.mainFrame = mainFrame;

        jf = new JFrame("Order History");
        jf.setSize(1000, 500);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jp = new JPanel();
        jp.setLayout(null);

        area = new JTextArea();
        area.setBounds(50, 50, 680, 300);
        area.setEditable(false);
        jp.add(area);

        backBtn = new JButton("Back to main");
        backBtn.setBounds(240, 370, 150, 30);
        backBtn.addActionListener(this);
        jp.add(backBtn);

        dltBtn = new JButton("Delete History");
        dltBtn.setBounds(410, 370, 150, 30);
        dltBtn.addActionListener(this);
        jp.add(dltBtn);

        jf.add(jp);

       
        
            String historyText = OrderRepository.loadOrderHistory();
            area.setText(historyText);
       
        jf.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            jf.setVisible(false);
            mainFrame.setVisible(true);
        } else if (e.getSource() == dltBtn) {
       int confirm = JOptionPane.showConfirmDialog(jf,"Delete order history?","Confirm Delete",JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    OrderRepository.deleteOrderHistory();
                    area.setText("Order history deletd");
                    JOptionPane.showMessageDialog(jf, "Order history deleted successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(jf,
                            "Failed to delete order history: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(jf, "Delete cancelled.");
            }
        }
    }
}
