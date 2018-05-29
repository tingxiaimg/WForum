<template>
    <div>
        <div>
            <div>
                <div class="topic-detail-box">
                    <div class="detail-title-box">
                        <div class="article-detail-title"><h1>{{article.name | entity2HTML}}</h1></div>
                        <p class="article-title-info">
                            <span class="article-title-info-item">
                                发布于&nbsp;{{article.createdAt | getReplyTime}}
                            </span>
                            <span class="article-title-info-item">
                                作者&nbsp;{{article.user.name}}
                            </span>
                            <span class="article-title-info-item">
                                {{article.browseCount}}次&nbsp;浏览
                            </span>
                            <span class="article-title-info-item">
                                版块&nbsp;{{article.categories[0].name}}
                            </span>
                            <span v-if="article.status != 1" class="article-title-info-item" style="color: #df2e00">
                                状态&nbsp;{{article.status | statusName}}
                            </span>
                        </p>
                    </div>
                    <div v-if="canRead" class="home-articles-box">
                        <div class="wforum-editor wforum-richtxt" v-html="article.htmlContent"></div>
                    </div>
                    <div v-if="canRead" class="article-actions">
                        <div class="article-share">
                            <div class="article-share-btn" @click="collect">
                                <Icon type="android-star-outline" style="font-size: 20px;margin-top:-2px;" :color="alreadyCollect ? '#5cadff' : ''"></Icon>
                                <span  :style="alreadyCollect ? { color:'#5cadff'} : {}">{{alreadyCollect ? '取消收藏' : '收藏'}}</span>
                            </div>
                            <div class="article-share-btn" @click="onShare(article.name, article.user.name)">
                                <Icon type="android-share-alt" style="font-size: 16px"></Icon>
                                <span>分享</span>
                            </div>
                            <template v-if="isAuthor">
                                <div class="article-share-btn">
                                    <Icon type="edit" style="font-size: 16px"></Icon>
                                    <a :href="'/topic/edit/' + article.id"><span>编辑</span></a>
                                </div>
                                <div class="article-share-btn">
                                    <Icon type="android-delete" style="font-size: 17px;"></Icon>
                                    <span @click="onDelete">删除</span>
                                </div>
                            </template>
                        </div>
                    </div>
                </div>

                <div v-if="canRead" class="wforum-cell comment-box">
                    <div class="title total-reply-count">{{article.commentCount > 0 ? article.commentCount : '暂无'}}评论</div>
                    <div class="comment-content">
                        <template v-if="article.commentCount > 0 && canRead">
                            <div :id="`reply-${item.id}`" class="comment-item" v-for="(item, index) in article.comments">
                                <div class="reply-user-icon-box">
                                    <a :href="'/user/' + item.user.id" target="_blank" class="reply-user-icon">
                                        <img :src="item.user.avatarUrl" alt="">
                                    </a>
                                </div>
                                <div class="reply-user-box">
                                    <div>
                                        <a :href="'/user/' + item.user.id" target="_blank" class="reply-user-name">{{item.user.name}}</a>
                                        <span class="reply-time">{{index + 1}}楼•{{item.createdAt | getReplyTime}}</span>
                                        <div class="comment-actions">
                                            <div class="comment-reply" @click="submitUp(item,'comment')">
                                                <Icon type="android-favorite" style="font-size: 20px;margin-top:-2px;" :color="item.isUp ? '#ff6969' : ''"></Icon>
                                                <span class="comment-reply-txt">{{item.ups && item.ups > 0 ? item.ups : '0'}}</span>
                                            </div>
                                            <div class="comment-reply" @click="onShowReply(item)">
                                                <Icon type="android-textsms" style="font-size: 20px;margin-top:-2px;"></Icon>
                                                <span class="comment-reply-txt">{{item.replyCount && item.replyCount > 0 ? item.replyCount : '0'}}</span>
                                            </div>
                                            <div v-if="user && user.id !== item.user.id" class="comment-reply" @click="onReplyUser(item)">
                                                <Icon type="reply" style="font-size: 17px;"></Icon>
                                                <span class="comment-reply-txt">回复</span>
                                            </div>
                                            <template v-if="user && user.id === item.user.id">
                                                <div class="comment-edit" @click="onCommentEdit(item)">
                                                    <Icon type="edit" style="font-size: 15px;"></Icon>
                                                    <span class="comment-edit-txt">编辑</span>
                                                </div>
                                                <div class="comment-delete" @click="onCommentDelete(item)">
                                                    <Icon type="android-delete" style="font-size: 17px;"></Icon>
                                                    <span class="comment-delete-txt">删除</span>
                                                </div>
                                            </template>
                                        </div>
                                    </div>
                                    <!--修改为回复需点击才能查看-->
                                    <div class="wforum-editor wforum-richtxt" v-html="item.htmlContent"></div>
                                    <template v-if="item.showReply && item.replyCount > 0">
                                        <article-reply
                                            :comment="replyComment"
                                            :user="user"
                                            :replyCount="item.replyCount"
                                            @replyUser="onReplyUser"
                                            @deleteReply="onCommentDelete"
                                            @submitUp="submitUp"/>
                                    </template>
                                    <div v-if="item.replyVisible || item.editReplyVisible">
                                        <div>
                                            <md-editor :user="user" :value="formData.content" @change="onContentChage" />
                                        </div>
                                        <Row>
                                            <Button @click="onEditOrSubmitReply(item)" type="primary">保存</Button>
                                            <Button @click="cancelReply" style="margin-left: 10px;" type="ghost">取消</Button>
                                        </Row>
                                    </div>
                                </div>
                            </div>
                        </template>
                        <template v-if="canRead">
                        <p class="not-signin" v-if="!article.commentCount && user">暂时还没有人评论过这个话题</p>
                        <p class="not-signin" v-if="!article.commentCount && !user">暂时还没有人评论过这个话题,&nbsp;要评论话题, 请先&nbsp;<a @click="onSignin">登录</a>&nbsp;或&nbsp;<a href="/signup">注册</a></p>
                        <p class="not-signin not-signin-border" v-if="article.commentCount && !user">要评论话题, 请先&nbsp;<a @click="onSignin">登录</a>&nbsp;或&nbsp;<a href="/signup">注册</a></p>
                        </template>
                        <template v-else>
                            <p class="not-signin">回复已被屏蔽</p>
                        </template>
                    </div>
                </div>
                <template v-if="article.status === 1">
                    <div class="wforum-cell comment-box" v-if="user && replyArticle">
                        <div class="title add-reply-title">添加评论</div>
                        <div class="comment-content">
                            <Form ref="formData" :model="formData" :rules="formRule">
                                <Form-item prop="content">
                                    <md-editor :user="user" :value="formData.content" @change="onContentChage" />
                                </Form-item>
                            </Form>
                            <Button type="primary" @click="onSubmitReply">发表评论</Button>
                        </div>
                    </div>
                </template>
            </div>
        </div>
        <Modal
            v-model="collectShow"
            class="collect-modal"
            title="添加收藏"
            @on-cancel="cancel">
            <Row
                class="not-signin-dividing collect-row"
                type="flex"
                justify="space-between"
                align="middle"
                v-for="(item, index) in collectDirList" key="index">
                <div>
                    <a :href="`/user/collect/${user.id}?folder=${item.id}`" target="_blank" class="collects-item-label">{{item.name}}</a>
                    <p class="collects-item-num">{{(item.collects && item.collects.length) || 0}}条内容</p>
                </div>
                <Button v-if="item.hasCollect" class="info-button" style="width: 80px" disabled="disabled">已收藏</Button>
                <Button v-else-if="!alreadyCollect" class="info-button" style="width: 80px" @click="createCollect(item.id)">收藏</Button>
            </Row>
            <Button
                type="primary"
                size="large"
                class="collect-dir-btn" @click="createCollectDir">创建收藏夹</Button>
            <div slot="footer"></div>
        </Modal>
        <Modal
            v-model="collectShowDir"
            class="collect-modal"
            title="创建新收藏夹"
            @on-cancel="cancel">
            <Form
                ref="CollectDir"
                :model="collectData"
                :rules="collectRule">
                <Form-item prop="title">
                    <i-input
                        v-model="collectData.title"
                        placeholder="收藏夹名称"
                        size="large"/>
                </Form-item>
            </Form>
            <Row type="flex" justify="space-between">
                <Button type="ghost" style="width:48%" @click="collect">返回</Button>
                <Button type="primary" style="width:48%" @click="submitCollectDir">确认创建</Button>
            </Row>
            <div slot="footer"></div>
        </Modal>
    </div>
