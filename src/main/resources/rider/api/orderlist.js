function getAllData(data) {
    return $axios({
        'url': '/riderorder/alldataByrider',
        'method': 'post',
        data
    })
}

function getUserData(phone) {
    return $axios({
        'url': `/rideruser/${phone}`,
        'method': 'get',
    })
}
