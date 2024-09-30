<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rank Score Chart</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            background-color: #2b2b2b;
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        #chartContainer {
            width: 90%;
            max-width: 1200px;
            height: 80%;
            margin-top: 20px;
        }

        canvas {
            width: 100% !important;
            height: 100% !important;
        }

        input, button {
            padding: 10px;
            margin: 5px;
            font-size: 1rem;
        }

        button {
            cursor: pointer;
        }
    </style>
</head>
<body>
<!-- 输入框和按钮 -->
<div>
    <input type="text" id="uidInput" placeholder="Enter UID"/>
    <button onclick="loadChartData()">Load Data</button>
</div>

<!-- 图表容器 -->
<div id="chartContainer">
    <canvas id="rankChart"></canvas>
</div>

<script>
    let rankChart;  // 将 rankChart 声明为全局变量以便更新图表

    function loadChartData() {
        const uid = document.getElementById('uidInput').value;
        if (!uid) {
            alert("Please enter a valid UID");
            return;
        }

        // 构建 API URL
        const apiUrl = 'http://127.0.0.1:8091/test/apexRankHistory?season=s22_s2&uid=' + uid;

        // 获取数据
        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                // 假设接口返回的数据格式和之前一致
                const rankData = data;

                // 按照 timestamp 从小到大排序
                rankData.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

                // 获取 labels 和 rankScore、ladder 数据
                const labels = rankData.map(item => item.timestamp);
                const rankScores = rankData.map(item => item.rankScore);
                const ladderRanks = rankData.map(item => item.ladder);

                // 如果图表已经存在，则更新数据；如果不存在，则创建图表
                if (rankChart) {
                    // 更新图表数据
                    rankChart.data.labels = labels;
                    rankChart.data.datasets[0].data = rankScores;
                    rankChart.update();
                } else {
                    // 创建图表
                    const ctx = document.getElementById('rankChart').getContext('2d');
                    rankChart = new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: labels, // 排序后的时间戳
                            datasets: [{
                                label: 'Rank Score',
                                data: rankScores,
                                borderColor: function (context) {
                                    const index = context.dataIndex;
                                    const previousValue = rankScores[index - 1];
                                    const currentValue = rankScores[index];
                                    return previousValue < currentValue ? '#00BFFF' : '#FF4500'; // 蓝色（上升）或红色（下降）
                                },
                                borderWidth: 2,
                                fill: false,
                                pointRadius: 0, // 不显示点，只有在鼠标移上去时显示
                                pointHoverRadius: 5, // 鼠标移上去时显示的点大小
                            }]
                        },
                        options: {
                            responsive: true, // 图表自适应
                            maintainAspectRatio: false, // 禁用默认的宽高比
                            scales: {
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Date'
                                    }
                                },
                                y: {
                                    beginAtZero: false,
                                    title: {
                                        display: true,
                                        text: 'Rank Score'
                                    }
                                }
                            },
                            plugins: {
                                tooltip: {
                                    enabled: true, // 启用鼠标移上去显示提示框
                                    mode: 'nearest', // 让提示框显示在最近的数据点上
                                    intersect: false, // 鼠标不需要完全移到点上，接近时就显示
                                    callbacks: {
                                        // 自定义提示框内容，显示 rankScore 和 ladder
                                        label: function (tooltipItem) {
                                            const rankScore = tooltipItem.raw;
                                            const ladderRank = ladderRanks[tooltipItem.dataIndex]; // 获取对应的 ladder 排名
                                            return [
                                                'Rank Score: ' + rankScore,
                                                'Ladder Rank: ' + ladderRank
                                            ];
                                        }
                                    }
                                },
                                legend: {
                                    display: false // 不显示图例
                                }
                            },
                            hover: {
                                mode: 'nearest', // 鼠标接近时显示点和提示框
                                intersect: false // 鼠标不需要完全移到点上
                            },
                            elements: {
                                line: {
                                    tension: 0.3 // 曲线平滑度
                                }
                            }
                        }
                    });
                }
            })
            .catch(error => console.error('Error fetching data:', error));
    }
</script>
</body>
</html>
