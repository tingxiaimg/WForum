<template>
    <div>
        <nuxt/>
        <app-footer />
    </div>
</template>

<script>
    import Footer from '~/components/Footer'

    export default {
        data () {
            return {
                user: this.$store.state.user
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
        middleware: 'appData',
        components: {
            'app-footer': Footer
        }
    }
</script>
