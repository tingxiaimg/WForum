let config = require('~/config')
let url = config.apiURL

if (typeof window === 'undefined') {
    url = config.backApiURL
}

const api = {
    categoryCreate: { // 新增分类
        url: url + '/categories/create',
        method: 'POST'
    },
    categoryUpdate: { // 编辑分类
        url: url + '/categories/update',
        method: 'PUT'
    },
    getAdminCategories: {
        url: url + '/categories/admin/list',
        method: 'GET',
        desc: '管理员获取分类列表'
    },
    categoryStatus: {
        url: url + '/category/admin/status/update',
        method: 'post',
        desc: '更改分类状态'
    },
    getAdminArticles: {
        url: url + '/articles/admin/list',
        method: 'GET',
        desc: '获取文章列表'
    },
    updateArticleStatus: { // 更新文章状态
        url: url + '/articles/status/update',
        method: 'PUT'
    },
    getComments: {
        url: url + '/comments/list',
        method: 'GET'
    },
    updateCommentStatus: { // 更新评论状态
        url: url + '/comments/update/status/:id',
        method: 'PUT'
    },
    getAdminUserList: {
        url: url + '/user/list',
        method: 'GET',
        desc: '获取用户列表'
    }
}

module.exports = api
