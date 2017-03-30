import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;

public class VoteTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/dive_bars_test", null, null);
  }

  @After
  public void tearDown() {
    try (Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM votes *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Vote_instantiatesCorrectly_true() {
    Vote vote = new Vote(5, 1);
    assertTrue(vote instanceof Vote);
  }

  @Test
  public void getters_returnsValues_true() {
    Vote vote = new Vote(5, 1);
    assertEquals(5, vote.getRating());
    assertEquals(1, vote.getBarId());
  }

  @Test
  public void save_savesToDatabase_true() {
    Vote vote = new Vote(5, 1);
    vote.save();
    assertTrue(Vote.all().get(0).equals(vote));
  }

  @Test
  public void averageRating_returnsAverageRatingFromBarById_4() {
    Vote vote1 = new Vote(5, 1);
    vote1.save();
    Vote vote2 = new Vote(3, 1);
    vote2.save();
    Vote vote3 = new Vote(0, 2);
    vote3.save();
    assertEquals(4, Vote.averageRating(1), 0);
  }
}
