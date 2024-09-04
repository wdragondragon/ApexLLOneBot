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

        .filters {
            display: flex;
            justify-content: flex-start;
            margin-bottom: 20px;
        }

        .filters button {
            background-color: #333;
            background-image: linear-gradient(to bottom, #444, #222);
            color: white;
            border: 1px solid #555;
            padding: 10px 20px;
            margin-right: 10px;
            cursor: pointer;
            border-radius: 4px;
            transition: background 0.3s, border-color 0.3s;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
        }

        .filters button:hover {
            background-color: #555;
            background-image: linear-gradient(to bottom, #666, #333);
            border-color: #777;
        }

        .filters button:active {
            background-color: #222;
            background-image: linear-gradient(to bottom, #333, #111);
            border-color: #999;
        }

        .filters button.selected {
            background-color: #1E90FF; /* 突显选中的按钮 */
            background-image: linear-gradient(to bottom, #1E90FF, #1C86EE);
            border-color: #1C86EE;
            color: white;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.8);
        }

        .container {
            max-width: 500px;
            margin: 0 auto;
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
    <!-- 左上角的筛选按钮 -->
    <div class="filters">
        <form method="get" action="/ban/today">
            <button type="submit" name="range" value="today" class="<#if range == 'today'>selected</#if>">今天</button>
            <button type="submit" name="range" value="7" class="<#if range == '7'>selected</#if>">近7天</button>
            <button type="submit" name="range" value="30" class="<#if range == '30'>selected</#if>">近30天</button>
        </form>
    </div>
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
