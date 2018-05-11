<template>
    <Menu theme="dark" :active-name="activeName" :open-names="openNames">
        <Submenu name="overview">
            <template slot="title">
                <Icon type="stats-bars"></Icon>
                网站概览
            </template>
            <router-link to="/admin">
                <Menu-item name="/admin">网站概览</Menu-item>
            </router-link>
        </Submenu>
        <Submenu name="category">
            <template slot="title">
                <Icon type="folder"></Icon>
                分类管理
            </template>
            <router-link to="/admin/category/list">
                <Menu-item name="/admin/category/list">分类列表</Menu-item>
            </router-link>
        </Submenu>
        <Submenu name="topic">
            <template slot="title">
                <Icon type="ios-paper"></Icon>
                话题管理
            </template>
            <router-link to="/admin/topic/list">
                <Menu-item name="/admin/topic/list">全部话题</Menu-item>
            </router-link>
            <router-link to="/admin/topic/today">
                <Menu-item name="/admin/topic/today">今日话题</Menu-item>
            </router-link>
            <router-link to="/admin/topic/yesterday">
                <Menu-item name="/admin/topic/yesterday">昨日话题</Menu-item>
            </router-link>
        </Submenu>
        <Submenu name="reply">
            <template slot="title">
                <Icon type="chatboxes"></Icon>
                回复管理
            </template>
            <router-link to="/admin/reply/list">
                <Menu-item name="/admin/reply/list">全部评论</Menu-item>
            </router-link>
            <router-link to="/admin/reply/today">
                <Menu-item name="/admin/reply/today">今日评论</Menu-item>
            </router-link>
            <router-link to="/admin/reply/yesterday">
                <Menu-item name="/admin/reply/yesterday">昨日评论</Menu-item>
            </router-link>
        </Submenu>
        <Submenu name="user">
            <template slot="title">
                <Icon type="ios-people"></Icon>
                用户管理
            </template>
            <router-link to="/admin/user/list">
                <Menu-item name="/admin/user/list">全部用户</Menu-item>
            </router-link>
            <router-link to="/admin/user/today">
                <Menu-item name="/admin/user/today">今日用户</Menu-item>
            </router-link>
            <router-link to="/admin/user/yesterday">
                <Menu-item name="/admin/user/yesterday">昨日用户</Menu-item>
            </router-link>
        </Submenu>
    </Menu>
</template>

<script>
    export default {
        props: ['activeName'],

        data () {
            return {
                openNames: ['overview', 'category', 'topic', 'reply', 'user', 'craler']
            }
        }
    }
</script>
