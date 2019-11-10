<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login</title>
</head>
<body>
<h2>登陆页面</h2>
<form >
    用户名:<br>
    <input type="text" id="username" value="lhq">
    <br>
    密码:<br>
    <input type="text" id="password" value="123456">
    <br><br>
</form>
<button id="btn-submit"> 登录 </button>
<button id="btn-apitest"> 调用接口测试 </button>
<p id="token"> 请执行登录获取 token </p>
</body>
<script>
    var jwtToken = undefined;
    document.getElementById("btn-submit").addEventListener("click", function (ev) {
        var username = document.getElementById("username").value
        var password = document.getElementById("password").value
        fetch("/login", {
            method: 'POST', // or 'PUT'
            body:   '{"username":"' +  username +'","password":"' + password + '"}' , // data can be `string` or {object}!
            headers: new Headers({
                'Content-Type': 'application/json'
            })
        }).then(function (res) {
            // console.log(res.json())

           return res.json();
        }).then(function (token) {
            console.log(token);
            jwtToken = token.token;
            document.getElementById("token").innerHTML=JSON.stringify(token) ;
        })

    });

    document.getElementById("btn-apitest").addEventListener("click", function (ev) {
        fetch('/apitest',{
            headers:{
                "Authorization" :jwtToken
            }
        })
            .then(function(res) {
                return res.json();
            })
            .then(function(response) {
                alert(JSON.stringify(response))
            });
    });

</script>
</html>