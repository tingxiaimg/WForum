import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const _1c394918 = () => import('..\\pages\\index.vue' /* webpackChunkName: "pages\\index" */).then(m => m.default || m)
const _022ebfe6 = () => import('..\\pages\\signup.vue' /* webpackChunkName: "pages\\signup" */).then(m => m.default || m)
const _24cf2d20 = () => import('..\\pages\\signin.vue' /* webpackChunkName: "pages\\signin" */).then(m => m.default || m)
const _b25cd81e = () => import('..\\pages\\admin\\index.vue' /* webpackChunkName: "pages\\admin\\index" */).then(m => m.default || m)
const _7b80924a = () => import('..\\pages\\links.vue' /* webpackChunkName: "pages\\links" */).then(m => m.default || m)
const _5ba8fea2 = () => import('..\\pages\\about.vue' /* webpackChunkName: "pages\\about" */).then(m => m.default || m)
const _767370c8 = () => import('..\\pages\\rank\\index.vue' /* webpackChunkName: "pages\\rank\\index" */).then(m => m.default || m)
const _39855e63 = () => import('..\\pages\\user\\edit.vue' /* webpackChunkName: "pages\\user\\edit" */).then(m => m.default || m)
const _5233a266 = () => import('..\\pages\\topic\\create.vue' /* webpackChunkName: "pages\\topic\\create" */).then(m => m.default || m)
const _0178c570 = () => import('..\\pages\\admin\\reply\\today.vue' /* webpackChunkName: "pages\\admin\\reply\\today" */).then(m => m.default || m)
const _f56f5332 = () => import('..\\pages\\admin\\reply\\list.vue' /* webpackChunkName: "pages\\admin\\reply\\list" */).then(m => m.default || m)
const _168c1516 = () => import('..\\pages\\admin\\user\\today.vue' /* webpackChunkName: "pages\\admin\\user\\today" */).then(m => m.default || m)
const _a41a18ee = () => import('..\\pages\\admin\\reply\\yesterday.vue' /* webpackChunkName: "pages\\admin\\reply\\yesterday" */).then(m => m.default || m)
const _5fa7943a = () => import('..\\pages\\admin\\topic\\today.vue' /* webpackChunkName: "pages\\admin\\topic\\today" */).then(m => m.default || m)
const _828076cc = () => import('..\\pages\\admin\\user\\list.vue' /* webpackChunkName: "pages\\admin\\user\\list" */).then(m => m.default || m)
const _5a112594 = () => import('..\\pages\\admin\\user\\yesterday.vue' /* webpackChunkName: "pages\\admin\\user\\yesterday" */).then(m => m.default || m)
const _761244e6 = () => import('..\\pages\\admin\\category\\list.vue' /* webpackChunkName: "pages\\admin\\category\\list" */).then(m => m.default || m)
const _3157c6a4 = () => import('..\\pages\\admin\\topic\\yesterday.vue' /* webpackChunkName: "pages\\admin\\topic\\yesterday" */).then(m => m.default || m)
const _28ecbe6c = () => import('..\\pages\\admin\\topic\\list.vue' /* webpackChunkName: "pages\\admin\\topic\\list" */).then(m => m.default || m)
const _6e7c092f = () => import('..\\pages\\topic\\edit\\_id.vue' /* webpackChunkName: "pages\\topic\\edit\\_id" */).then(m => m.default || m)
const _75abe8ae = () => import('..\\pages\\user\\collect\\_id.vue' /* webpackChunkName: "pages\\user\\collect\\_id" */).then(m => m.default || m)
const _40358871 = () => import('..\\pages\\user\\_id.vue' /* webpackChunkName: "pages\\user\\_id" */).then(m => m.default || m)
const _f28eca1e = () => import('..\\pages\\user\\_id\\index.vue' /* webpackChunkName: "pages\\user\\_id\\index" */).then(m => m.default || m)
const _c5fe8dee = () => import('..\\pages\\user\\_id\\reply.vue' /* webpackChunkName: "pages\\user\\_id\\reply" */).then(m => m.default || m)
const _1854e189 = () => import('..\\pages\\user\\_id\\collect.vue' /* webpackChunkName: "pages\\user\\_id\\collect" */).then(m => m.default || m)
const _982b9e8e = () => import('..\\pages\\topic\\_id.vue' /* webpackChunkName: "pages\\topic\\_id" */).then(m => m.default || m)



