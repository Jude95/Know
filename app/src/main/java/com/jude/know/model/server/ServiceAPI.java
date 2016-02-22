package com.jude.know.model.server;


import com.jude.know.bean.AnswerResult;
import com.jude.know.bean.Info;
import com.jude.know.bean.QuestionResult;
import com.jude.know.bean.User;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Mr.Jude on 2015/11/18.
 */
public interface ServiceAPI {
    String SERVER_ADDRESS = "http://redrock.alien95.cn/know/";

    @POST("register.php")
    @FormUrlEncoded
    Observable<Info> register(
            @Field("name") String name,
            @Field("password") String password);

    @POST("login.php")
    @FormUrlEncoded
    Observable<User> login(
            @Field("name") String name,
            @Field("password") String password);

    @POST("modifyFace.php")
    @FormUrlEncoded
    Observable<Info> modifyFace(
            @Field("token") String token,
            @Field("face") String face);

    @POST("getQuestionList.php")
    @FormUrlEncoded
    Observable<QuestionResult> getQuestionList(
            @Field("page") int page,
            @Field("count") int count);

    @POST("getAnswerList.php")
    @FormUrlEncoded
    Observable<AnswerResult> getAnswerList(
            @Field("page") int page,
            @Field("questionId") int questionId,
            @Field("count") int count,
            @Field("desc") boolean desc);

    @POST("question.php")
    @FormUrlEncoded
    Observable<Info> question(
            @Field("title") String title,
            @Field("content") String content,
            @Field("token") String token);

    @POST("answer.php")
    @FormUrlEncoded
    Observable<Info> answer(
            @Field("questionId") int questionId,
            @Field("content") String content,
            @Field("token") String token);

}
