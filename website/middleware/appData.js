import request from '~/net/request'
import config from '~/config'

export default function (context, next) {
    Promise.all([
        request.getUserInfo({client: context.req}),
        request.getMessages({client: context.req}),
        request.getTop10({client: context.req}),
        request.getMaxComment({client: context.req, params: {size: 5}}),
        request.getMaxBrowse({client: context.req, params: {size: 5}})
    ]).then((arr) => {
        context.user = arr[0].data.user || null
        let messages = arr[1].data ? arr[1].data.messages || [] : []
        let messageCount = arr[1].data ? arr[1].data.total || 0 : 0
        let top10Users = arr[2].data.users || []
        let maxCommentArticles = arr[3].data.articles || []
        let maxBrowseArticles = arr[4].data.articles || []
        context.store.commit('messages', messages)
        context.store.commit('messageCount', messageCount)
        if (context.user && context.user.avatarUrl) {
            context.user.avatarUrl = config.apiURL + context.user.avatarUrl
        }
        // console.log(context.user.avatarUrl)
        context.store.commit('user', context.user)
        context.store.commit('top10Users', top10Users)
        context.store.commit('maxCommentArticles', maxCommentArticles)
        context.store.commit('maxBrowseArticles', maxBrowseArticles)
        next()
    }).catch((err) => {
        console.log(err)
        context.error({ statusCode: 404, message: 'Page not found' })
    })
}
