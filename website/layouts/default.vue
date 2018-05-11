<template>
    <div>
        <app-header />
        <div class="common-body">
            <div class="common-body-left">
                <nuxt ref="content"/>
            </div>
            <app-sidebar />
        </div>
        <app-footer />
        <BackTop></BackTop>
    </div>
</template>

<script>
    import Header from '~/components/Header'
    import Footer from '~/components/Footer'
    import Sidebar from '~/components/Sidebar'

    export default {
        head () {
            return {
                titleTemplate: '%s - ' + 'WForum',
                meta: [
                    { hid: 'description', name: 'description', content: 'WForum' },
                    { name: 'keywords', content: 'WForum' }
                ]
            }
        },
        middleware: 'appData',
        components: {
            'app-header': Header,
            'app-footer': Footer,
            'app-sidebar': Sidebar
        }
    }
</script>
