
#新仿知乎API文档

###规则
1. 参数表单提交，返回json

2. status表示状态：

+ 200 —— 成功
+ 0 —— 失败
+ 213 —— token无效

3. 为方便开发学习，info输出sql语句
###1. 注册
地址：http://redrock.hotwoo.cn/zhihu/register.php
参数：
>name:
>password:

返回：
>{
    "status": 200,
    "info": "INSERT INTO person ( name , password ) VALUES ( 'jimm' , '33' )",
    "data": null
}

###2. 登录
地址：http://redrock.hotwoo.cn/zhihu/login.php
参数
>name:
>password:

返回：
>{
    "status": 200,
    "info": "SELECT * FROM person WHERE name = 'jimm' AND password = '33'",
    "data": {
        "id": "50",
        "name": "jimm",
        "face": null,
        "password": "33",
        "token": "501343d0d2a14eb67885ee4f8c2ef31d95fb8859"
    }
}

返回用户信息及token

###3. 修改头像
地址：http://redrock.hotwoo.cn/zhihu/modifyFace.php
参数
>token:
>face：

传用户头像地址。本API不负责图片文件储存，图片储存请右转阿里，七牛。把图片地址穿上来。

返回
>{
    "status": 200,
    "info": "UPDATE person SET face = 33333 WHERE id = 50",
    "data": null
}

###4. 取问题列表
地址：http://redrock.hotwoo.cn/zhihu/getQuestionList.php
参数
>page:
>count:

count可空，每页条数，默认20条。
返回
>{
    "status": 200,
    "info": "SELECT question.id,question.title,question.content,question.bestAnswerId,question.date,question.recent,question.answerCount,question.authorId,person.name as authorName,person.face as authorFace FROM person RIGHT JOIN question ON person.id = question.authorId ORDER BY IFNULL(recent,date) DESC LIMIT 0,20",
    "data": {
        "totalCount": 6,
        "totalPage": 1,
        "questions": [
            {
                "id": "70",
                "title": "为什么欧洲各国都在争夺人民币离岸中心？",
                "content": "最近在欧洲除了英国，德国法国也都加入人民币离岸中心的争夺，而早前在亚洲像新加坡，香港，日本等地区也有过类似的情况，各国为什么会如此看重？这对各国将产生什么影响？而对中国来说，又该如何选择？",
                "bestAnswerId": null,
                "date": "2015-06-08 19:38:33",
                "recent": "2015-06-10 15:17:04",
                "answerCount": "1",
                "authorId": "58",
                "authorName": "day",
                "authorFace": null
            },
            {
                "id": "75",
                "title": "期末要挂了怎么办？",
                "content": "",
                "bestAnswerId": null,
                "date": "2015-06-10 14:57:30",
                "recent": null,
                "answerCount": "0",
                "authorId": "58",
                "authorName": "day",
                "authorFace": null
            },
            {
                "id": "69",
                "title": "怎样从领导讲话中过滤废话？",
                "content": "非常急，在线等。领导讲了8个小时根本停不下来。",
                "bestAnswerId": null,
                "date": "2015-06-08 17:22:17",
                "recent": "2015-06-10 14:56:37",
                "answerCount": "2",
                "authorId": "58",
                "authorName": "day",
                "authorFace": null
            },
            {
                "id": "74",
                "title": "你为何这么屌？",
                "content": "屌炸了。",
                "bestAnswerId": null,
                "date": "2015-06-10 14:52:36",
                "recent": "2015-06-10 14:52:36",
                "answerCount": "0",
                "authorId": "58",
                "authorName": "day",
                "authorFace": null
            },
            {
                "id": "68",
                "title": "挖掘机技术哪家强？",
                "content": "非常急，在线等！",
                "bestAnswerId": null,
                "date": "2015-06-08 17:17:48",
                "recent": "2015-06-10 14:45:58",
                "answerCount": "1",
                "authorId": "58",
                "authorName": "day",
                "authorFace": null
            }
        ],
        "curPage": "0"
    }
}

recent表示最近回复时间。没有回复时为null。

###5. 取回答列表
地址：http://redrock.hotwoo.cn/zhihu/getAnswerList.php
参数：
>page:
>questionId:
>count:
>desc
count可空，每页条数，默认20条。
desc可空，是否倒序，填true 或 false
返回：
>{
    "status": 200,
    "info": "SELECT answer.id,answer.content,answer.date,answer.authorId,person.name as authorName,person.face as authorFace FROM person RIGHT JOIN answer ON person.id = answer.authorId WHERE questionId = 33 LIMIT 0,20",
    "data": {
        "totalCount": "2",
        "totalPage": 1,
        "answers": [
            {
                "id": "42",
                "content": "hehe",
                "date": "2015-05-02 16:49:14",
                "authorId": "21",
                "authorName": "zhs",
                "authorFace": null
            },
            {
                "id": "43",
                "content": "??",
                "date": "2015-05-02 16:49:20",
                "authorId": "21",
                "authorName": "zhs",
                "authorFace": null
            }
        ]
         "curPage": "0"
    }
}

###6. 发布问题
地址：http://redrock.hotwoo.cn/zhihu/question.php
参数：
>title:
>content:
>token:

返回：
>{
    "status": 200,
    "info": "INSERT INTO question ( authorId , title , content , date ) \n\tVALUES ( '19' , '这是标题','这是描述',now())",
    "data": null
}

###7. 发布回答
地址：http://redrock.hotwoo.cn/zhihu/answer.php
参数：
>questionId:
>content:
>token

返回：
>{
    "status": 200,
    "info": "INSERT INTO answer ( authorId , questionId , content , date ) VALUES ( '34' , '13','hehe',now())",
    "data": null
}