import request from '~/net/request'

export default function (context, next) {
    Promise.all([
        request.getUserInfo({client: context.req}),
        request.getMessages({client: context.req})
    ]).then((arr) => {
        context.user = arr[0].data.user || null
        let messages = arr[1].data !== null ? arr[1].data.messages || [] : []
        let messageCount = arr[1].data !== null ? arr[1].data.total || 0 : 0
        context.store.commit('messages', messages)
        context.store.commit('messageCount', messageCount)
        context.store.commit('user', context.user)
        next()
    }).catch((err) => {
        console.log(err)
        context.error({ statusCode: 404, message: 'Page not found 0x007' })
    })
}
