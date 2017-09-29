
public class Test {

    public static void main(String[] args) {
        JdbcQueryUtil mJdbcQueryUtil = JdbcQueryUtil.getInstance();
        mJdbcQueryUtil.findCustomer(1);
        System.out.println(mJdbcQueryUtil.fetchCategories().size());
        System.out.println(mJdbcQueryUtil.findBook("Richmond Underground", "travel").size());
        System.out.println(mJdbcQueryUtil.minPrice("Are my feet too big?").getPrice());

        Window window = new Window();
        window.initUI();
        window.initEvent();
    }
}
