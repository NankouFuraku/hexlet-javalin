package org.example;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import static io.javalin.rendering.template.TemplateUtil.model;
import org.example.hexlet.model.Course;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;

public class HelloWorld {
    static final List<Course> COURSES = List.of(
            new Course("Java Basics", "An introductory course to Java."),
            new Course("Advanced Java", "A deep dive into Java programming."),
            new Course("Java Spring Framework", "Learn to build applications using Spring."));
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        app.get("/", ctx -> ctx.render("index.jte"));

        app.get("/courses/{id}", ctx -> {
            var id = ctx.pathParam("id");
            var course = new Course("java", "Java course for beginners");
            var page = new CoursePage(course);
            ctx.render("courses/show.jte", model("page", page));
        });

        app.get("/courses", ctx -> {
            var term = ctx.queryParam("term");
            List<Course> filteredCourses;

            if (term != null) {
                filteredCourses = COURSES.stream()
                        .filter(c -> c.getName().equals(term) || c.getDescription().contains(term))
                        .toList();
            } else {
                filteredCourses = COURSES;
            }

            var header = "Курсы по программированию";
            var page = new CoursesPage(filteredCourses, header, term);
            ctx.render("courses/index.jte", model("page", page));
        });

        app.get("/users", ctx -> ctx.result("GET /users"));

        app.get("/users/{id}", ctx -> {
            var id = ctx.pathParam("id");
            var escapedId = StringEscapeUtils.escapeHtml4(id);
            ctx.contentType("text/html");
            ctx.result(escapedId);
        });

        app.get("/users/{id}/post/{postId}", ctx -> {
            var id = ctx.pathParam("id");
            var postId = ctx.pathParam("postId");
            ctx.result("User ID: " + id + " Post ID: " + postId);
        });

        app.get("/hello", ctx -> {
            var name = ctx.queryParamAsClass("name", String.class).getOrDefault("World");
            ctx.result("Hello, " + name + "!");
        });

        app.post("/users", ctx -> ctx.result("POST /users"));

        app.start(7070);
    }
}