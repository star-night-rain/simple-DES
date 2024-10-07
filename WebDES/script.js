const baseUrl = "http://8.137.22.197:8080";

// 获取页面元素
const inputText = document.getElementById('inputText');
const secretKey = document.getElementById('secretKey');
const outputResult = document.getElementById('outputResult');
const encryptBtn = document.getElementById('encryptBtn');
const decryptBtn = document.getElementById('decryptBtn');
const jumpBtn=document.getElementById('jumpBtn');
// 发送加密请求
encryptBtn.addEventListener('click', () => {
    const plainText = inputText.value;
    const key = secretKey.value;

    if (!plainText || !key|| key.length !== 10) {
        alert("请输入明文和密钥，同时密钥长度必须为10bit二进制");
        return;
    }

    const requestData = {
        plainText: plainText,
        secretKey: key
    };

    fetch(`${baseUrl}/des/encode`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.code === 1) {
                outputResult.value = data.data.cipherText;
            } else {
                alert("加密失败：" + data.msg);
            }
        })
        .catch(error => {
            alert("请求失败：" + error.message);
        });
});

// 发送解密请求
decryptBtn.addEventListener('click', () => {
    const cipherText = inputText.value;
    const key = secretKey.value;

    if (!cipherText || !key|| key.length !== 10) {
        alert("请输入密文和密钥，同时密钥长度必须为10bit二进制");
        return;
    }

    const requestData = {
        cipherText: cipherText,
        secretKey: key
    };

    fetch(`${baseUrl}/des/decode`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.code === 1) {
                outputResult.value = data.data.plainText;
            } else {
                alert("解密失败：" + data.msg);
            }
        })
        .catch(error => {
            alert("请求失败：" + error.message);
        });
});

//发送跳转请求
jumpBtn.addEventListener('click',()=>{
    window.location.href='WebDES_Break.html';
    }
);