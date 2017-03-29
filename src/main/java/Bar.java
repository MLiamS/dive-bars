import org.sql2o.*;
import java.util.List;

public class Bar {
  private int id;
  private String name;
  private String description;
  private String happyHour;
  private String closingTime;
  private boolean hasMusic;
  private boolean hasPool;
  private boolean hasFryer;
  private float rating;

  public Bar(String name, String description, String happyHour, String closingTime, boolean hasMusic, boolean hasPool, boolean hasFryer) {
    this.name = name;
    this.description = description;
    this.happyHour = happyHour;
    this.closingTime = closingTime;
    this.hasMusic = hasMusic;
    this.hasPool = hasPool;
    this.hasFryer = hasFryer;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public String getHappyHour() {
    return this.happyHour;
  }

  public String getClosingTime() {
    return this.closingTime;
  }

  public boolean getHasMusic() {
    return this.hasMusic;
  }

  public boolean getHasPool() {
    return this.hasPool;
  }

  public boolean getHasFryer() {
    return this.hasFryer;
  }

  public int getId() {
    return this.id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO bars (name, description, happy_hour, closing_time, has_music, has_pool, has_fryer) VALUES (:name, :description, :hh, :ct, :hm, :hp, :hf)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .addParameter("description", this.description)
      .addParameter("hh", this.happyHour)
      .addParameter("ct", this.closingTime)
      .addParameter("hm", this.hasMusic)
      .addParameter("hp", this.hasPool)
      .addParameter("hf", this.hasFryer)
      .executeUpdate()
      .getKey();
    }
  }

  public static List<Bar> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bars";
      return con.createQuery(sql)
        .addColumnMapping("happy_hour", "happyHour")
        .addColumnMapping("closing_time", "closingTime")
        .addColumnMapping("has_music", "hasMusic")
        .addColumnMapping("has_pool", "hasPool")
        .addColumnMapping("has_fryer", "hasFryer")
        .executeAndFetch(Bar.class);
    }
  }

  @Override
  public boolean equals(Object otherBar) {
    if(!(otherBar instanceof Bar)) {
      return false;
    } else {
      Bar newBar = (Bar) otherBar;
      return this.getName().equals(newBar.getName()) && this.getId() == newBar.getId();
    }
  }

  public static Bar find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM bars WHERE id = :id";
      Bar bar = con.createQuery(sql)
        .addParameter("id", id)
        .addColumnMapping("happy_hour", "happyHour")
        .addColumnMapping("closing_time", "closingTime")
        .addColumnMapping("has_music", "hasMusic")
        .addColumnMapping("has_pool", "hasPool")
        .addColumnMapping("has_fryer", "hasFryer")
        .executeAndFetchFirst(Bar.class);
      return bar;
    }
  }
}
