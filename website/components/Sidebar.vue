<template>
    <div class="common-body-right">
        <template v-if="!user && userLoginVisible">
            <div class="wforum-cell wforum-user-info">
                <p>wforum中文社区</p>
                <p>您可以<a class="wforum-user-info-action sidebar-user-signin" @click="onSignin">登录</a>或<a class="wforum-user-info-action sidebar-user-signup" href="/signup">注册</a></p>
            </div>
        </template>
        <div v-if="user && publishTopicVisible" class="wforum-user-publish">
            <a href="/topic/create"><button class="signup-button ivu-btn ivu-btn-primary ivu-btn-large">发布话题</button></a>
        </div>
        <div v-if="topicAuthor" class="wforum-cell">
            <div class="title">作者信息</div>
            <div class="wforum-user-info" style="padding-top:16px;">
                <div>
                    <a :href="'/user/' + topicAuthor.id" target="_blank" class="wforum-sidebar-icon-box">
                        <img class="wforum-user-info-icon" :src="topicAuthor.avatarUrl" :alt="topicAuthor.name">
                    </a>
                    <span class="wforum-sidebar-info-box">
                        <a :href="'/user/' + topicAuthor.id" target="_blank">{{topicAuthor.name}}</a>
                    </span>
                </div>
                <div class="wforum-user-line author-info-text">
                    积分: {{topicAuthor.score}}
                </div>
                <div class="wforum-user-line author-info-text text-italic">{{topicAuthor.signature || '这家伙很懒，什么个性签名都没有留下'}}</div>
            </div>
        </div>
        <div v-if="authorRecentArticles && authorRecentArticles.length" class="wforum-cell">
            <div class="title">作者近期话题</div>
            <ul>
                <li v-for="topic in authorRecentArticles" class="wforum-cell-item"><a :href="`/topic/${topic.id}`" target="_blank" class="sidebar-articles-title">{{topic.name}}</a></li>
            </ul>
        </div>
        <div v-if="top10Users && top10Visible" class="wforum-cell">
			<div class="title">积分榜<a class="top100-link" href="/rank" target="_blank">TOP 100>></a></div>
			<ul>
				<li v-for="item in top10Users" class="wforum-cell-item">
					<span class="wforum-score-item">{{item.score}}</span>
					<span class="wforum-score-item"><a class="user-page-link" :href="'/user/' + item.id" target="_blank">{{item.name}}</a></span>
				</li>
			</ul>
        </div>
        <div v-if="maxBrowseVisible && maxBrowseArticles && maxBrowseArticles.length" class="wforum-cell">
			<div class="title">热门话题</div>
			<ul>
				<li class="wforum-cell-item" v-for="item in maxBrowseArticles"><a :href="`/topic/${item.id}`" target="_blank" class="sidebar-articles-title">{{item.name}}</a></li>
			</ul>
        </div>
        <div v-if="maxCommentVisible && maxCommentArticles && maxCommentArticles.length" class="wforum-cell">
            <div class="title">评论最多的话题</div>
            <ul>
                <li class="wforum-cell-item" v-for="item in maxCommentArticles"><a :href="`/topic/${item.id}`" target="_blank" class="sidebar-articles-title">{{item.name}}</a></li>
            </ul>
        </div>
        <div v-if="friendLinkVisible" class="wforum-cell">
            <div class="title">友情链接</div>
            <ul style="text-align: center;">
                <li class="wforum-cell-item"><a href="https://cnodejs.org" target="_blank" class="sidebar-articles-title"><img style="width: 150px;" src="https://cnodejs.org/public/images/cnodejs.svg"/></a></li>
                <li class="wforum-cell-item"><a href="https://ruby-china.org/" target="_blank" class="sidebar-articles-title"><img style="width: 150px;" src="https://dn-phphub.qbox.me/assets/images/friends/ruby-china.png"/></a></li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        data () {
            return {
                user: this.$store.state.user,
                top10Users: this.$store.state.top10Users,
                maxCommentArticles: this.$store.state.maxCommentArticles,
                maxBrowseArticles: this.$store.state.maxBrowseArticles,
                topicAuthor: this.$store.state.topicAuthor,
                authorRecentArticles: this.$store.state.authorRecentArticles,
                userLoginVisible: this.$store.state.userLoginVisible,
                publishTopicVisible: this.$store.state.publishTopicVisible,
                top10Visible: this.$store.state.top10Visible,
                maxBrowseVisible: this.$store.state.maxBrowseVisible,
                maxCommentVisible: this.$store.state.maxCommentVisible,
                friendLinkVisible: this.$store.state.friendLinkVisible,
                statVisible: this.$store.state.statVisible
            }
        },
        mounted () {
        },
        methods: {
            onSignin () {
                location.href = '/signin?ref=' + encodeURIComponent(location.href)
            }
        }
    }
</script>

<style>

</style>
