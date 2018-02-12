package org.example.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RestApiVerticle extends AbstractVerticle {
    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        Router httpRouter = Router.router(vertx);
        httpRouter.route().handler(BodyHandler.create());
        httpRouter.post("/sendMessage")
                .handler(request -> {
                    vertx.eventBus().publish("router", request.getBodyAsString());
                    request.response().end("ok");
                });
        httpRouter.get("/getUpdates")
                .handler(request -> {
                    vertx.eventBus().send("getUpdates", request.getBodyAsString(), result -> {
                        request.response().end(result.result().body().toString());
                    });
                });
        httpServer.requestHandler(httpRouter::accept);
        httpServer.listen(8080);
    }
}
