<template>

    <div v-if="replies.length && replies.length > 0" style="border: 2px solid #cbcbcb;padding: 15px 5px 5px 5px;background: #dfdfdf">
        <div :id="`reply-${item.id}`" class="comment-item" v-for="(item, index) in replies" style="background: white;padding: 5px 0 5px 5px">
            <div class="reply-user-icon-box">
                <a :href="'/user/' + item.userId" target="_blank" class="reply-user-icon">
                    <img :src="item.user.avatarUrl" alt="">
                </a>
            </div>
            <div class="reply-user-box" style="width: 750px">
                <div>
                    <a :href="'/user/' + item.user.id" target="_blank" class="reply-user-name">{{item.user.name}}</a>
                    <template v-if="item.replyUserId">
                        <span class="reply-time">回复</span>
                        <span><a :href="`/user/${item.replyUser.id}`" target="_blank" class="reply-user-name">@{{item.replyUser.name}}</a></span>
                    </template>
                    <span class="reply-time">{{index + 1}}楼•{{item.createdAt | getReplyTime}}</span>
                    <div class="comment-actions">
                        <div class="comment-reply" @click="submitUp(item)">
                            <Icon type="android-favorite" style="font-size: 20px;margin-top:-2px;" :color="item.isUp ? '#ff6969' : ''"></Icon>
                            <span class="comment-reply-txt">{{item.ups && item.ups > 0 ? item.ups : '0'}}</span>
                        </div>
                        <div v-if="user && user.id !== item.user.id" class="comment-reply" @click="onReplyUser(item)">
                            <Icon type="reply" style="font-size: 17px;"></Icon>
                            <span class="comment-reply-txt">回复</span>
                        </div>
                        <div v-if="user && user.id === item.userId" class="comment-delete" @click="onReplyDelete(item)">
                            <Icon type="android-delete" style="font-size: 17px;"></Icon>
                            <span class="comment-delete-txt">删除</span>
                        </div>
                    </div>
                </div>
                <div v-if="!item.editReplyVisible" class="wforum-editor wforum-richtxt" v-html="item.htmlContent"></div>
            </div>
        </div>
    </div>
    <Spin v-else style="height: 40px;overflow: hidden">
        <Icon type="load-c" size=18 class="wforum-spin-icon-load"></Icon>
        <div>Loading</div>
    </Spin>
</template>
<script>
    import htmlUtil from '~/utils/html'
    import request from '~/net/request'
    import dateTool from '~/utils/date'
    import MD from '~/utils/markdown-it'
    import config from '~/config'

    export default {
        props: {
            user: {
                type: Object
            },
            comment: {
                type: Object
            },
            replyCount: {
                type: Number
            }
        },
        data () {
            return {
                loading: false,
                formData: {
                    content: ''
                },
                formRule: {
                    content: [
                        { required: true, message: '请输入回复内容', trigger: 'blur' }
                    ]
                },
                replies: []
            }
        },
        methods: {
            getReply () {
                let _this = this
                return request.getReply({
                    params: {
                        commentID: this.comment.id
                    },
                    query: {
                        articleId: this.comment.articleId,
                        pageNo: 1,
                        pageSize: 200
                    }
                }).then(function (data) {
                    let replies = data.data.replies || []
                    Promise.all(replies).then(arr => {
                        let floorMap = {}
                        for (let i = 0; i < replies.length; i++) {
                            replies[i].replyVisible = false
                            replies[i].editReplyVisible = false
                            replies[i].htmlContent = MD.render(replies[i].content || '')
                            floorMap[replies[i].id] = i + 1
                        }
                        _this.replies = replies
                        _this.floorMap = floorMap
                    })
                }).catch(err => {
                    console.log(err)
                    this.$Message.error({
                        duration: config.messageDuration,
                        closable: true,
                        content: err
                    })
                })
            },
            onReplyUser (reply) {
                console.log(reply)
                // 对回复进行回复
                this.$emit('replyUser', reply)
            },
            onReplyDelete (reply) {
                this.$emit('deleteReply', reply)
            },
            submitUp (reply) {
                this.$emit('submitUp', reply, 'reply')
            }
        },
        mounted () {
            // 代码高亮
            window.hljs.initHighlightingOnLoad()

            setTimeout(() => {
                // 获取数据
                this.getReply()
            }, 500)
        },
        filters: {
            getReplyTime: dateTool.getReplyTime,
            entity2HTML: htmlUtil.entity2HTML
        },
        watch: {
            replyCount () {
                this.getReply()
            }
        }
    }
</script>

<style>
    @import '../../assets/styles/article/detail.css'
    .wforum-spin-icon-load{
        animation: ani-wforum-spin 1s linear infinite;
    }
    @keyframes ani-wforum-spin {
        from { transform: rotate(0deg);}
        50%  { transform: rotate(180deg);}
        to   { transform: rotate(360deg);}
    }
</style>
