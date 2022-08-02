function getAllData() {
    return $axios({
        'url': '/riderorder/alldata',
        'method': 'get',
    })
}

function getUserData(phone) {
    return $axios({
        'url': `/rideruser/${phone}`,
        'method': 'get',
    })
}

function addOrder(data) {
    return $axios({
        'url': '/riderorder/add',
        'method': 'post',
        data
    })
}

function getCompleteSum() {
    return $axios({
        'url': '/riderorder/getcompletesum',
        'method': 'get',
    })
}