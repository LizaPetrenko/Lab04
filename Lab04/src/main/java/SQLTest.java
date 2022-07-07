import java.sql.*;

public class SQLTest {
    private Connection con;

    private void initialization(String name){
        try{
            Class.forName("org.sqlite.JDBC"); //ініціює з'єднання з базою даних
            con = DriverManager.getConnection("jdbc:sqlite:" + name); // створення конекшену
            PreparedStatement st = con.prepareStatement("create table if not exists 'test' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text);");
            int result = st.executeUpdate(); //буде виконувати наші запити
        }catch(ClassNotFoundException e){ //оброблювання помилок, або щось, що буде неправильно працювати
            System.out.println("Не знайшли драйвер JDBC");
            e.printStackTrace();
            System.exit(0);
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит");
            e.printStackTrace();
        }
    }

    private void insertTestData(String name){
        try{
            PreparedStatement statement = con.prepareStatement("INSERT INTO test(name) VALUES (?)");
            //statement.setInt(1, 1);
            statement.setString(1, name);

            int result = statement.executeUpdate();

            statement.close();
        }catch (SQLException e){
            System.out.println("Не вірний SQL запит на вставку");
            e.printStackTrace();
        }
    }

    private void showAllData(){
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM test");//вибирає всі колонки з таблички test
            while (res.next()) {
                String name = res.getString("name");//опрацьовуємо кожен рядочок
                System.out.println (res.getShort("id")+" "+name); //виводимо результат
            }
            res.close();
            st.close();
        }catch(SQLException e){
            System.out.println("Не вірний SQL запит на вибірку даних");
            e.printStackTrace();
        }
    }

    private void updateName(String newName, int id) throws SQLException {

        PreparedStatement statement = con.prepareStatement("UPDATE test set name=(?) where id=(?)");
        //statement.setInt(1, 1);
        statement.setString(1, newName);
        statement.setInt(2, id);
        //statement.executeUpdate();
        //statement.close();


        final boolean oldAutoCommit = statement.getConnection().getAutoCommit();
        statement.getConnection().setAutoCommit(false);

        try {
            // Your update and insert code here
            statement.executeUpdate();
        } catch(Exception e) {
            statement.getConnection().rollback();
        } finally {
            statement.getConnection().commit();
            statement.getConnection().setAutoCommit(oldAutoCommit);
        }


    }

    private void delete(int id) throws SQLException {


        PreparedStatement statement = con.prepareStatement("DELETE from test where id=(?)");
        statement.setInt(1,id);
        statement.execute();
        statement.close();


    }

    public static void main(String[] args) throws SQLException {
        SQLTest sqlTest = new SQLTest();
        sqlTest.initialization("HelloDB");

        //insert
        sqlTest.insertTestData("MiniMAKAKA");
        sqlTest.insertTestData("MaxiMAKAKA");

        System.out.println("-------------------------------------------");
        sqlTest.showAllData();


        //delete
        sqlTest.delete(8);//delete MaxiMAKAKA

        System.out.println("-------------------------------------------");
        sqlTest.showAllData();


        //update
        sqlTest.updateName("MaxiMAKAKA", 2);
        sqlTest.updateName("MaxiMAKAKA", 6);

        System.out.println("-------------------------------------------");
        sqlTest.showAllData();














    }
}
