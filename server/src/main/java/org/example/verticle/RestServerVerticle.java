package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServerVerticle extends AbstractVerticle {
    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        Router httpRouter = Router.router(vertx);
        httpRouter.route().handler(BodyHandler.create());
        httpRouter.post("/sendMessage")
                .handler(request -> {
                    vertx.eventBus().send("router", request.getBodyAsString());
                    request.response().end("ok");
                });
        httpRouter.get("/getHistory")
                .handler(request ->
                        vertx.eventBus().send("getHistory", request.getBodyAsString(), result ->
                                request.response().end(result.result().body().toString())
                        )
                );
        httpRouter.get("/parasite")
                .handler(request -> {
                    for (int i = 0; i < 8; i++) {
                        vertx.eventBus().send("parasite", request.getBodyAsString());
                    }
                    request.response().end("ok");
                });
        httpRouter.get("/blockingCode")
                .handler(request -> {
                    for (int i = 0; i < 8; i++) {
                        vertx.eventBus().send("blockingCode", request.getBodyAsString());
                    }
                    request.response().end("ok");
                });
        httpServer.requestHandler(httpRouter::accept);
        httpServer.listen(8081);
    }
}
