<template>
  <div>
    <el-dialog custom-class="signup-dialog" title="회원가입" v-model="state.dialogVisible" @close="handleClose" top="20vh">
      <el-form :model="state.form" :rules="state.rules" ref="signupForm" :label-position="state.form.align">
        <el-form-item prop="id" label="아이디" :label-width="state.formLabelWidth" >
          <el-input v-model="state.form.id" autocomplete="off" placeholder="ID"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="비밀번호" :label-width="state.formLabelWidth">
          <el-input v-model="state.form.password" autocomplete="off" type="password" placeholder="Enter a password of 8digits or more"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="이름" :label-width="state.formLabelWidth">
          <el-input v-model="state.form.name" autocomplete="off" placeholder="Name"></el-input>
        </el-form-item>
        <el-form-item prop="email" label="이메일" :label-width="state.formLabelWidth">
          <el-input v-model="state.form.email" autocomplete="off" type="email" placeholder="E-mail"></el-input>
        </el-form-item>
        <!-- <el-form-item prop="birthday" label="생년월일" :label-width="state.formLabelWidth">
          <el-date-picker
            v-model="state.form.birthday" type="date" placeholder="Pick your birthday" style="width: 100%" format="YYYY-MM-DD" value-format="YYYYMMDD">
          </el-date-picker>
        </el-form-item> -->
        <el-form-item prop="department" label="학교" :label-width="state.formLabelWidth">
          <el-input v-model="state.form.school" autocomplete="off" placeholder="School"></el-input>
        </el-form-item>
        <el-form-item prop="position" label="연락처" :label-width="state.formLabelWidth">
          <el-input v-model="state.form.contact" autocomplete="off" placeholder="Phone"></el-input>
        </el-form-item>
      </el-form>
      <el-row class="signup-row">
        <el-button @click="clickStSignup">회원가입(학생)</el-button>
        <el-button @click="clickTchrSignup">회원가입(교사)</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>
<style>
.signup-dialog {
  width: 500px !important;
  height: auto;
  border-radius: 30px;
}
.signup-dialog .el-dialog__headerbtn {
  float: right;
}
.signup-dialog .el-form-item__content {
  margin-left: 0 !important;
  width: 200px;
  display: inline-block;
}
.signup-dialog .el-form-item {
  margin-bottom: 20px;
}
.signup-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.signup-dialog .el-input__suffix {
  display: none;
}
.signup-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.signup-dialog .dialog-footer .el-button {
  width: 120px;
}
.signup-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 40px;
}
.signup-row .el-button {
  background-color: #91847A;
  color: #fff;
  height: 45px;
  width: 40%;
  border-radius: 15px;
}
.el-picker-panel__icon-btn{
  background-color: black;
}
</style>

<script>
import { reactive, computed, ref} from 'vue'
import { useStore } from 'vuex'
export default {
  name: 'signup-dialog',
  props: {
    open: {
      type: Boolean,
      default: false
    }
  },
  setup(props, { emit }) {
    const store = useStore()
    const signupForm = ref(null)
    let reg = /(?!^(\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\w~!@#$%^&*?]{8,15}$/
    const checkPasswordPattern = function(rule, value, callback) {
      if (!reg.test(value)) {
        callback(new Error('Password should be 8-15 Numbers, letters or characters'))
      } else {
        callback()
      }
    }

    const state = reactive({
      form: {
        id: '',
        password: '',
        email: '',
        school: '',
        contact: '',
        name: '',
        align: 'left',
        // birthday:'',
      },
      rules: {
        id: [
          { required: true, message: 'Please input ID', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please input password', trigger: 'blur'},
          { validator: checkPasswordPattern, trigger: 'blur'}
        ],
        email: [
          { required: true, type: 'email', message: "Please input correct email address", trigger: ['blur', 'change'] }
        ],
        name: [
          { required: true,}
        ]
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: '120px'
    })
    const clickStSignup = function () {
      signupForm.value.validate((valid) => {
        if (valid) {
          store.dispatch('root/requestStSignup', {
            st_contact: state.form.contact,
            st_email: state.form.email,
            st_id: state.form.id,
            st_name: state.form.name,
            st_password: state.form.password,
            st_school: state.form.school,
            // st_birthday:state.form.birthday,
          })
          .then(function (result) {
            alert('회원가입(학생) 성공')
            handleClose()
          })
          .catch(function (err) {
            alert('아이디/비밀번호/이름/이메일/생일을 전부 입력하고 아이디와 이메일 중복을 확인해주세요')
          })
        } else {
          alert('아이디/비밀번호/이름/이메일/생일을 전부 입력하고 아이디와 이메일 중복을 확인해주세요')
        }
      });
    }
    const clickTchrSignup = function () {
      signupForm.value.validate((valid) => {
        if (valid) {
          store.dispatch('root/requestTchrSignup', {
            tchr_contact: state.form.contact,
            tchr_email: state.form.email,
            tchr_id: state.form.id,
            tchr_name: state.form.name,
            tchr_password: state.form.password,
            tchr_school: state.form.school,
            // tchr_birthday: state.form.birthday
          })
          .then(function (result) {
            alert('회원가입(교사) 성공')
            handleClose()
          })
          .catch(function (err) {
            alert('Form에 맞는 양식으로 작성해 주세요!')
          })
        } else {
          alert('Form에 맞는 양식으로 작성해 주세요!')
        }
      });
    }
    const handleClose = function () {
      state.form.id = ''
      state.form.password = '',
      state.form.email = '',
      state.form.school = '',
      state.form.contact = '',
      state.form.name = '',
      // state.form.birthday = '';
      emit('closeSignupDialog')
    }

    return { signupForm, state, clickStSignup, clickTchrSignup, handleClose}
  }
}
</script>
