<template>
    <Row type="flex" align="middle" justify="center" class="wforum-signin-container">
        <Col :xs="24" :lg="6" :md="14">
            <a class="wforum-signin-title" href="/">WForum</a>
            <p class="wforum-signin-desc">和朋友分享你的知识、经验和见解</p>
        	<Form ref="formCustom" :model="formCustom" :rules="ruleCustom" class="signup-form">
        		<Form-item prop="username">
                    <i-input
                        size="large"
                        v-model="formCustom.username"
                        @on-blur="blur('formCustom.username')"
                        placeholder="用户名 / 邮箱"></i-input>
                </Form-item>
                <Form-item prop="passwd">
                    <i-input size="large" type="password" v-model="formCustom.passwd" placeholder="密码" @keydown.native="handleKeyUp"></i-input>
                </Form-item>
                <p style="text-align: right;padding-right: 2px;margin-top:10px;">
                    <a href="/signup" class="wforum-common-link" style="margin-right: 12px;">立即注册</a>
                </p>
                <Form-item style="margin-top: 10px">
                    <i-button size="large" type="primary" @click="handleSubmit('formCustom')" style="width: 100%">登&nbsp;&nbsp;录</i-button>
                </Form-item>
            </Form>
        </Col>
        <script type="text/javascript" color="51,133,255" opacity='0.7' zIndex="1" count="80" src="/javascripts/canvasnest/canvas-nest.min.js"></script>
    </Row>
</template>

<script>
    import ErrorCode from '~/constant/ErrorCode'
    import request from '~/net/request'
    import config from '~/config'
    import url from 'url'
    import {trim, trimBlur} from '~/utils/tool'

    export default {
        data () {
            return {
                loading: false,
                formCustom: {
                    passwd: '',
                    username: ''
                },
                success: false,
                ruleCustom: {
                    passwd: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' }
                    ]
                }
            }
        },
        asyncData (context) {
            let user = context.user
            let redirectURL

            let myURL = url.parse(context.req.url, true)
            if (myURL.query && myURL.query.ref) {
                redirectURL = decodeURIComponent(myURL.query.ref)
            } else {
                redirectURL = '/'
            }
            if (user) {
                context.redirect(redirectURL)
                return
            }
            return {
                user: user,
                ref: myURL.query.ref,
                redirectURL: redirectURL
            }
        },
        layout: 'nolayout',
        head () {
            return {
                title: '登录'
            }
        },
        methods: {
            handleSubmit (name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        if (this.loading) {
                            return
                        }
                        this.loading = true
                        request.signin({
                            query: {
                                loginType: this.formCustom.username.indexOf('@') > 0 ? 'email' : 'name'
                            },
                            body: {
                                signInInput: trim(this.formCustom.username),
                                password: trim(this.formCustom.passwd)
                            }
                        }).then(res => {
                            this.loading = false
                            if (res.status === ErrorCode.SUCCESS) {
                                this.$Message.success('你好！')
                                window.location.href = this.redirectURL
                            } else if (res.status === ErrorCode.LOGGED) {
                                window.location.href = '/'
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
            handleKeyUp (e) {
                if (e.keyCode === 13) {
                    return this.handleSubmit('formCustom')
                }
            },
            blur (name) {
                trimBlur(name, this)
            }
        }
    }
</script>

<style>
    @import '../assets/styles/signin.css'
</style>
