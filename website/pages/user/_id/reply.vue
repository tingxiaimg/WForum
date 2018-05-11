<template>
    <div class="articles-container">
        <div class="article-top">
            <div>{{user && user.id == this.currentId ? '我' : (sex ? '她' : '他')}}的评论和回复</div>
        </div>
        <template v-if="comments.length > 0">
            <div v-for="(comment, index) in comments" class="articles-item" :class="{'articles-item-no': index === 0}">

                <div class="articles-title">
                    <span style="font-size: 16px;" v-if="comment.sourceName === 'article'">[评论]</span>
                    <span style="font-size: 16px;" v-else-if="comment.sourceName === 'comment'">[回复]</span>
                    <a style="font-weight:bold;" v-if="comment.sourceName === 'article' && !comment.noSource" :href="`/topic/${comment.articleId}#reply-${comment.id}`" target="_blank">{{comment.articleName}}</a>
                    <a style="font-weight:bold;" v-else-if="comment.sourceName === 'comment'" :href="comment.noComment ? `/topic/${comment.articleId}` : `/topic/${comment.articleId}#reply-${comment.parentId}`" target="_blank">{{comment.articleName}}</a>

                    <template v-if="comment.sourceName === 'article' && comment.noSource">
                        <span class="reply-no-source">原话题已被作者删除。</span>
                    </template>
                    <span v-else-if="comment.sourceName === 'comment' && comment.noComment" class="reply-no-source">原评论已被作者删除，点击浏览话题。</span>
                    <span @click="deletComment(comment.id)" style="cursor: pointer;font-size: 16px;">[删除]</span>
                </div>
                <p class="articles-user-info">
                    <img class="articles-user-info-img" :src="comment.user.avatarUrl" alt="">
                    <a class="articles-user-info-name">{{comment.user.name}}</a>
                    <span v-if="comment.sourceName === 'comment'">
                        <span class="articles-user-info-name">回复</span><a :href="`/user/${comment.replyUserId}`" target="_blank" class="articles-user-info-name" style="color: #0f88eb">@{{comment.replyUser.name}}</a>
                    </span>
                </p>
                <div class="wforum-digest" v-html="comment.content"></div>
            </div>
            <div style="text-align: center;">
                <Page class="common-page"
                    :current="pageNo"
                    :page-size="pageSize"
                    :total="totalCount"
                    @on-change="onPageChange"
                    :show-elevator="true"/>
            </div>
        </template>
        <div v-else class="articles-item-empty">
            还没有过评论或回复
        </div>
    </div>
</template>

<script>
    import trimHtml from 'trim-html'
    import request from '~/net/request'
    import htmlUtil from '~/utils/html'
    import MD from '~/utils/markdown-it'
    import ErrorCode from '~/constant/ErrorCode'
    import config from '~/config'

    export default {
        data () {
            return {
                sex: 0
            }
        },
        asyncData (context) {
            return request.getMineComment({
                client: context.req,
                params: {
                    userID: context.params.id
                },
                query: {
                    orderType: 1,
                    desc: 1,
                    pageNo: context.query.pageNo || 1,
                    pageSize: 20
                }
            }).then(res => {
                let comments = res.data.comments || []
                for (let i = 0; i < comments.length; i++) {
                    let sourceName = 'topic'
                    let theID = comments[i].articleId
                    let limit = 100
                    let replyId = comments[i].sourceName === 'article' ? comments[i].id : comments[i].parentId
                    let hash = `/#reply-${replyId}`
                    if (comments[i].sourceName === 'comment' && !comments[i].parentComment) {
                        hash = ''
                    }
                    let more = `...&nbsp;&nbsp;<a href="/${sourceName}/${theID}${hash}" target="_blank"  class="wforum-digest-continue">继续阅读»</a>`

                    // 没有articleName说明原话题被删除
                    let noSource = !comments[i].articleName
                    comments[i].noSource = noSource
                    if (comments[i].sourceName === 'comment') {
                        let noComment = !comments[i].parentComment
                        comments[i].noComment = noComment
                    }
                    let trimObj = trimHtml(MD.render(comments[i].content), {
                        limit: limit,
                        suffix: !noSource ? more : '',
                        moreLink: false
                    })
                    let content = trimObj.html
                    content = htmlUtil.trimImg(content)
                    if (!trimObj.more) {
                        let newTrimObj = trimHtml(comments[i].content, {
                            limit: limit,
                            preserveTags: false
                        })
                        content = newTrimObj.html + (!noSource ? more : '')
                    }
                    comments[i].content = content
                }
                return {
                    userId: context.params.id,
                    pageNo: res.data.pageNo,
                    pageSize: res.data.pageSize,
                    totalCount: res.data.total,
                    comments: comments,
                    user: context.user,
                    currentId: context.params.id
                }
            }).catch(err => {
                console.log(err)
                context.error({ statusCode: 404, message: 'Page not found 0x005' })
            })
        },
        mounted () {
            this.$data.sex = this.$parent.currentUser.sex
        },
        methods: {
            onPageChange (value) {
                let userId = this.userId
                window.location.href = `/user/${userId}/reply?pageNo=${value}`
            },
            deletComment (id) {
                let self = this
                this.$Modal.confirm({
                    title: `删除`,
                    content: `确定要删除吗?`,
                    onOk () {
                        request.deleteComment({
                            params: {
                                id: id
                            }
                        }).then((res) => {
                            if (res.status === ErrorCode.SUCCESS) {
                                location.href = `/user/${self.$route.params.id}/reply`
                                setTimeout(() => {
                                    location.reload()
                                }, 100)
                            } else if (res.status === ErrorCode.LOGIN_TIMEOUT) {
                                location.href = '/signin?ref=' + encodeURIComponent(location.href)
                            } else {
                                return Promise.reject(new Error(res.msg))
                            }
                        }).catch(err => {
                            self.loading = false
                            self.$Message.error({
                                duration: config.messageDuration,
                                closable: true,
                                content: err.message || err.msg
                            })
                        })
                    },
                    onCancel () {
                        self.loading = false
                    }
                })
            }
        }
    }
</script>
