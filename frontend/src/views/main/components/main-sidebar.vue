<template>
  <div style="height: 100%;">
    <el-row style="height: 100%">
      <el-col :span="24" style="height: 100%">
        <el-menu
          default-active="0"
          active-text-color="#ffd04b"
          background-color="#83B1C9"
          text-color="#fff"
          style="height: 100%; position: fixed; width: 241px;"
          class="el-menu-vertical-demo"
          @select="menuSelect">
          <el-container class="profile-card">
            <el-card style="text-align: center; width: 220px; border: none; margin-top: 10px; background-color: #b2d1e4; border-radius: 20px">
              <p>
                <el-avatar :size="80" fit=cover :src="state.imgpath" v-if="state.imgpath"></el-avatar>
                <el-avatar :size="80" fit=cover :src="require('@/assets/images/기본프로필.png')" v-else></el-avatar>
              </p>
              <div>
                <span style="color:white"><strong>{{ username }}</strong></span>
              </div>
              <el-button
                color="#626aef"
                style="margin-top: 5px; background-color: #91847A; color: white; font-weight:bold; border-color: #91847A"
                @click="goMypage"
                >마이페이지
              </el-button>
            </el-card>
          </el-container>

          <el-menu-item v-for="(item, index) in state.menuItems" :key="index" :index="index.toString()">
            <font-awesome-icon v-if="index==0" icon="home" style="font-size:30px"/>&nbsp;
            <span>{{ item.title }}</span>
          </el-menu-item>
          <div v-if="whetherTchr">
            <el-scrollbar max-height="50vh" >
              <el-menu-item v-for="(val, idx) in state.tchr_scha" :key="idx" :index="(idx+1).toString()" @click="MoveLesson(val[0], val[1])">
                  <span>{{ val[1] }}</span>
              </el-menu-item>
            </el-scrollbar>
            <el-menu-item @click="state.isVisible=true">
              <font-awesome-icon icon="chalkboard-teacher" style="font-size:25px"/>&nbsp;
              <span>수업개설</span>
            </el-menu-item>
            <el-menu-item v-if="state.conference" @click="MoveConference" id="desk">
              <font-awesome-icon icon="spinner" style="font-size:25px" class="fa-spin"/>
              <p>On-Air</p>
            </el-menu-item>
          <ModalView class="li_zindex" v-if ="state.isVisible" @close-modal="closeModal"></ModalView>
          </div>
          <el-menu-item class="mt-auto" style="bottom: 0; width: 240px; position : fixed; border:none;" @click="logout">
            <font-awesome-icon icon="running" style="font-size:25px"/>&nbsp;
            <p>로그아웃</p>
          </el-menu-item>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>
