const baseUrl = "http://8.137.22.197:8080";

// 获取页面元素
const inputText = document.getElementById('inputText');
const plainText = document.getElementById('plainText');
const keyResult = document.getElementById('keyResult');
const timeResult = document.getElementById('timeResult');
const breakBtn = document.getElementById('breakBtn');

// 发送破解请求
breakBtn.addEventListener('click', () => {
    const cipherText = inputText.value;
    const plainMatchText = plainText.value;

    if (!cipherText || !plainMatchText) {
        alert("请输入密文和明文匹配项");
        return;
    }

    const requestData = {
        plainTexts: [plainMatchText],
        cipherTexts: [cipherText]
    };

    fetch(`${baseUrl}/des/break`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 1) {
                const keys = data.data.keys.join(', ');
                const time = data.data.time;
                keyResult.value = keys;
                timeResult.value = `${time} 秒`;
            } else {
                alert("破解失败：" + data.msg);
            }
        })
        .catch(error => {
            alert("请求失败：" + error.message);
        });
});
