import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class Window extends JFrame {

    // Window
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 300;

    JPanel customerPanel;
    JLabel customerJTFLabel;
    JTextField customerJTF;
    JButton customerBtn;
    JLabel customerLabel;

    JPanel categoryPanel;
    JLabel categoryComboBoxLabel;
    JComboBox<Category> categoryComboBox;
    JLabel bookJTFLabel;
    JTextField bookJTF;
    JButton bookBtn;
    JLabel bookChooseLabel = new JLabel("Choose a book");
    JComboBox<Book> bookComboBox;

    JPanel purchasePanel;
    JButton priceBtn = new JButton("Min Price");
    JLabel priceLabel = new JLabel();
    JLabel quantityLabel = new JLabel("Number of books:");
    JTextField quantityJTF = new JTextField();
    JButton calculateBtn = new JButton("calculate");
//    JLabel countLabel = new JLabel("You wants to purchase the book/books?");
    JLabel countLabel = new JLabel();
    JButton purchaseBtn = new JButton("Purchase");
    JLabel purchaseResultLabel = new JLabel();


    // DB2
    JdbcQueryUtil mJdbcQueryUtil;

    // varibles
    Customer customer;
    Category category;
    Book book;
    Offer offer;
    int quantity;
    float count;

    public Window() {
        mJdbcQueryUtil = JdbcQueryUtil.getInstance();
    }

    public void initUI() {

        this.setTitle("IBM DB2 Application");
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setLayout(new FlowLayout());

        customerPanel = new JPanel();
        customerPanel.setLayout(new GridLayout(2, 1));
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridLayout(1, 3));
        customerJTFLabel = new JLabel("Customer Id:");
        customerJTF = new JTextField();
        customerJTF.setSize(100, 20);
        customerBtn = new JButton("Find");
        customerLabel = new JLabel();
        containerPanel.add(customerJTFLabel);
        containerPanel.add(customerJTF);
        containerPanel.add(customerBtn);
        customerPanel.add(containerPanel);
        customerPanel.add(customerLabel);

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(2,1));
        JPanel containerPanel2 = new JPanel();
        containerPanel2.setLayout(new GridLayout(1, 5));
        categoryComboBoxLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>();
        List<Category> categories = mJdbcQueryUtil.fetchCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
        bookJTFLabel = new JLabel("Book title:");
        bookJTF = new JTextField();
        bookBtn = new JButton("Find Books");
        containerPanel2.add(categoryComboBoxLabel);
        containerPanel2.add(categoryComboBox);
        containerPanel2.add(bookJTFLabel);
        containerPanel2.add(bookJTF);
        containerPanel2.add(bookBtn);
        categoryPanel.add(BorderLayout.NORTH, containerPanel2);
        JPanel containerPanel3 = new JPanel();
        containerPanel3.setLayout(new GridLayout(1, 2));
        containerPanel3.add(bookChooseLabel);
        bookComboBox = new JComboBox<>();
        containerPanel3.add(bookComboBox);
        categoryPanel.add(containerPanel3);

        purchasePanel = new JPanel();
        purchasePanel.setLayout(new GridLayout(3, 1));
        JPanel containerPanel4 = new JPanel();
        JPanel containerPanel5 = new JPanel();
        // JPanel containerPanel6 = new JPanel();
        containerPanel4.setLayout(new GridLayout(1, 5));
        containerPanel4.add(priceBtn);
        containerPanel4.add(priceLabel);
        containerPanel4.add(quantityLabel);
        containerPanel4.add(quantityJTF);
        containerPanel4.add(calculateBtn);
        containerPanel5.setLayout(new GridLayout(1, 2));
        containerPanel5.add(countLabel);
        containerPanel5.add(purchaseBtn);
        // JLabel purchaseResultLabel = new JLabel();
        purchasePanel.add(containerPanel4);
        purchasePanel.add(containerPanel5);
        purchasePanel.add(purchaseResultLabel);

        this.add(customerPanel);
        this.add(categoryPanel);
        this.add(purchasePanel);
        this.setVisible(true);
    }

    public void initEvent() {

        customerBtn.addActionListener(event -> {
            String cid = customerJTF.getText().trim();
            customer = mJdbcQueryUtil.findCustomer(Integer.parseInt(cid));
            if (customer != null) {
                customerLabel.setText(customer.toString());
            } else {
                customerLabel.setText("Don't exists customer.Please Input the customer id again");
            }
        });

        categoryComboBox.addActionListener(event -> {
            category = categoryComboBox.getItemAt(categoryComboBox.getSelectedIndex());
            System.out.println(category.toString());
        });

        bookBtn.addActionListener(event -> {
            bookComboBox.removeAllItems();
            String title = bookJTF.getText().trim();
            if (category != null) {
                List<Book> books = mJdbcQueryUtil.findBook(title, category.getCat());
                for (Book book : books) {
                    bookComboBox.addItem(book);
                }
            }
        });

        bookComboBox.addActionListener(event ->  {
            book = bookComboBox.getItemAt(bookComboBox.getSelectedIndex());
            System.out.println(book);
        });

        priceBtn.addActionListener(event -> {
            if (book != null) {
                offer = mJdbcQueryUtil.minPrice(book.getTitle());
                if (offer != null) {
                    priceLabel.setText(offer.getPrice() + "");
                }
            }
        });

        calculateBtn.addActionListener(event -> {
            if (offer != null) {
                String number = quantityJTF.getText().trim();
                quantity = Integer.parseInt(number);
                count = offer.getPrice() * quantity;
                countLabel.setText("Total is " + count + ", purchase?");
            }
        });

        purchaseBtn.addActionListener(event -> {
            Member member = mJdbcQueryUtil.findMember(customer.getCid());
            if (member != null) {
                Purchase purchase = new Purchase();
                purchase.setCid(member.getCid());
                purchase.setClub(member.getClub());
                purchase.setQnty(quantity);
                purchase.setTitle(offer.getTitle());
                purchase.setWhen(new Date());
                purchase.setYear(offer.getYear());
                if (mJdbcQueryUtil.insertPurchase(purchase)) {
                	purchaseResultLabel.setText("Purchase false.");
                } else {
                    purchaseResultLabel.setText(purchase.toString());
                }

            }
        });


    }

}
