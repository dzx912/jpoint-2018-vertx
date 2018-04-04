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
        httpRouter.get("/message/:address")
                .handler(request -> {
                            String address = request.request().getParam("address");
                            System.out.println("address: " + address);
                            vertx.eventBus().send("getHistory", address, result -> {
                                        String history = result.result().body().toString();
                                        request.response().end(history);
                                    }
                            );
                        }
                );
        httpServer.requestHandler(httpRouter::accept);
        httpServer.listen(8081);
    }
}
