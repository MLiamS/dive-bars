import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.List;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("bars", Bar.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/bars/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
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
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/bars/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Bar bar = Bar.find(Integer.parseInt(request.params(":id")));
      model.put("bar", bar);
      model.put("template", "templates/bar.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
