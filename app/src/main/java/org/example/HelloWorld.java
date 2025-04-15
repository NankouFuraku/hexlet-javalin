package org.example;

import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        var app = Javalin.create(config -> config.bundledPlugins.enableDevLogging());

        app.get("/users", ctx -> ctx.result("GET /users"));

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