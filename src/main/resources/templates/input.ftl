<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>输入页面</title>
</head>
<body>
<label for="createNumber"></label>
<input type="number" id="createNumber" placeholder="创建数量" required>
<label for="keyType"></label>
<select id="keyType" required>
    <option value="">选择密钥类型</option>
    <option value="ai">ai</option>
    <option value="apex_recoils">apex_recoils</option>
    <option value="apex_recoils_server">apex_recoils_server</option>
    <option value="auto_upgrade_script">auto_upgrade_script</option>
</select>

<label for="validateType"></label>
<select id="validateType" required>
    <option value="">选择验证类型</option>
    <option value="1">天</option>
    <option value="2">周</option>
    <option value="3">月</option>
    <option value="4">年</option>
    <option value="5">永久</option>
</select>
<button id="submitButton">提交</button>

<div id="result"></div>

<script>
    document.getElementById('submitButton').onclick = async function () {
        const token = localStorage.getItem('token');
        const createNumber = document.getElementById('createNumber').value;
        const keyType = document.getElementById('keyType').value;
        const validateType = document.getElementById('validateType').value;

        // 构建 URL
        const params = [
            'createNumber=' + encodeURIComponent(createNumber),
            'keyType=' + encodeURIComponent(keyType),
            'validateType=' + encodeURIComponent(validateType)
        ];
        const url = '/ag/createKeyExt?' + params.join('&'); // 使用 join 构建查询参数
        fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.text()) // 直接获取文本
            .then(result => {
                const resultDiv = document.getElementById('result');
                resultDiv.innerHTML = result.replace(/\n/g, '<br>'); // 显示返回的字符串
            })
            .catch(() => {
                document.getElementById('result').innerHTML = '提交失败';
            });
    };
</script>
</body>
</html>
