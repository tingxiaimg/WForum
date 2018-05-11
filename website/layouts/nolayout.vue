<template>
    <nuxt/>
</template>

<script>
    export default {
        head () {
            return {
                titleTemplate: '%s - ' + 'WForum',
                meta: [
                    { hid: 'description', name: 'description', content: 'WForum' },
                    { name: 'keywords', content: 'WForum' }
                ]
            }
        }
    }
</script>
