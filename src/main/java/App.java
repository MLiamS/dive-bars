import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("query", "");
      model.put("bars", Bar.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/bars/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("query", "");
      model.put("template", "templates/bar-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/bars", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String description = request.queryParams("description");
      String happy = request.queryParams("happy_hour");
      String close = request.queryParams("closing_time");
      boolean hasMusic;
      if (request.queryParams("has_music") == null) {
        hasMusic = false;
      } else {
        hasMusic = true;
      }
      boolean hasPool;
      if (request.queryParams("has_pool") == null) {
        hasPool = false;
      } else {
        hasPool = true;
      }
      boolean hasFryer;
      if (request.queryParams("has_fryer") == null) {
        hasFryer = false;
      } else {
        hasFryer = true;
      }
      Bar newBar = new Bar(name, description, happy, close, hasMusic, hasPool, hasFryer);
      newBar.save();

      model.put("bars", Bar.all());
      model.put("query", "");
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/bars/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Bar bar = Bar.find(Integer.parseInt(request.params(":id")));
      model.put("bar", bar);
      model.put("comments", Comment.allFromBar(Integer.parseInt(request.params(":id"))));
      model.put("query", "");
      model.put("template", "templates/bar.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/bars/:id/comments/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Bar bar = Bar.find(Integer.parseInt(request.params(":id")));
      model.put("query", "");
      model.put("bar", bar);
      model.put("template", "templates/bar-comment-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/bars/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String comString = request.queryParams("comment");
      String commenter = request.queryParams("commenter");
      Comment comment = new Comment(comString, commenter, Integer.parseInt(request.params(":id")));
      comment.save();
      Bar bar = Bar.find(Integer.parseInt(request.params(":id")));
      model.put("bar", bar);
      model.put("comments", Comment.allFromBar(Integer.parseInt(request.params(":id"))));
      model.put("query", "");
      model.put("template", "templates/bar.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/search", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String query = request.queryParams("search");
      ArrayList<Bar> barList = Bar.search(query);
      model.put("query", query);
      model.put("bars", barList);
      model.put("template", "templates/search-results.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
