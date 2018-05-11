import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = () => new Vuex.Store({
    state: {
        isAdminPage: false,
        user: null,
        messages: [],
        messageCount: 0,
        top10Users: [],
        maxCommentArticles: [],
        maxBrowseArticles: [],
        topicAuthor: null,
        authorRecentArticles: [],
        votesMaxBrowse: [],
        votesMaxComment: [],

        userLoginVisible: true,
        publishTopicVisible: true,
        top10Visible: false,
        maxBrowseVisible: true,
        maxCommentVisible: true,
        friendLinkVisible: false,
        statVisible: false
    },
    mutations: {
        isAdminPage (state, isAdminPage) {
            state.isAdminPage = isAdminPage
        },
        messages (state, messages) {
            state.messages = messages
        },
        messageCount (state, messageCount) {
            state.messageCount = messageCount
        },
        user (state, user) {
            state.user = user
        },
        avatarUrl (state, url) {
            if (state.user) {
                state.user.avatarUrl = url
            }
        },
        top10Users (state, top10Users) {
            state.top10Users = top10Users
        },
        maxCommentArticles (state, maxCommentArticles) {
            state.maxCommentArticles = maxCommentArticles
        },
        maxBrowseArticles (state, maxBrowseArticles) {
            state.maxBrowseArticles = maxBrowseArticles
        },
        topicAuthor (state, topicAuthor) {
            state.topicAuthor = topicAuthor
        },
        authorRecentArticles (state, authorRecentArticles) {
            state.authorRecentArticles = authorRecentArticles
        },
        userLoginVisible (state, userLoginVisible) {
            state.userLoginVisible = userLoginVisible
        },
        publishTopicVisible (state, publishTopicVisible) {
            state.publishTopicVisible = publishTopicVisible
        },
        top10Visible (state, top10Visible) {
            state.top10Visible = top10Visible
        },
        maxBrowseVisible (state, maxBrowseVisible) {
            state.maxBrowseVisible = maxBrowseVisible
        },
        maxCommentVisible (state, maxCommentVisible) {
            state.maxCommentVisible = maxCommentVisible
        },
        friendLinkVisible (state, friendLinkVisible) {
            state.friendLinkVisible = friendLinkVisible
        },
        statVisible (state, statVisible) {
            state.statVisible = statVisible
        }
    }
})

export default store
