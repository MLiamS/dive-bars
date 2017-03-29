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
      String sql = "DELETE FROM comments *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Comment_instantiatesCorrectly_true() {
    Comment comment = new Comment("Wow, it's cool.", "Bub", 1);
    assertTrue(comment instanceof Comment);
  }

  @Test
  public void getters_returnsCorrectly_true() {
    Comment comment = new Comment("Wow, it's cool.", "Bub", 1);
    assertEquals("Wow, it's cool.", comment.getComment());
    assertEquals("Bub", comment.getCommenter());
    assertEquals(1, comment.getBarId());
  }

  @Test
  public void save_savesToDatabase_true() {
    Comment comment = new Comment("Wow, it's cool.", "Bub", 1);
    comment.save();
    assertTrue(Comment.all().get(0).equals(comment));
  }

  @Test
  public void find_returnsCommentById_comment2() {
    Comment comment1 = new Comment("Wow, it's cool.", "Bub", 1);
    comment1.save();
    Comment comment2 = new Comment("Wow, it's great.", "Ry", 1);
    comment2.save();
    assertEquals(comment2, Comment.find(comment2.getId()));
  }
}
