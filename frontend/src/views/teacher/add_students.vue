<template>
  <div class ="modal" style="box-shadow: 3px 3px 3px 3px gray;">
    <el-button type="text" class="overlay" @click="closeModal" style="margin-right: 5px">❌</el-button>
    <h2 style="margin-top: 4vh"><strong>학생 등록</strong></h2>
    <el-row>
      <el-col :span="20" :offset="2">
        <el-form>
          <el-form-item label="학생 ID">
            <el-input v-model="state.studId"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-button class="addbtn" @click="addstudent">추가</el-button>
  </div>
</template>

<script>
import { reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
export default {
  name: 'ModalView',
  setup(props, context) {
    const router = useRouter()
    const store = useStore()
    const state = reactive({
      studId:"",
    })
    const closeModal = function(){
      state.studId =""
      context.emit('close-modal')
    }
    const addstudent = function(){
      if(!state.studId){
        alert("아이디를 넣어주세요")
      }else{
        store.dispatch('root/requestAddStudentInStudy', {
          stId : state.studId,
          studyId: store.state.root.curClassId,
          tchrId: store.state.root.userid
        })
        .then(res=>{
          closeModal()
        })
        .catch(error=>{
          alert(error+" (학생 아이디가 존재하는지 확인해주세요!) ")
        })
      }
    }
    return {state, addstudent, closeModal}
  },

};
</script>

<style scoped>
  .modal{
    width: 500px;
    height: auto;
    position: fixed;
    top: 50%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    -moz-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    -o-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    border-radius: 20px;
    background-color: white;
  }
  .overlay{
    position: absolute;
    width : 20px;
    height : 20px;
    right: 10px;
    top: 10px;
  }
  .addbtn {
    width: 20%;
    background-color: #91847A;
    border-radius: 12px;
    text-align: center;
    color: #fff;
    margin-top: 10px;
    margin-bottom: 2vh;
  }
</style>
