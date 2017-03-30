import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Vote {
  private int id;
  private int rating;
  private int barId;

  public Vote(int rating, int barId) {
    this.rating = rating;
    this.barId = barId;
  }

  public int getRating() {
    return this.rating;
  }

  public int getBarId() {
    return this.barId;
  }

  public int getId() {
    return this.id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO votes (rating, bar_id) VALUES (:rating, :bar_id)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("rating", this.rating)
      .addParameter("bar_id", this.barId)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Vote> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM votes";
      return con.createQuery(sql)
        .addColumnMapping("bar_id", "barId")
        .executeAndFetch(Vote.class);
    }
  }

  @Override
  public boolean equals(Object otherVote) {
    if(!(otherVote instanceof Vote)) {
      return false;
    } else {
      Vote newVote = (Vote) otherVote;
      return this.getId() == newVote.getId();
    }
  }

  public static Vote find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM votes WHERE id = :id";
      Vote vote = con.createQuery(sql)
        .addParameter("id", id)
        .addColumnMapping("bar_id", "barId")
        .executeAndFetchFirst(Vote.class);
      return vote;
    }
  }

  public static float averageRating(int barId) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM votes WHERE bar_id = :id";
      List<Vote> votes = con.createQuery(sql)
      .addParameter("id", barId)
      .addColumnMapping("bar_id", "barID")
      .executeAndFetch(Vote.class);
      float ratingTotal = 0;
      for (Vote vote : votes) {
        ratingTotal += vote.getRating();
      }
      return ratingTotal / votes.size();
    }
  }
}
