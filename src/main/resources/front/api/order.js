//提交订单
function addOrderApi(data) {
    return $axios({
        'url': '/order/submit',
        'method': 'post',
        data
    })
}

//查询所有订单
function orderListApi() {
    return $axios({
        'url': '/order/list',
        'method': 'get',
    })
}

//分页查询订单
function orderPagingApi(data) {
    return $axios({
        'url': '/order/userPage',
        'method': 'get',
        params: {...data}
    })
}

//再来一单
function orderAgainApi(data) {
    return $axios({
        'url': '/order/again',
        'method': 'post',
        data
    })
}

//获取用户信息
function getUser(data) {
    return $axios({
        'url': '/user/phone',
        'method': 'get',
        params: {...data}
    })
}

//根据phone去修改身份证号
function updateByPhone(data) {
    return $axios({
        'url': '/user/info',
        'method': 'put',
        data
    })
}

function updatepwdByPhone(data) {
    return $axios({
        'url': '/user/pwd',
        'method': 'put',
        data
    })
}
//兑换优惠券
function addCodeCoupons(data) {
    return $axios({
        'url': '/coupons/add',
        'method': 'get',
        params: {...data}
    })
}

//取出优惠券
function getCoupons() {
    return $axios({
        'url': '/coupons',
        'method': 'get',
    })
}

//删除优惠券
function deleteCoupons(data) {
    return $axios({
        'url': '/coupons',
        'method': 'delete',
        params: {...data}
    })
}

function addComments(data) {
    return $axios({
        'url': '/comments/add',
        'method': 'post',
        data
    })
}

function getCommentsNum(data) {
    return $axios({
        'url': '/comments/num',
        'method': 'post',
        data
    })
}

function toComplete(data) {
    return $axios({
        'url': '/order/tocomplete',
        'method': 'put',
        data
    })
}

function toCancle(data) {
    return $axios({
        'url': '/aliapy/fefund/' + data.id,
        'method': 'get',
    })
}

function toPay(data) {
    return $axios({
        'url': '/aliapy/pay',
        'method': 'post',
        data
    })
}

function getOrders(data) {
    return $axios({
        'url': '/order/getorder',
        'method': 'get',
        params: {...data}
    })
}

function getAddress(data) {
    return $axios({
        'url': `/addressBook/${data.id}`,
        'method': 'get',
    })
}


function toUpdateOrders(data) {
    return $axios({
        'url': '/order/toupdate',
        'method': 'post',
        data
    })
}