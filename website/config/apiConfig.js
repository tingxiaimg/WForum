let config = require('~/config')
let url = config.apiURL

if (typeof window === 'undefined') {
    url = config.backApiURL
}

const api = {
    getCategories: { // 获取分类列表
        url: url + '/categories/list',
        method: 'GET'
    },
    getArticles: { // 获取文章列表
        url: url + '/articles/list',
        method: 'GET'
    },
    getArticle: { // 获取文章信息
        url: url + '/articles/info/:id',
        method: 'GET'
    },
    deleteArticle: { // 删除文章
        url: url + '/articles/delete/:id',
        method: 'DELETE'
    },
    getTop10: { // 获取积分排名前10的用户
        url: url + '/user/score/top10',
        method: 'GET'
    },
    getTop100: { // 获取积分排名前100的用户
        url: url + '/user/score/top100',
        method: 'GET'
    },
    getUserArticles: { // 获取用户的文章列表
        url: url + '/articles/user/:userID',
        method: 'GET'
    },
    getUserInfo: { // 获取当前登录用户信息
        url: url + '/user/info',
        method: 'GET'
    },
    createArticle: { // 新建文章
        url: url + '/articles/create',
        method: 'POST'
    },
    signin: { // 登陆
        url: url + '/user/signIn',
        method: 'POST'
    },
    signup: { // 注册
        url: url + '/user/signUp',
        method: 'POST'
    },
    updateArticle: { // 编辑文章
        url: url + '/articles/update',
        method: 'PUT'
    },
    replyCreate: { // 回复提交
        url: url + '/comments/reply/create',
        method: 'POST'
    },
    commentCreate: { // 提交评论
        url: url + '/comments/create',
        method: 'POST'
    },
    commentEdit: { // 编辑评论
        url: url + '/comments/update',
        method: 'PUT'
    },
    deleteComment: { // 删除评论
        url: url + '/comments/delete/:id',
        method: 'DELETE'
    },
    getMaxComment: { // 回复最多的话题
        url: url + '/articles/max:size?by=comment',
        method: 'GET'
    },
    getMaxBrowse: { // 浏览最多的话题
        url: url + '/articles/max:size?by=browse',
        method: 'GET'
    },
    logout: { // 退出登录
        url: url + '/user/signOut',
        method: 'POST'
    },
    getTopList: { // 获取置顶文章列表
        url: url + '/articles/top/global',
        method: 'GET'
    },
    setTop: { // 设置置顶
        url: url + '/articles/top/:id',
        method: 'POST'
    },
    delTop: { // 取消置顶
        url: url + '/articles/deltop/:id',
        method: 'DELETE'
    },
    getMineComment: { // 获取当前用户回复
        url: url + '/comments/user/:userID',
        method: 'GET'
    },
    getReply: {
        url: url + '/comments/reply/:commentID',
        method: 'GET'
    },
    getPublicUser: { // 获取其他用户信息
        url: url + '/user/info/public/:id',
        method: 'GET'
    },
    userInfoDetail: { // 获取用户详情
        url: url + '/user/info/detail',
        method: 'GET'
    },
    updateInfo: { // 修改用户信息
        url: url + '/user/update/:type',
        method: 'PUT'
    },
    schoolAdd: { // 添加教育经历
        url: url + '/user/school/add',
        method: 'POST'
    },
    schoolDelete: { // 删除教育经历
        url: url + '/user/school/delete/:id',
        method: 'DELETE'
    },
    careerAdd: { // 增加工作经历
        url: url + '/user/career/add',
        method: 'POST'
    },
    careerDelete: { // 删除工作经历
        url: url + '/user/career/delete/:id',
        method: 'DELETE'
    },
    getCollectDirList: { // 查询用户的收藏夹列表
        url: url + '/collects/user/:userID/folders',
        method: 'GET'
    },
    getFoldersSource: {
        url: url + '/collects/folders/withsource', // 查询用户的收藏夹列表，并且返回每个收藏夹中收藏了哪些话题
        method: 'GET'
    },
    createCollectDir: { // 创建收藏夹
        url: url + '/collects/folder/create',
        method: 'POST'
    },
    createCollect: { // 收藏文章或收藏投票
        url: url + '/collects/create', // collect_source_article收藏文章
        method: 'POST'
    },
    cancelCollect: { // 取消收藏
        url: url + '/collects/delete/:id',
        method: 'DELETE'
    },
    collectList: { // 获取收藏夹下的话题
        url: url + '/collects/list',
        method: 'GET'
    },
    getMessages: { // 未读消息
        url: url + '/messages/unread',
        method: 'GET'
    },
    readMessage: { // 将消息标记为已读
        url: url + '/messages/read/:id',
        method: 'GET'
    },
    submitUp: {
        url: url + '/comments/up',
        method: 'POST'
    }
}

module.exports = api
