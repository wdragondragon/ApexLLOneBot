<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Styled Table with Freemarker</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }

        h1 {
            text-align: center;
            color: #343a40;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
            box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px 15px;
            text-align: center;
            border: 1px solid #dee2e6;
        }

        th {
            background-color: #6c757d;
            color: #ffffff;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #e9ecef;
        }

        @media (max-width: 768px) {
            table {
                width: 100%;
            }
        }
    </style>
</head>
<body>
<h1>${title}</h1>
<table>
    <thead>
    <tr>
        <#list tableHeaders as header>
            <th>${header}</th>
        </#list>
    </tr>
    </thead>
    <tbody>
    <#list tableRows as row>
        <tr>
            <#list row as cell>
                <td>${cell}</td>
            </#list>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
