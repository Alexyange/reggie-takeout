function loginApi(data) {
    return $axios({
        'url': '/rideruser/login',
        'method': 'post',
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': '/rideruser/loginout',
        'method': 'post',
    })
}

  