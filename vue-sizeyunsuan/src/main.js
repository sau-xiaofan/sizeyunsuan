import Vue from 'vue'
import App from './App'
import './assets/styles/reset.css'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios'

Vue.use(ElementUI);
Vue.prototype.$axios = axios
axios.defaults.baseURL = '/api'


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
