import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home',
    name: 'LoginRegister',
    component: () => import('@/views/LoginRegister'),
    children: [
      {
        path: 'register',
        name: 'Register',
        component: () => import('@/components/Register')
      },
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/components/Login')
      },
      {
        path: 'modifyPassword',
        name: 'ModifyPassword',
        component: () => import('@/components/ModifyPassword')
      }
    ]

  },
  {
    path: '/home',
    redirect: 'home/show',
    name: 'Home',
    component: () => import('@/views/Home'),
    children: [
      {
        path: 'show',
        name: 'Show',
        component: () => import('@/components/Show')
      },
      {
        path:'showDetails',
        name:'ShowDetails',
        component: () => import('@/components/ShowDetails'),
        redirect: {name: 'ShowComment'},
        children:[
          {
            path:'showComment',
            name:'ShowComment',
            component: () => import('@/components/ShowComment'),
          },
          {
            path:'showCopy',
            name:'ShowCopy',
            component: () => import('@/components/ShowCopy'),
          },
        ]
      },
      {
        path: 'copyRecord',
        name: 'CopyRecord',
        component: () => import('@/components/CopyRecord')
      },
      {
        path: 'user',
        name: 'User',
        redirect:'/home/user/userInfo',
        component: () => import('@/views/User'),
        children:[
          {
            path: 'userInfo',
            name: 'UserInfo',
            component: () => import('@/components/UserInfo')
          },
          {
            path: 'userBorrowed',
            name: 'UserBorrowed',
            component: () => import('@/components/UserBorrowed')
          },
          {
            path: 'userReserved',
            name: 'UserReserved',
            component: () => import('@/components/UserReserved')
          },
          {
            path: 'userRecord',
            name: 'UserRecord',
            component: () => import('@/components/UserRecord')
          },
          {
            path: 'userCredit',
            name: 'UserCredit',
            component: () => import('@/components/UserCredit')
          }
        ]
      },
      {
        path: 'admin',
        name: 'Admin',
        redirect: '/home/admin/returnBooks',
        component: () => import('@/views/Admin'),
        children: [
          {
            path: 'returnBooks',
            name: 'ReturnBooks',
            component: () => import('@/components/ReturnBooks')
          },
          {
            path: 'borrowBooks',
            name: 'BorrowBooks',
            component: () => import('@/components/BorrowBooks')
          },
          {
            path: 'fetchBooks',
            name: 'FetchBooks',
            component: () => import('@/components/FetchBooks')
          },
          {
            path: 'upload',
            name: 'Upload',
            component: () => import('@/components/Upload')
          },
          {
            path: 'addCopy',
            name: 'AddCopy',
            component: () => import('@/components/AddCopy')
          },
          {
            path: 'adminSensitive',
            name: 'AdminSensitive',
            component: () => import('@/components/AdminSensitive')
          },
          {
            path: 'adminCredit',
            name: 'AdminCredit',
            component: () => import('@/components/AdminCredit')
          },
          {
            path: 'adminRecord',
            name: 'AdminRecord',
            component: () => import('@/components/AdminRecord')
          },
          {
            path: 'timeSet',
            name: 'TimeSet',
            component: () => import('@/components/TimeSet')
          },
          {
            path: 'addAdmin',
            name: 'AddAdmin',
            component: () => import('@/components/AddAdmin')
          }
        ]
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
