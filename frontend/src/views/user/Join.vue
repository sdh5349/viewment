<template>
  <v-row
    justify="center"
  >
    <v-col
        lg="4"
        md="4"
        sm="6"
    >
      <validation-observer ref="observer" v-slot="{ invalid }">
        <form @submit.prevent="submit">
          <validation-provider rules="required|email" v-slot="{ errors }">
            <v-text-field
              v-model="email" 
              id="email" 
              placeholder="email을 입력해주세요."  
              type="text"
              label="이메일" 
              :error-messages="errors">
            </v-text-field>
          </validation-provider>

          <validation-provider rules="required|max:8" v-slot="{ errors }">
            <v-text-field
              v-model="nickname" 
              id="nickname" 
              placeholder="닉네임을 입력해주세요."
              type="text"
              label="닉네임"
              :error-messages="errors">
            </v-text-field>
          </validation-provider>

          <validation-provider 
            mode="aggressive"
            rules="required|min:8" 
            name="password"
            v-slot="{ errors }" 
          >
            <v-text-field
              v-model="password"
              :error-messages="errors"
              label="비밀번호"
              placeholder="비밀번호를 입력해주세요."
              :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              :type="showPassword ? 'text' : 'password'"
              @click:append="showPassword = !showPassword"
            ></v-text-field>
          </validation-provider>

          <validation-provider 
            mode="aggressive"
            rules="required|min:8|password:@password" 
            v-slot="{ errors }">
            <v-text-field
              v-model="passwordConfirm"
              :error-messages="errors"
              label="비밀번호 확인"
              placeholder="비밀번호를 다시한번 입력하세요."
              :type="showPassword ? 'text' : 'password'"
            ></v-text-field>
          </validation-provider>

          <!-- <label>
            <input v-model="isTerm" type="checkbox" id="term" />
            <span>약관을 동의합니다.(아직 기능 구현 x)</span>
          </label>
          <span @click="termPopup=true">약관보기</span>
          <br> -->

          <v-btn
            class="mr-4"
            @click="submit(email, password, nickname)"
            @keypress.enter="submit(email, password, nickname)"
            :disabled="invalid"
            block
          >
            이메일 인증
          </v-btn>
        </form>
      </validation-observer>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'
import { required, email, min, max } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider } from 'vee-validate'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

extend('password', { // 비밀번호, 비밀번호 확인 입력 값 일치 검사
  params: ['target'],
  validate(value, { target }) {
    return value === target;
  },
  message: '비밀번호가 일치하지 않습니다.'
});

extend('required', {
  ...required,
  message: '필수 입력 항목입니다.'
})

extend('min', {
  ...min,
  message: '비밀번호는 8자이상이여야 합니다.'
})


extend('max', {
  ...max,
  message: '닉네임은 8자이하 입니다.'
})


extend('email', {
    ...email,
    message: '이메일 형식이 아닙니다.'
})


export default {
  components: {
    ValidationProvider,
    ValidationObserver
  },
  data: () => {
    return {
      email: "",
      password: "",
      passwordConfirm: "",
      nickname: "",
      showPassword: false,
      isTerm: false,
      isLoading: false,
    };
  },
  computed: {
  },
  methods: {
    submit(email, password, nickname) {
      firebase.auth().createUserWithEmailAndPassword(email, password)
      .then(res => {
        const createdUser = res.user
        const params = {
          "userId":  createdUser.uid,
          "password": createdUser.password,
          "email": createdUser.email,
          "nickname": nickname,
        }

        axios.post(`${SERVER_URL}/accounts`, params)
        .then(() => {
          createdUser.sendEmailVerification()
          .then(() => {
            alert('인증메일 발송 이메일을 확인해주세요')
            this.$router.push({name:'Login'})
          })
          .catch((err) => {
            alert('인증메일 발송 실패')
          })
        })
        .catch(() => {
          alert('server 계정생성 오류')
          createdUser.delete()
        })
      })
      .catch(err => {
        alert('firebase 계정생성 오류')
      })
    }
  }
}
</script>

<style>

</style>