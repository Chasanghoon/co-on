<template>
  <el-menu
    mode="horizontal"
    :default-active="0"
    class="el-menu-demo"
    @select="handleSelect"
    background-color="#b2d1e4"
    text-color="#fff"
    style="border:none; font-weight: bold;"
    active-text-color="#fff">
    <el-menu-item index="1" class="navitem" style="margin-left: 10px; font-size: 18px;">내 수업</el-menu-item>
    <el-menu-item index="2" class="navitem" style="font-size: 18px;">우리반보기</el-menu-item>
    <el-menu-item index="3" class="navitem" style="font-size: 18px;">출결관리</el-menu-item>
    <el-menu-item index="4" class="navitem" style="font-size: 18px;">과제관리</el-menu-item>
    <el-menu-item index="5" class = "lessonstr" style="font-size: 18px;">수업 시작</el-menu-item>
  </el-menu>
</template>

<script>
import { reactive, computed, ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
export default {
  name: 'Tchr_Lesson',
  setup(props, { emit }) {
    const router = useRouter()
    const store = useStore()
    const activeIndex = ref('1')
    const state = reactive({
      classtitle: computed(() => store.getters['root/getStudyName']),
      id: store.state.root.userid
    })
    const handleSelect = (key) => {
      if (key === '1') {
        router.push({
        name: 'Tchr_contents'
        })
      } else if (key === '2'){
        router.push({
        name: 'Tchr_ourclass'
        })
      } else if (key === '3') {
        router.push({
        name: 'Tchr_attend'
        })
      } else if (key === '4') {
        router.push({
        name: "Tchr_HWManage"
        })
      } else if (key === '5'){
        emit('startvideo')
      }
    }

    return {state, activeIndex, handleSelect}
  },
}
</script>
<style scoped>
*, html, body {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
.topnav{
  background-color: #545c64;
  color: #fff;
  display: flex;
  list-style:none;
  width: 100%;
}
.navitem{
  padding: 10px;
  margin-right: 10px;
  height: 60px;
}
.lessonstr {
  right: 10px;
  position: absolute;
}
.sub_btn{
  padding: 5px;
  margin: 10px;
  background-color: red;
  float: right;
}
</style>
