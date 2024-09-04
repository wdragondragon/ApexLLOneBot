<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ban Information Display</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #000;
            color: #fff;
            margin: 0;
            padding: 0;
        }
        .container {
            width: 90%;
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
        }
        .item {
            margin-bottom: 20px;
            padding: 15px;
            border-radius: 8px;
            background-color: #222;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
            border-left: 8px solid;
        }
        .master {
            border-left-color: purple;
        }
        .apex-pred {
            border-left-color: red;
        }
        .info {
            margin-bottom: 10px;
        }
        .info span {
            font-weight: bold;
        }
        .link {
            margin-top: 10px;
        }
        .link a {
            text-decoration: none;
            color: #007BFF;
            font-weight: bold;
        }
        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <#list items as item>
        <div class="item <#if item.rankRole == "Master">master<#else>apex-pred</#if>">
            <div class="info">
                <p><span>Date:</span> ${item.banDate.format('yyyy-MM-dd hh:mm:ss')}</p>
                <p><span>ID:</span> ${item.uid}</p>
                <p><span>User ID:</span> ${item.statusUid}</p>
                <p><span>Rank:</span> ${item.descMsg}</p>
                <p><span>Msg:</span> ${item.msg}</p>
            </div>
            <div class="link">
                <a href="https://apexlegendsstatus.com/profile/uid/${item.platform}/${item.statusUid}" target="_blank">
                    View Apex Legends Status
                </a>
            </div>
        </div>
    </#list>
</div>
</body>
</html>
