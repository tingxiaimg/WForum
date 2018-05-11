import UserRole from '~/constant/UserRole'
import request from '~/net/request'

export default function (context, next) {
    request.getUserInfo({client: context.req}).then((rsp) => {
        context.user = rsp.data.user || null
        if (!context.user) {
            context.redirect('/signin?ref=' + encodeURIComponent(context.req.url))
        } else {
            let user = context.user
            let admin = UserRole.USER_ROLE_ADMIN
            if (admin === user.role) {
                context.store.commit('user', context.user)
                context.store.commit('isAdminPage', true)
                next()
            } else {
                context.error({ statusCode: 403, message: 'forbidden' })
            }
        }
    }).catch((err) => {
        console.log(err)
        context.error({ statusCode: 404, message: 'Page not found' })
    })
}