</template>

<script>
    import config from '~/config'
    import htmlUtil from '~/utils/html'
    import ErrorCode from '~/constant/ErrorCode'
    import Editor from '~/components/Editor'
    import Reply from '~/components/article/Reply'
    import request from '~/net/request'
    import dateTool from '~/utils/date'
    import { trim } from '~/utils/tool'
    import MD from '~/utils/markdown-it'
    import { ArticleStatus } from '~/constant/Article'

    export default {
        data () {
            return {
                collectShowDir: false,
                collectShow: false,
                loading: false,
                formData: {
                    content: ''
                },
                formRule: {
                    content: [
                        { required: true, message: '请输入回复内容', trigger: 'blur' }
                    ]
                },
                collectData: {
                    title: ''
                },
                collectRule: {
                    title: [
                        { required: true, message: '请输入收藏夹名称', trigger: 'blur' }
                    ]
                },
                replyComment: {}
            }
        },
        validate ({ params }) {
            var hasId = !!params.id
            return hasId
        },
        asyncData (context) {
            return request.getArticle({
                client: context.req,
                params: {
                    id: context.params.id
                }
            }).then(function (data) {
                let article = data.data.article
                if (!article) {
                    context.error({ statusCode: 404, message: 'Page not found' })
                    return
                }
                article.htmlContent = MD.render(article.content || '')
                article.comments = data.data.comments || []
                let reqArr = [
                    request.getUserArticles({
                        client: context.req,
                        params: {
                            userID: article.userId
                        },
                        query: {
                            orderType: 0,
                            desc: true,
                            pageSize: 5
                        }
                    })
                ]
                if (context.user) {
                    reqArr.push(request.getFoldersSource({
                        client: context.req
                    }))
                }
                return Promise.all(reqArr).then(arr => {
                    let recentArticles = arr[0].data.articles
                    let collectDirList = []
                    let alreadyCollect = false
                    let alreadyCollectID = 0
                    if (arr[1]) {
                        collectDirList = arr[1].data.folders || []
                        collectDirList.map(item => {
                            item.hasCollect = false
                            item.collects.map(items => {
                                if (items.articleId === parseInt(context.params.id)) {
                                    item.hasCollect = true
                                    alreadyCollect = true
                                    alreadyCollectID = items.id
                                }
                            })
                        })
                    }
                    let isAuthor = context.user && context.user.id === article.user.id
                    let canRead = article.status === 1 ? true : isAuthor ? true : context.user ? context.user.role === 2 : false
                    let floorMap = {}
                    let commentCount = 0
                    for (let i = 0; i < article.comments.length; i++) {
                        commentCount++
                        article.comments[i].replyVisible = false
                        article.comments[i].editReplyVisible = false
                        article.comments[i].showReply = false
                        if (!article.comments[i].replyCount) {
                            article.comments[i].replyCount = 0
                        }
                        article.comments[i].htmlContent = MD.render(article.comments[i].content || '')
                        floorMap[article.comments[i].id] = i + 1
                    }
                    article.commentCount = commentCount
                    context.store.commit('topicAuthor', article.user)
                    context.store.commit('authorRecentArticles', recentArticles)
                    return {
                        isAuthor: isAuthor,
                        user: context.user,
                        replyArticle: true, // 直接回复话题的编辑器是否显示(即parentCommentID为0)
                        parentCommentID: 0,
                        replyUserID: 0,
                        floorMap: floorMap,
                        article: article,
                        recentArticles: recentArticles,
                        collectDirList: collectDirList,
                        alreadyCollect: alreadyCollect,
                        alreadyCollectID: alreadyCollectID,
                        canRead: canRead
                    }
                })
            }).catch(err => {
                console.log(err)
                context.error({ statusCode: 404, message: 'Page not found' })
            })
        },
        head () {
            return {
                title: this.article.name,
                link: [
                    { rel: 'stylesheet', href: '/styles/editor/simplemde.min.css' },
                    { rel: 'stylesheet', href: '/styles/highlight/codestyle.css' } // Solarized Light
                ],
                script: [
                    { src: '/javascripts/highlight/highlight.min.js' }
                ]
            }
        },
        methods: {
            onShowReply (comment) {
                this.replyComment = comment
                comment.showReply = !comment.showReply
            },
            onSignin () {
                location.href = '/signin?ref=' + encodeURIComponent(location.href)
            },
            onDelete () {
                if (this.loading) {
                    return
                }
                this.loading = true
                let self = this
                this.$Modal.confirm({
                    title: '删除话题',
                    content: '确定要删除这个话题?',
                    onOk () {
                        request.deleteArticle({
                            params: {
                                id: self.article.id
                            }
                        }).then(res => {
                            self.loading = false
                            if (res.status === ErrorCode.SUCCESS) {
                                self.$Message.success({
                                    duration: config.messageDuration,
                                    closable: true,
                                    content: '已删除!'
                                })
                                setTimeout(function () {
                                    location.href = '/'
                                }, 500)
                            } else {
                                self.$Message.error({
                                    duration: config.messageDuration,
                                    closable: true,
                                    content: res.message
                                })
                            }
                        }).catch(err => {
                            self.loading = false
                            err = '内部错误'
                            self.$Message.error({
                                duration: config.messageDuration,
                                closable: true,
                                content: err
                            })
                        })
                    },
                    onCancel () {

                    }
                })
            },
            onContentChage (content) {
                this.formData.content = content
            },
            onReplyUser (comment) {
                let commentID
                if (comment.sourceName === 'article') {
                    // 对评论进行回复
                    commentID = comment.id
                } else {
                    // 对回复进行回复
                    commentID = comment.parentId
                }
                for (let i = 0; i < this.article.comments.length; i++) {
                    this.article.comments[i].replyVisible = false
                    this.article.comments[i].editReplyVisible = false
                    if (this.article.comments[i].id === commentID) {
                        this.article.comments[i].replyVisible = true
                    }
                }
                this.parentCommentID = commentID
                this.replyUserID = comment.userId
                this.replyArticle = false
            },
            onCommentEdit (comment) {
                let commentID = comment.id
                for (let i = 0; i < this.article.comments.length; i++) {
                    this.article.comments[i].replyVisible = false
                    this.article.comments[i].editReplyVisible = false
                    if (this.article.comments[i].id === commentID) {
                        this.article.comments[i].editReplyVisible = true
                    }
                }
                this.parentCommentID = comment.parentID
                this.replyUserID = comment.replyUserId
                this.replyArticle = false
                this.formData.content = comment.content
            },
            // 关闭回复
            cancelReply () {
                for (let i = 0; i < this.article.comments.length; i++) {
                    this.article.comments[i].replyVisible = false
                    this.article.comments[i].editReplyVisible = false
                }
                this.parentCommentID = 0
                this.replyUserID = 0
                this.replyArticle = true
                this.formData.content = ''
            },
            onEditOrSubmitReply (comment) {
                if (comment.editReplyVisible) {
                    this.onSubmitEditReply(comment)
                } else {
                    this.onSubmitReply()
                }
            },
            // 编辑回复
            onSubmitEditReply (comment) {
                // 验证交给后台
                if (!this.loading) {
                    this.loading = true
                    request.commentEdit({
                        body: {
                            id: comment.id,
                            content: this.formData.content
                        }
                    }).then(res => {
                        if (res.status === ErrorCode.SUCCESS) {
                            for (let i = 0; i < this.article.comments.length; i++) {
                                this.article.comments[i].replyVisible = false
                                this.article.comments[i].editReplyVisible = false
                                if (this.article.comments[i].id === comment.id) {
                                    this.article.comments[i].content = this.formData.content
                                    this.article.comments[i].htmlContent = MD.render(this.formData.content)
                                }
                            }
                        } else if (res.status === ErrorCode.LOGIN_TIMEOUT) {
                            location.href = '/signin?ref=' + encodeURIComponent(location.href)
                        } else {
                            return Promise.reject(new Error(res.message))
                        }
                        this.loading = false
                        this.parentCommentID = 0
                        this.replyUserID = 0
                        this.replyArticle = true
                        this.formData.content = ''
                    }).catch(err => {
                        this.loading = false
                        if (err.message) {
                            this.$Message.error({
                                duration: config.messageDuration,
                                closable: true,
                                content: err.message
                            })
                        }
                    })
                }
            },
            // 直接评论，或对评论或回复进行回复
            onSubmitReply () {
                // 验证交给后台
                let self = this
                if (!this.loading) {
                    this.loading = true
                    let reqToComment = this.replyArticle ? request.commentCreate({
                        body: {
                            articleId: parseInt(this.$route.params.id),
                            content: this.formData.content,
                            sourceName: 'article'
                        }
                    }) : request.replyCreate({
                        body: {
                            articleId: parseInt(this.$route.params.id),
                            parentId: this.parentCommentID,
                            replyUserId: this.replyUserID,
                            content: this.formData.content,
                            sourceName: 'comment'
                        }
                    })
                    reqToComment.then(res => {
                        if (res.status === ErrorCode.SUCCESS) {
                            let comment = res.data.comment || {}
                            if (comment.sourceName === 'article') {
                                let commentID = comment.id
                                location.href = `/topic/${self.article.id}#reply-${commentID}`
                                setTimeout(() => {
                                    location.reload()
                                }, 100)
                            } else {
                                for (let i = 0; i < self.article.comments.length; i++) {
                                    self.article.comments[i].replyVisible = false
                                    self.article.comments[i].editReplyVisible = false
                                    if (self.article.comments[i].id === comment.parentId) {
                                        self.article.comments[i].replyCount++
                                    }
                                }
                                self.parentCommentID = 0
                                self.replyUserID = 0
                                self.replyArticle = true
                                self.formData.content = ''
                            }
                        } else if (res.status === ErrorCode.LOGIN_TIMEOUT) {
                            location.href = '/signin?ref=' + encodeURIComponent(location.href)
                        } else {
                            return Promise.reject(new Error(res.message))
                        }
                        this.loading = false
                    }).catch(err => {
                        this.loading = false
                        if (err.message) {
                            this.$Message.error({
                                duration: config.messageDuration,
                                closable: true,
                                content: err.message
                            })
                        }
                    })
                }
            },
            onCommentDelete (comment) {
                if (this.loading) {
                    return
                }
                this.loading = true
                let self = this
                let id = comment.id
                let title = comment.sourceName === 'article' ? '评论' : '回复'
                this.$Modal.confirm({
                    title: `删除${title}`,
                    content: `确定要删除这个${title}?`,
                    onOk () {
                        request.deleteComment({
                            params: {
                                id: id
                            }
                        }).then((res) => {
                            if (res.status === ErrorCode.SUCCESS) {
                                if (comment.sourceName === 'article') {
                                    location.href = `/topic/${self.article.id}`
                                    setTimeout(() => {
                                        location.reload()
                                    }, 100)
                                } else {
                                    for (let i = 0; i < self.article.comments.length; i++) {
                                        self.article.comments[i].replyVisible = false
                                        self.article.comments[i].editReplyVisible = false
                                        if (self.article.comments[i].id === comment.parentId) {
                                            self.article.comments[i].replyCount--
                                        }
                                    }
                                    self.parentCommentID = 0
                                    self.replyUserID = 0
                                    self.replyArticle = true
                                    self.formData.content = ''
                                    self.loading = false
                                }
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
            },
            collect () {
                let self = this
                if (!this.user) {
                    location.href = '/signin?ref=' + encodeURIComponent(location.href)
                    return
                }
                if (this.alreadyCollect) {
                    this.$Modal.confirm({
                        title: '取消收藏',
                        content: '确认要取消收藏?',
                        onOk () {
                            request.cancelCollect({
                                params: {
                                    id: self.alreadyCollectID
                                }
                            }).then(res => {
                                if (res.status === ErrorCode.SUCCESS) {
                                    self.alreadyCollect = false
                                    self.alreadyCollectID = 0
                                    let collectDirList = self.collectDirList
                                    collectDirList.map(item => {
                                        item.hasCollect = false
                                        let collects = item.collects
                                        item.collects = collects.filter(citem => {
                                            return citem.articleId !== parseInt(self.$route.params.id)
                                        })
                                    })
                                    collectDirList.map(item => {
                                        item.hasCollect = false
                                    })
                                    self.$Message.success({
                                        duration: config.messageDuration,
                                        closable: true,
                                        content: '已取消收藏'
                                    })
                                } else {
                                    self.$Message.error({
                                        duration: config.messageDuration,
                                        closable: true,
                                        content: res.message
                                    })
                                }
                            }).catch(err => {
                                self.$Message.error({
                                    duration: config.messageDuration,
                                    closable: true,
                                    content: err.message || err.msg
                                })
                            })
                        },
                        cancel () {
                        }
                    })
                    return
                }
                this.hideCreateCollectDir()
            },
            hideCreateCollectDir () {
                this.collectShowDir = false
                this.collectData.title = ''
                this.collectShow = true
            },
            cancel () {
                this.collectShowDir = false
                this.collectShow = false
                this.collectData.title = ''
            },
            createCollectDir () {
                this.collectShowDir = true
                this.collectShow = false
            },
            submitCollectDir () {
                this.$refs['CollectDir'].validate(valid => {
                    if (!this.loading && valid) {
                        this.loading = true
                        request.createCollectDir({
                            body: {
                                name: trim(this.collectData.title),
                                parentId: 0
                            }
                        }).then(res => {
                            this.loading = false
                            if (res.status === ErrorCode.SUCCESS) {
                                let collectDir = res.data.folder
                                collectDir.hasCollect = false
                                collectDir.collects = collectDir.collects || []
                                this.collectDirList.unshift(collectDir)
                                this.hideCreateCollectDir()
                            } else {
                                this.$Message.error({
                                    duration: config.messageDuration,
                                    closable: true,
                                    content: res.message
                                })
                            }
                        }).catch(err => {
                            this.loading = false
                            this.$Message.error({
                                duration: config.messageDuration,
                                closable: true,
                                content: err.message || err.msg
                            })
                        })
                    }
                })
            },
            createCollect (id) {
                if (this.loading) {
                    return
                }
                this.loading = true
                request.createCollect({
                    body: {
                        articleId: parseInt(this.$route.params.id),
                        folderId: id
                    }
                }).then(res => {
                    this.loading = false
                    if (res.status === ErrorCode.SUCCESS) {
                        let collectDirList = this.collectDirList || []
                        for (let i = 0; i < collectDirList.length; i++) {
                            if (collectDirList[i].id === id) {
                                collectDirList[i].hasCollect = true
                                collectDirList[i].collects.push(res.data.collect)
                                break
                            }
                        }
                        this.alreadyCollect = true
                        this.alreadyCollectID = res.data.collect.id
                    } else {
                        this.$Message.error({
                            duration: config.messageDuration,
                            closable: true,
                            content: res.message
                        })
                    }
                }).catch(err => {
                    this.loading = false
                    this.$Message.error({
                        duration: config.messageDuration,
                        closable: true,
                        content: err.message || err.msg
                    })
                })
            },
            submitUp (item, type) { // 点赞
                if (this.loading) {
                    return
                }
                if (type && (type === '' || type === null)) {
                    return
                }
                this.loading = true
                request.submitUp({
                    body: {
                        id: item.id,
                        type: trim(type)
                    }
                }).then(res => {
                    this.loading = false
                    if (res.status === ErrorCode.SUCCESS) {
                        item.isUp = !item.isUp
                        if (item.isUp) {
                            item.ups += 1
                        } else {
                            item.ups += -1
                        }
                    } else {
                        this.$Message.error({
                            duration: config.messageDuration,
                            closable: true,
                            content: res.message
                        })
                    }
                }).catch(err => {
                    this.loading = false
                    this.$Message.error({
                        duration: config.messageDuration,
                        closable: true,
                        content: err.message || err.msg
                    })
                })
            },
            onShare (name, author) {
                if (name == null) {
                    return
                }
                let p = {
                    url: location.href,
                    showcount: '1',
                    desc: '',
                    summary: `来自WForum，作者：${author}`,
                    title: name.trim(),
                    site: 'WForum',
                    pics: '',
                    style: '203',
                    width: 98,
                    height: 22
                }
                let s = []
                for (let i in p) {
                    s.push(i + '=' + encodeURIComponent(p[i] || ''))
                }
                window.open('http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?' + s.join('&'), '_blank')
            }
        },
        mounted () {
            // 代码高亮
            window.hljs.initHighlightingOnLoad()
            setTimeout(() => {
                let hash = location.hash || ''
                if (hash.length > 1) {
                    hash = hash.substring(1)
                }
                let replyDOM = document.getElementById(hash)
                replyDOM && replyDOM.scrollIntoView && replyDOM.scrollIntoView()
            }, 1000)
        },
        filters: {
            getReplyTime: dateTool.getReplyTime,
            entity2HTML: htmlUtil.entity2HTML,
            statusName (statusCode) {
                let status = ''
                switch (statusCode) {
                case ArticleStatus.ArticleVerifying:
                    status = '审核中'
                    break
                case ArticleStatus.ArticleVerifyFail:
                    status = '未通过'
                    break
                default: status = ''
                }
                return status
            }
        },
        components: {
            'md-editor': Editor,
            'article-reply': Reply
        }
    }
</script>

<style>
    @import '../../assets/styles/article/detail.css'
</style>
