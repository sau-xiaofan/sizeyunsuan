import Vue from 'vue'
import Router from 'vue-router'
import sizeyunsuan from "../components/sizeyunsuan";
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'sizeyunsuan',
      component: sizeyunsuan
    },
  ]
})
