<template>
	<div class="wforum-top-header">
		<div class="wforum-top-box">
			<div class="wforum-top-header-left">
				<div class="wforum-logo-container">
                    <a href="/"><img src="/images/logo.png" /></a>
				</div>
				<div class="wforum-header-search">
					<form @submit.prevent="onSearch" action="" target="_blank" method="get" class="wforum-top-search">
                        <p style="position: relative;">
    						<input @focus="onInputFocus" @blur="onInputBlur" v-model="q" type="text" class="wforum-top-input" :style="{border: isInputFocus ? '1px #a2a2a2 solid' : '1px #e3e3e3 solid'}" name="topSearch">
                            <span class="search-icon"></span>
                        </p>
					</form>
				</div>
			</div>
            <div class="wforum-top-header-nav">
                <ul>
                    <li><a href="/">话题</a></li>
                </ul>
            </div>
            <div class="wforum-top-header-nav">
                <ul>
                    <li><a href="https://github.com/tingxiaimg/WForum">源码</a></li>
                </ul>
            </div>
			<div class="wforum-top-header-right">
                    <!--<li><a href="/" target="_blank">WForum源码</a></li> -->
                    <template v-if="user">
                        <li class="user-message-wrapbox">
                            <Tooltip v-if="userMessages.length" trigger="hover" placement="bottom">
                                <a href="" class="user-message-box">
                                    <Icon class="user-message" type="ios-bell-outline"></Icon>
                                    <span class="user-message-tip-count">{{messageCount}}</span>
                                </a>
                                <ul slot="content" class="header-message-list">
                                    <li v-for="message in userMessages">
                                        <p v-if="message.type === 'comment'" class="header-message-item">
                                            <a :href="`/user/${message.fromUser.id}`" target="_blank" class="header-message-user">
                                                {{message.fromUser.name}}
                                            </a>
                                                &nbsp;评论了你的话题&nbsp;
                                            <a @click="onReadMessage(message)" :href="!message.article ? '' : `/topic/${message.articleId}/#reply-${message.sourceId}`" target="_blank" class="header-message-content" :style="{color: message.readed ? '#a0a3a4' : ''}">
                                            {{message.article.name}}
                                            </a>
                                        </p>
                                        <p v-else-if="message.type === 'reply'" class="header-message-item">
                                            <a :href="`/user/${message.fromUser.id}`" target="_blank" class="header-message-user">
                                                {{message.fromUser.name}}
                                            </a>
                                                &nbsp;在话题：
                                            <a @click="onReadMessage(message)" class="header-message-content" :href="!message.article ? '' : `/topic/${message.articleId}/#reply-${message.sourceId}`" :style="{color: message.readed ? '#a0a3a4' : ''}" target="_blank">
                                                {{message.article.name}}
                                            </a>&nbsp;中回复了你
                                        </p>
                                        <p v-else-if="message.type === 'collect'" class="header-message-item">
                                            <a :href="`/user/${message.fromUser.id}`" target="_blank" class="header-message-user">
                                                {{message.fromUser.name}}
                                            </a>
                                            &nbsp;收藏了你的话题&nbsp;
                                            <a @click="onReadMessage(message)" :href="!message.article ? '' : `/topic/${message.articleId}`" target="_blank" class="header-message-content" :style="{color: message.readed ? '#a0a3a4' : ''}">
                                                {{message.article.name}}
                                            </a>
                                        </p>
                                    </li>
                                </ul>
                            </Tooltip>
                            <a v-else href="" class="user-message-box"><Icon class="user-message" type="ios-bell-outline"></Icon></a>
                        </li>
                        <li style="padding-right:0;">
                            <Tooltip v-if="user" trigger="hover" :title="user.name" placement="bottom">
                                <a :href="`/user/${user.id}`" class="header-usre-box">
                                    <span class="header-avatar">
                                        <img :src="user.avatarUrl" :alt="user.name">
                                    </span>
                                    <span class="header-user-name">{{user.name}}</span>
                                </a>
                                <ul slot="content" class="header-user-box">
                                    <li title="主页"><a :href="`/user/${user.id}`">个人主页</a></li>
                                    <li title="话题"><a href="/topic/create">发布话题</a></li>
                                    <li title="管理" v-if="adminVisible"><a href="/admin">后台管理</a></li>
                                    <li title="退出" @click="onSignout">退&nbsp&nbsp出</li>
                                </ul>
                            </Tooltip>
                        </li>
                    </template>
					<template v-else>
						<a @click="onSignin"><li style="color: #333;">登录</li></a>
                        <a href="/signup"><li style="color: #333;">注册</li></a>
					</template>
				</ul>
			</div>
		</div>
	</div>
</template>

<script>
    import UserRole from '~/constant/UserRole'
    import request from '~/net/request'
    import ErrorCode from '~/constant/ErrorCode'

    export default {
        data () {
            let user = this.$store.state.user
            let admins = [
                UserRole.USER_ROLE_ADMIN
            ]
            let adminVisible = false
            if (user && admins.indexOf(user.role) >= 0) {
                adminVisible = true
            }
            return {
                q: '',
                user: user,
                adminVisible: adminVisible,
                isInputFocus: false,
                userMessages: [],
                messages: this.$store.state.messages,
                messageCount: this.$store.state.messageCount
            }
        },
        methods: {
            onSearch () {
                // let searchURL = '/api/search/&q=' + encodeURIComponent(this.q)
                // window.open(searchURL)
                this.$Message.error({
                    content: '抱歉，该功能还未开放。',
                    duration: 1.5
                })
            },
            onInputFocus () {
                this.isInputFocus = true
            },
            onInputBlur () {
                this.isInputFocus = false
            },
            onReadMessage (message) {
                request.readMessage({
                    params: {
                        id: message.id
                    }
                }).then(res => {
                    if (res.status === ErrorCode.SUCCESS) {
                        message.readed = true
                    }
                })
            },
            onSignin () {
                location.href = '/signin?ref=' + encodeURIComponent(location.href)
            },
            onSignout () {
                request
                    .logout()
                    .then(res => {
                        if (res.status === ErrorCode.SUCCESS) {
                            this.user = null
                            window.location.href = '/signin'
                        }
                    })
                    .catch(err => {
                        console.log(err)
                    })
            }
        },
        mounted () {
            let messages = this.messages || []
            let userMessages = messages.slice(0)
            let maxLen = 15
            for (let i = 0; i < userMessages.length; i++) {
                if (userMessages[i].article) {
                    let title = userMessages[i].article.name || ''
                    if (title.length > maxLen) {
                        userMessages[i].article.name = title.substr(0, maxLen) + '...'
                    }
                }
                userMessages[i].readed = false
            }
            this.userMessages = userMessages
        }
    }
</script>

<style>

</style>
