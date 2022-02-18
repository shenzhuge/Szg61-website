import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from "axios";

const app = createApp(App)

app.use(ElementPlus)
let vm = app.mount('#app')

export function axiosConfiguration(){
    axios.defaults.baseURL = 'http://localhost'
    axios.defaults.withCredentials = true
}
