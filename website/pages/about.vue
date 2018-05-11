<template>
    <div>
        <div class="about-box" style="text-align: center">
            <div class="about-main" style="text-align: left">
                <ul class="about-nav">
                    <li><a href="/"><span>首页</span></a></li>
                    <li class="about-nav-sep"><span>/</span></li>
                    <li><span class="about-label">关于</span></li>
                </ul>
                <div class="about-center">
                    <h1 class="about-title">关于WForum</h1>
                    <p class="about-text">WForum一个提供<em>分享</em>、<em>互动</em>、<em>学习</em>的平台。</p>
                    <p class="about-text">WForum希望能够为大家提供一个<em>开放</em>、<em>学习</em>、<em>交流</em>的平台。</p>
                    <p class="about-text"> WForum的技术架构是前后端分离的, 前端使用vue、iview、node.js、nuxt等技术来开发, 后端使用springBoot,myBatis,JWT等技术来开发。WForum的技术选型也是超前的, 大胆的使用nuxt来做<em>前后端同构渲染</em>。</p>
                    <p class="about-text">最后,祝愿大家快乐过每一天！</p>
                    <div class="about-wforum">
                        <pre>
       ＷＷＷＷＷ　ＷＷＷＷＷ　　ＷＷＷＷ　　　　ＷＷＷＷＷＷＷＷＷＷＷＷＷ　
　　　　ＷＷＷ　　　ＷＷＷ　　　ＷＷＷ　　　　　 ＷＷＷＷＷＷＷＷＷＷＷＷ
　　　　ＷＷＷ　　　ＷＷＷ　　　ＷＷ　　　　　　 ＷＷ               ＷＷ
　　　　ＷＷＷ　　　ＷＷＷ　　　ＷＷ　　　　　　 ＷＷ
　　　　　ＷＷ　　ＷＷＷＷ　　　ＷＷ　　　　　　 ＷＷ　　　　　ＷＷ
　　　　　ＷＷＷ　ＷＷＷＷＷ　ＷＷ　　　　　　　 ＷＷＷＷＷＷＷＷＷ
　　　　　ＷＷＷ　ＷＷＷＷＷ　ＷＷ　　　　　　　 ＷＷ　　　　　ＷＷ
　　　　　ＷＷＷＷＷＷ　ＷＷ　ＷＷ　　　　　　　 ＷＷ　　　　　 ＷＷ
　　　　　　ＷＷＷＷ　　ＷＷＷＷＷ　　　　　　　 ＷＷ
　　　　　　ＷＷＷＷ　　ＷＷＷＷ　　　　　　　　 ＷＷ
　　　　　　ＷＷＷＷ　　　ＷＷＷ　　　　　　　　 ＷＷ
　　　　　　　ＷＷ　　　　ＷＷＷ　　　　　　　 ＷＷＷ
　　　　　　　ＷＷ　　　　ＷＷ　　　　　　　 ＷＷＷＷＷ
　　　　　　　ＷＷ　　　　　Ｗ　           ＷＷＷＷＷＷＷ
                            　　
                        </pre>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        /*
        asyncData (context) {
            context.store.commit('publishTopicVisible', false)
            context.store.commit('maxBrowseVisible', false)
            context.store.commit('maxCommentVisible', false)
            return {}
        },
        */
        head () {
            return {
                title: '关于'
            }
        },
        mounted () {
        },
        layout: 'nosidebar'
    }
</script>

<style>
    @import '../assets/styles/about.css'
</style>
