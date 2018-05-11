var config = {
    // 生产环境改为 /api
    apiURL: '/api',
    backApiURL: '/api',
    useProxy: false,
    tokenCookieName: 'token',
    tokenMaxAge: 3600, // token多久过期，单位秒
    messageDuration: 5,
    sizeConfig: {},
    sizeLimit: 3 * 1024 * 1024,
    sizeLimitTip: '3M',
    uploadURL: '/files/upload',
    uploadAvatar: '/files/upload/avatar',
    proxy: {
        host: '127.0.0.1',
        port: 80
    }
}

config.uploadURL = config.apiURL + config.uploadURL
config.uploadAvatar = config.apiURL + config.uploadAvatar

module.exports = config
