function getOrderData(orderId) {
    return $axios({
        'url': `/riderorder/${orderId}`,
        'method': 'get',
    })
}

function toMeal(id) {
    return $axios({
        'url': `/riderorder/tomeal/${id}`,
        'method': 'get',
    })
}

function toComplete(id) {
    return $axios({
        'url': `/riderorder/tocomplete/${id}`,
        'method': 'get',
    })
}
