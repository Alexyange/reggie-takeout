function getUserData(phone) {
    return $axios({
        'url': `/rideruser/${phone}`,
        'method': 'get',
    })
}

//根据phone去修改身份证号
function updateByPhone(data) {
    return $axios({
        'url': '/rideruser/info',
        'method': 'put',
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': `/rideruser/logout`,
        'method': 'get',
    })
}