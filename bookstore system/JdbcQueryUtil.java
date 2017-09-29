import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcQueryUtil {

    String jdbcClassName="com.ibm.db2.jcc.DB2Driver";
    String url="jdbc:db2:c3421m";

    Connection connection = null;
    String  queryText = "";     // The SQL text.
    PreparedStatement querySt   = null;   // The query handle.
    ResultSet answers   = null;   // A cursor.


    public static JdbcQueryUtil instance = new JdbcQueryUtil();

    private JdbcQueryUtil() {
        try {
            try {
                Class.forName(jdbcClassName).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            System.out.println("Start Connecting");
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(connection!=null){
                System.out.println("Connected successfully.");
            }
        }
    }

    public static JdbcQueryUtil getInstance() {
        return instance;
    }

    public Customer findCustomer(int cid) {
        Customer customer = null;
        queryText = "SELECT * " + "FROM yrb_customer " + "WHERE cid = ?";
        try {
            querySt = connection.prepareStatement(queryText);
            querySt.setInt(1, cid);
            answers = querySt.executeQuery();
            if (answers.next()) {
                customer = new Customer();
                customer.setCid(cid);
                customer.setName(answers.getString("name"));
                customer.setCity(answers.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public Member findMember(int cid) {
        Member member = null;
        queryText = "SELECT * " + "FROM yrb_member " + "WHERE cid = ?";
        try {
            querySt = connection.prepareStatement(queryText);
            querySt.setInt(1, cid);
            answers = querySt.executeQuery();
            if (answers.next()) {
                member = new Member();
                member.setCid(cid);
                member.setClub(answers.getString("club"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

    public List<Category> fetchCategories() {
        List<Category> categories = new ArrayList<>();
        queryText = "SELECT * " + "FROM yrb_category";
        try {
            querySt = connection.prepareStatement(queryText);
            answers = querySt.executeQuery();
            while (answers.next()) {
                Category category = new Category();
                category.setCat(answers.getString("cat"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public List<Book> findBook(String title, String cat) {
        List<Book> books = new ArrayList<>();
        queryText = "SELECT * " + "FROM yrb_book " + "WHERE title LIKE ? AND cat = ?";
        try {
            querySt = connection.prepareStatement(queryText);
            querySt.setString(1, "%" + title + "%");
            querySt.setString(2, cat);
            answers = querySt.executeQuery();
            while (answers.next()) {
                Book book = new Book();
                book.setTitle(answers.getString("title"));
                book.setCat(cat);
                book.setLanguage(answers.getString("language"));
                book.setYear(answers.getInt("year"));
                book.setWeight(answers.getInt("weight"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Offer minPrice(String title) {
        Offer offer = null;
        queryText = "SELECT * " + "FROM yrb_offer " + "WHERE title = ? ORDER BY price";
        try {
            querySt = connection.prepareStatement(queryText);
            querySt.setString(1, title);
            answers = querySt.executeQuery();
            if (answers.next()) {
                offer = new Offer();
                offer.setTitle(title);
                offer.setYear(answers.getInt("year"));
                offer.setClub(answers.getString("club"));
                offer.setPrice(answers.getFloat("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offer;
    }

    public boolean insertPurchase(Purchase purchase) {
        boolean result = false;
        queryText = "insert into yrb_purchase (cid,club,title,year,when,qnty) values (?, ?, ?, ?, ?, ?)";
        try {
            querySt = connection.prepareStatement(queryText);
            querySt.setInt(1, purchase.getCid());
            querySt.setString(2, purchase.getClub());
            querySt.setString(3, purchase.getTitle());
            querySt.setInt(4, purchase.getYear());
            querySt.setDate(5, new Date(purchase.getWhen().getYear(), purchase.getWhen().getMonth(), purchase.getWhen().getDay()));
//            querySt.setDate(5, new Date(purchase.getWhen().getTime()));
            querySt.setInt(6, purchase.getQnty());
            result = querySt.execute();
            System.out.println(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
