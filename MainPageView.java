

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.IOException;

public class MainPageView extends JFrame implements ActionListener {
    private JPanel jp;
    private JLabel label;
    private JTextField tName, tQuantity, tPrice, tCuisine, tExpiry;
    private JTextArea cartTextArea;
    private JButton addbutton, orderbutton, historybutton;
    private FoodItem[] cartItems = new FoodItem[100];
    private int count = 0;
    private double totalCost = 0.0;

    public MainPageView() {
        super("Food Management System");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jp = new JPanel();
        jp.setLayout(null);

        label = new JLabel("Food name:");
        label.setBounds(50, 50, 150, 20);
        jp.add(label);

        tName = new JTextField();
        tName.setBounds(140, 50, 150, 30);
        jp.add(tName);

        label = new JLabel("Food quantity:");
        label.setBounds(50, 100, 150, 20);
        jp.add(label);

        tQuantity = new JTextField();
        tQuantity.setBounds(140, 100, 150, 30);
        jp.add(tQuantity);

        label = new JLabel("Price per item:");
        label.setBounds(50, 150, 150, 20);
        jp.add(label);

        tPrice = new JTextField();
        tPrice.setBounds(140, 150, 150, 30);
        jp.add(tPrice);

        label = new JLabel("Cuisine:");
        label.setBounds(50, 200, 150, 20);
        jp.add(label);

        tCuisine = new JTextField();
        tCuisine.setBounds(140, 200, 150, 30);
        jp.add(tCuisine);

        label = new JLabel("Expiry date:");
        label.setBounds(50, 250, 150, 20);
        jp.add(label);

        tExpiry = new JTextField();
        tExpiry.setBounds(140, 250, 150, 30);
        jp.add(tExpiry);

        label = new JLabel("Cart:");
        label.setBounds(650, 20, 40, 20);
        jp.add(label);

        cartTextArea = new JTextArea();
        cartTextArea.setBounds(450, 50, 450, 350);
        cartTextArea.setEditable(false);
        jp.add(cartTextArea);

        addbutton = new JButton("Add to cart");
        addbutton.setBounds(50, 350, 110, 30);
        jp.add(addbutton);

        orderbutton = new JButton("Order now");
        orderbutton.setBounds(170, 350, 110, 30);
        jp.add(orderbutton);

        historybutton = new JButton("Order history");
        historybutton.setBounds(290, 350, 130, 30);
        jp.add(historybutton);

        addbutton.addActionListener(this);
        orderbutton.addActionListener(this);
        historybutton.addActionListener(this);

        add(jp);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addbutton) {
            String name = tName.getText().trim();
            String quantityStr = tQuantity.getText().trim();
            String priceStr = tPrice.getText().trim();
            String cuisine = tCuisine.getText().trim();
            String expiry = tExpiry.getText().trim();

            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter name, quantity and price.",
                        "Blank", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);

                if (count >= cartItems.length) {
                    JOptionPane.showMessageDialog(this, "Cart is full",
                            "Cart Full", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FoodItem item = new FoodItem(name, quantity, price, cuisine, expiry);
                cartItems[count++] = item;
                totalCost += item.getCost();

                StringBuilder cartText = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    cartText.append(cartItems[i].toString()).append("\n");
                }
                cartText.append("\nTotal: ").append(totalCost).append(" tk");
                cartTextArea.setText(cartText.toString());

                tName.setText("");
                tQuantity.setText("");
                tPrice.setText("");
                tCuisine.setText("");
                tExpiry.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (e.getSource() == orderbutton) {
            if (count == 0) {
                JOptionPane.showMessageDialog(this,
                        "Cart is empty, please add items",
                        "Empty Cart",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                OrderRepository.saveOrder(cartItems, count, totalCost);
                JOptionPane.showMessageDialog(this,
                        "Order saved!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                for (int i = 0; i < count; i++) {
                    cartItems[i] = null;
                }
                count = 0;
                totalCost = 0.0;
                cartTextArea.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to save order: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } 
       
        else if (e.getSource() == historybutton) {
            this.setVisible(false);
            new OrderHistoryView(this);
        }
    }
}
