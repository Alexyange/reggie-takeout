// 查询列表接口
const getCategoryPage = (params) => {
    return $axios({
        url: '/category/page',
        method: 'get',
        params
    })
}

// 编辑页面反查详情接口
const queryCategoryById = (id) => {
    return $axios({
        url: `/category/${id}`,
        method: 'get'
    })
}

// 删除当前列的接口
const deleCategory = (id) => {
    return $axios({
        url: '/category',
        method: 'delete',
        params: {id}
    })
}

// 修改接口
const editCategory = (params) => {
    return $axios({
        url: '/category',
        method: 'put',
        data: {...params}
    })
}

// 新增接口
const addCategory = (params) => {
    return $axios({
        url: '/category',
        method: 'post',
        data: {...params}
    })
}

function getData1(params) {
    return $axios({
        url: '/orderDetail/dish',
        method: 'get',
        params
    })
}

function getAmount1(params) {
    return $axios({
        url: '/orderDetail/dishamount',
        method: 'get',
        params
    })
}

function getData2(params) {
    return $axios({
        url: '/orderDetail/setmeal',
        method: 'get',
        params
    })
}

function getAmount2(params) {
    return $axios({
        url: '/orderDetail/setmealamount',
        method: 'get',
        params
    })
}