const scrollBehavior = (to, from, savedPosition) => {
  // SavedPosition is only available for popstate navigations.
  if (savedPosition) {
    return savedPosition
  } else {
    let position = {}
    // If no children detected
    if (to.matched.length < 2) {
      // Scroll to the top of the page
      position = { x: 0, y: 0 }
    }
    else if (to.matched.some((r) => r.components.default.options.scrollToTop)) {
      // If one of the children has scrollToTop option set to true
      position = { x: 0, y: 0 }
    }
    // If link has anchor, scroll to anchor by returning the selector
    if (to.hash) {
      position = { selector: to.hash }
    }
    return position
  }
}


export function createRouter () {
  return new Router({
    mode: 'history',
    base: '/',
    linkActiveClass: 'nuxt-link-active',
    linkExactActiveClass: 'nuxt-link-exact-active',
    scrollBehavior,
    routes: [
		{
			path: "/",
			component: _1c394918,
			name: "index"
		},
		{
			path: "/signup",
			component: _022ebfe6,
			name: "signup"
		},
		{
			path: "/signin",
			component: _24cf2d20,
			name: "signin"
		},
		{
			path: "/admin",
			component: _b25cd81e,
			name: "admin"
		},
		{
			path: "/links",
			component: _7b80924a,
			name: "links"
		},
		{
			path: "/about",
			component: _5ba8fea2,
			name: "about"
		},
		{
			path: "/rank",
			component: _767370c8,
			name: "rank"
		},
		{
			path: "/user/edit",
			component: _39855e63,
			name: "user-edit"
		},
		{
			path: "/topic/create",
			component: _5233a266,
			name: "topic-create"
		},
		{
			path: "/admin/reply/today",
			component: _0178c570,
			name: "admin-reply-today"
		},
		{
			path: "/admin/reply/list",
			component: _f56f5332,
			name: "admin-reply-list"
		},
		{
			path: "/admin/user/today",
			component: _168c1516,
			name: "admin-user-today"
		},
		{
			path: "/admin/reply/yesterday",
			component: _a41a18ee,
			name: "admin-reply-yesterday"
		},
		{
			path: "/admin/topic/today",
			component: _5fa7943a,
			name: "admin-topic-today"
		},
		{
			path: "/admin/user/list",
			component: _828076cc,
			name: "admin-user-list"
		},
		{
			path: "/admin/user/yesterday",
			component: _5a112594,
			name: "admin-user-yesterday"
		},
		{
			path: "/admin/category/list",
			component: _761244e6,
			name: "admin-category-list"
		},
		{
			path: "/admin/topic/yesterday",
			component: _3157c6a4,
			name: "admin-topic-yesterday"
		},
		{
			path: "/admin/topic/list",
			component: _28ecbe6c,
			name: "admin-topic-list"
		},
		{
			path: "/topic/edit/:id?",
			component: _6e7c092f,
			name: "topic-edit-id"
		},
		{
			path: "/user/collect/:id?",
			component: _75abe8ae,
			name: "user-collect-id"
		},
		{
			path: "/user/:id?",
			component: _40358871,
			children: [
				{
					path: "",
					component: _f28eca1e,
					name: "user-id"
				},
				{
					path: "reply",
					component: _c5fe8dee,
					name: "user-id-reply"
				},
				{
					path: "collect",
					component: _1854e189,
					name: "user-id-collect"
				}
			]
		},
		{
			path: "/topic/:id?",
			component: _982b9e8e,
			name: "topic-id"
		}
    ],
    fallback: false
  })
}
