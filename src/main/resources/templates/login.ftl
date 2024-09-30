<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
</head>
<body>
<form id="loginForm">
    <input type="text" id="username" placeholder="用户名" required>
    <input type="password" id="password" placeholder="密码" required>
    <button type="submit">登录</button>
</form>

<script>
    document.getElementById('loginForm').onsubmit = async function (e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username, password})
        });

        if (response.ok) {
            const data = await response.text();
            console.log(data)
            localStorage.setItem('token', data);
            window.location.href = '/front/input';
        } else {
            alert('登录失败');
        }
    };
</script>
</body>
</html>