<style scoped>
#desk{
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 5px 50px;
  background-color: red !important;
  color: white !important;
  font-weight: bold;
  font-size: 25px;
  border: solid white 4px;
  border-radius: 20px;
}
.profile-card {
  padding: auto;
  justify-content: center;
}
.main-sidebar .el-menu {
  margin-top: 0;
  padding-left: 0;
}
.main-sidebar .hide-on-small {
  height: 100%;
}
.main-sidebar .hide-on-small .el-menu {
  height: 100%;
}
.main-sidebar .el-menu .el-menu-item {
  cursor: pointer;
  border-right: none;
}
.main-sidebar .el-menu .el-menu-item .ic {
  margin-right: 5px;
}
.li_zindex{
  z-index: 1000;
  box-shadow: 3px 3px 3px 3px gray;
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.studyList{
  border-radius: 10px;
  list-style: none;
  padding: 10px;
  margin: 5px;
}
.studyList>li{
  margin : 10px;
  cursor: pointer;
}
.studyBtn{
  width: 50px;
  height: 50px;
  border-radius: 25px;
  border: solid 1px #e5e9f2;
  text-align: center;
  cursor: pointer;
}
</style>
<script>
import ModalView from "../../teacher/tchr_create_lesson"
import { reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import $axios from 'axios'

export default {
  name: 'main-header',
  components:{
    ModalView
  },
  props: {
    width: {
      type: String,
      default: '240px'
    }
  },
  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()

    const username = store.state.root.userid
    const whetherTchr = store.state.root.whetherTchr

    const state = reactive({
      isOnAir:computed(() => store.getters['root/getStudyName']),
      isVisible: false,
      isteacher :false,
      tchr_scha: computed(() => store.getters['root/getStudy']),
      searchValue: null,
      menuItems: computed(() => {
        const MenuItems = store.getters['root/getMenus']
        let keys = Object.keys(MenuItems)
        let menuArray = []
        for (let i = 0; i < keys.length; ++i) {
          let menuObject = {}
          menuObject.icon = MenuItems[keys[i]].icon
          menuObject.title = MenuItems[keys[i]].name
          menuArray.push(menuObject)
        }
        return menuArray
      }),
      activeIndex: computed(() => store.getters['root/getActiveMenuIndex']),
      imgpath: '',
      conference: '',
    })

    if (state.activeIndex === -1) {
      state.activeIndex = 0
      store.commit('root/setMenuActive', 0)
    }

    const menuSelect = function (index) {
      store.commit('root/setMenuActive', index)
      const MenuItems = store.getters['root/getMenus']
      let keys = Object.keys(MenuItems)
      store.commit('root/changeClassName', "")
      store.commit('root/changeClassId', 0)
      router.push({
        name: keys[index]
      })
      getConference();
    }

    const goMypage = function(){
      store.commit('root/changeClassName', "")
      store.commit('root/changeClassId', 0)
      router.push({
        name: 'Mypage'
      })
      getConference();
    }

    const logout = function(){
      emit('logout1')
    }
    const MoveLesson = function(idx, name){
      store.commit('root/changeClassName', name)
      store.commit('root/changeClassId', idx)
      router.push({
        name: 'Tchr_main',
      })
      getConference();
    }
    const MoveConference = function(){
      store.commit('root/changeTchrConference', state.conference)
      router.push({
        name: 'Tchr_conference',
      })
    }
    const getClass = function(){
      store.dispatch('root/requestGetTchrClass', {
          tchrId: store.state.root.userid})
        .then(res =>{
          store.state.root.classList = res.data
          state.tchr_scha = res.data
        })
    }
    const getConference = function(){
      store.dispatch('root/requestConfInfo', {
            studyId: store.state.root.curClassId,
            tchrId: store.state.root.userid})
        .then(res =>{
          if (res.data){
            if (new Date(res.data.confStart) <new Date()){
              if (new Date() < new Date(res.data.confEnd)){
                state.conference=res.data
              }
            }
          }else {
            state.conference=''
          }
        })
    }
    const closeModal = function(){
      getClass();
      state.isVisible = false
    }
    onMounted(()=>{
      if (store.state.root.whetherTchr){
        $axios.get(`/teacher/${store.state.root.userid}?tchrId=` + store.state.root.userid )
        .then(res => {
          if (res.data.tchrProfName) {
            $axios.get(`/teacher/profile-img?fileName=${res.data.tchrProfName}`)
            .then(res => {
              state.imgpath = res.data
            })
          }
          getClass();
          getConference()
        })
        .catch(err => {
        })
      } else {
        $axios.get(`/student/${store.state.root.userid}?stId=` + store.state.root.userid )
        .then(res => {
          if (res.data.stProfName) {
            $axios.get(`/student/profile-img?fileName=${res.data.stProfName}`)
            .then(res => {
              state.imgpath = res.data
            })
          }
          getClass();
        })
      }
    })

    return { state, username, whetherTchr, onMounted, MoveLesson, MoveConference, menuSelect, logout, goMypage, getClass, closeModal}
  }
}
</script>
