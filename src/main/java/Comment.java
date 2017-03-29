import org.sql2o.*;
import java.util.List;

public class Comment {
  private int id;
  private String comment;
  private String commenter;
  private int barId;

  public Comment(String comment, String commenter, int barId) {
    this.comment = comment;
    this.commenter = commenter;
    this.barId = barId;
  }

  public String getComment() {
    return this.comment;
  }

  public String getCommenter() {
    return this.commenter;
  }

  public int getBarId() {
    return this.barId;
  }

  public int getId() {
    return this.id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO comments (comment, commenter, bar_id) VALUES (:comment, :commenter, :bar_id)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("comment", this.comment)
      .addParameter("commenter", this.commenter)
      .addParameter("bar_id", this.barId)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Comment> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments";
      return con.createQuery(sql)
      .addColumnMapping("bar_id", "barID")
      .executeAndFetch(Comment.class);
    }
  }

  @Override
  public boolean equals(Object otherComment) {
    if(!(otherComment instanceof Comment)) {
      return false;
    } else {
      Comment newComment = (Comment) otherComment;
      return this.getComment().equals(newComment.getComment()) && this.getId() == newComment.getId();
    }
  }

  public static Comment find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments WHERE id = :id";
      Comment comment = con.createQuery(sql)
        .addParameter("id", id)
        .addColumnMapping("bar_id", "barID")
        .executeAndFetchFirst(Comment.class);
      return comment;
    }
  }

  public static List<Comment> allFromBar(int barId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM comments WHERE bar_id = :id";
      return con.createQuery(sql)
      .addParameter("id", barId)
      .addColumnMapping("bar_id", "barID")
      .executeAndFetch(Comment.class);
    }
  }
}
