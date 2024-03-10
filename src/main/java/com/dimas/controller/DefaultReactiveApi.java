package com.dimas.controller;

import com.dimas.openapi.model.ApiLoginPost200Response;
import com.dimas.openapi.model.ApiLoginPostRequest;
import com.dimas.openapi.model.ApiUser;
import com.dimas.openapi.model.ApiUserRegisterPost200Response;
import com.dimas.openapi.model.ApiUserRegisterPostRequest;
import io.quarkiverse.openapi.generator.annotations.GeneratedParam;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;


@Path("")
@ApplicationScoped
public interface DefaultReactiveApi {

    /**
     * Упрощенный процесс аутентификации путем передачи идентификатор пользователя и получения токена для дальнейшего прохождения авторизации
     *
     * @param apiLoginPostRequest
     */
    @POST
    @Path("/login")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<ApiLoginPost200Response> loginPost(
            ApiLoginPostRequest apiLoginPostRequest
    );

    /**
     * Получение анкеты пользователя
     *
     * @param id Идентификатор пользователя
     */
    @GET
    @Path("/user/get/{id}")
    @Produces({"application/json"})
    Uni<ApiUser> userGetIdGet(
            @GeneratedParam("id") @PathParam("id") String id
    );

    /**
     * Регистрация нового пользователя
     *
     * @param apiUserRegisterPostRequest
     */
    @POST
    @Path("/user/register")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    Uni<ApiUserRegisterPost200Response> userRegisterPost(
            ApiUserRegisterPostRequest apiUserRegisterPostRequest
    );


}
