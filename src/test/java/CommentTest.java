import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CommentTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/dive_bars_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM name_of_your_table *;";
      con.createQuery(sql).executeUpdate();
    }
  }

}
