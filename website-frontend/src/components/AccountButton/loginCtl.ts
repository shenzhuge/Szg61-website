import {ElMessage, ElMessageBox} from "element-plus";
import axios, {AxiosResponse} from "axios";
import {onMounted, reactive, ref} from "vue";
import {axiosConfiguration} from "@/main";
import {cookieHandler} from "@/utils/CookieHandler"

interface UserInfo {
    userId: string,
    username: string,
    email: string,
    avatar: string,
    lastLogin: string
}

export function useLoginCtl(userInfo: UserInfo) {
    const isLogin = ref(true)
    const dialogVisible = ref(false)

    const loginInfo = reactive({
        username: '',
        password: ''
    })
    const registerInfo = reactive({
        username: '',
        password: '',
        passwordTwice: ''
    })
    const registerRule = reactive({
        username: [
            {
                required: true,
                max: 16,
                message: '用户名过长',
                trigger: 'change'
            }
        ],
        password: [
            {
                required: true,
                validator: (rule: any, value: any, callback: any) => {
                    if (value.length < 3) callback(new Error("密码至少3位"))
                    else if (value.length > 16) callback(new Error("密码最长16位"))
                    else callback()
                },
                trigger: 'change'
            }
        ],
        passwordTwice: [
            {
                required: true,
                validator: (rule: any, value: any, callback: any) => {
                    if (value !== registerInfo.password) callback(new Error("两次输入不匹配"))
                    else callback()
                },
                trigger: 'change'
            }
        ]
    })

    function checkResult(re: AxiosResponse): boolean {
        if (re.data.status === 'ok') {
            userInfo.userId = re.data.id
            userInfo.username = re.data.username
            userInfo.email = re.data.email
            userInfo.avatar = re.data.avatar
            userInfo.lastLogin = re.data.lastLogin
            return true
        } else {
            return false
        }
    }

    function login() {
        if (loginInfo.username !== '' && loginInfo.username.length <= 16 &&
            loginInfo.password !== '' && loginInfo.password.length >= 3 && loginInfo.password.length <= 16) {
            axios({
                url: '/account/login',
                method: 'POST',
                data: {
                    username: loginInfo.username,
                    password: loginInfo.password
                }
            }).then(function (re) {
                if (checkResult(re)) {
                    dialogVisible.value = false
                    cookieHandler.setCookie("userId", userInfo.userId, 30, '/account')
                    ElMessage({
                        type: 'success',
                        message: '登录成功'
                    })
                } else {
                    ElMessage({
                        type: 'error',
                        message: re.data.status
                    })
                }
            })
        } else {
            ElMessage({
                type: 'error',
                message: '用户名密码长度检测错误，用户名不长于16位，密码在3~16位之间'
            })
        }
    }

    function register() {
        if (registerInfo.username !== '' && registerInfo.username.length <= 16 &&
            registerInfo.password !== '' && registerInfo.password.length >= 3 && registerInfo.password.length <= 16 &&
            registerInfo.password === registerInfo.passwordTwice) {
            ElMessageBox.confirm('用户名：' + registerInfo.username + '\n密码：' + registerInfo.password + '\n确认注册？', '注册信息确认', {
                confirmButtonText: '确定注册',
                cancelButtonText: '取消'
            }).then(function () {
                axios({
                    url: '/account/register',
                    method: 'POST',
                    data: {
                        username: registerInfo.username,
                        password: registerInfo.password
                    }
                }).then(function (re) {
                    if (checkResult(re)) {
                        dialogVisible.value = false
                        cookieHandler.setCookie("userId", userInfo.userId, 30, '/account')
                        ElMessage({
                            type: 'success',
                            message: '注册成功'
                        })
                    } else {
                        ElMessage({
                            type: 'error',
                            message: re.data.status
                        })
                    }
                }).catch(function () {
                    ElMessage({
                        type: 'info',
                        message: '注册取消'
                    })
                })
            })
        } else {
            ElMessage({
                type: 'error',
                message: '用户名密码格式不符合要求'
            })
        }
    }

    function logout() {
        cookieHandler.removeCookie('userId', '/account')
        userInfo.userId = ''
        userInfo.username = ''
        userInfo.email = ''
        userInfo.avatar = ''
        userInfo.lastLogin = ''
    }

    // 配置axios，尝试通过session自动登录
    onMounted(() => {
        axiosConfiguration()
        axios({
            url: '/account/autoLogin',
            method: 'POST',
        }).then(function (re) {
            if (!checkResult(re) && re.data.status !== 'no cookie') {
                ElMessage({
                    type: 'error',
                    message: re.data.status
                })
            }
        })
    })

    return {
        isLogin,
        dialogVisible,
        loginInfo,
        registerInfo,
        registerRule,
        login,
        register,
        logout
    }
}