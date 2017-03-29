import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BarTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/dive_bars_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM bars *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Bar_instantiatesCorrectly_true() {
    Bar bar = new Bar("A & L Tavern", "It's gross!", "5pm-9pm", "2am", false, true, true);
    assertTrue(bar instanceof Bar);
  }

  @Test
  public void getters_barGettersReturn_true() {
    Bar bar = new Bar("A & L Tavern", "It's gross!", "5pm-9pm", "2am", false, true, true);
    assertEquals("A & L Tavern", bar.getName());
    assertEquals("It's gross!", bar.getDescription());
    assertEquals("5pm-9pm", bar.getHappyHour());
    assertEquals("2am", bar.getClosingTime());
    assertEquals(false, bar.getHasMusic());
    assertEquals(true, bar.getHasPool());
    assertEquals(true, bar.getHasFryer());
  }

  @Test
  public void save_savesToDatabase_true() {
    Bar bar = new Bar("A & L Tavern", "It's gross!", "5pm-9pm", "2am", false, true, true);
    bar.save();
    assertTrue(Bar.all().get(0).equals(bar));
  }

  @Test
  public void find_returnsBarById_bar2() {
    Bar bar1 = new Bar("A & L Tavern", "It's gross!", "5pm-9pm", "2am", false, true, true);
    bar1.save();
    Bar bar2 = new Bar("Biddy's", "It's bad!", "5pm-7pm", "1am", true, false, true);
    bar2.save();
    assertEquals(bar2, Bar.find(bar2.getId()));
  }
}
