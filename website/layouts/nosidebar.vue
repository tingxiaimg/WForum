<template>
    <div>
        <app-header />
        <div class="common-body" style="margin-bottom: 20px;">
            <nuxt/>
        </div>
        <app-footer />
        <BackTop></BackTop>
    </div>
</template>

<script>
    import Header from '~/components/Header'
    import Footer from '~/components/Footer'

    export default {
        data () {
            return {
                user: this.$store.state.user,
                userLoginVisible: !this.$store.state.user,
                messages: this.$store.state.messages,
                messageCount: this.$store.state.messageCount
            }
        },
        head () {
            return {
                titleTemplate: '%s - ' + 'WForum',
                meta: [
                    { hid: 'description', name: 'description', content: 'WForum' },
                    { name: 'keywords', content: 'WForum' }
                ]
            }
        },
        middleware: 'userData',
        components: {
            'app-header': Header,
            'app-footer': Footer
        }
    }
</script>
