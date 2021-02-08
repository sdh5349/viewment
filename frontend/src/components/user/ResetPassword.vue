<template>
  <v-col
    lg="4"
    md="4"
    sm="6"
  >
    <h1>비밀번호 재설정</h1>
    <validation-observer
      ref="observer"
      v-slot="{ invalid }"
    >
      <form @submit.prevent="submit">
        <validation-provider
          mode="aggressive"
          name="password"
          rules="required|min:8"
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
          v-slot="{ errors }"
        >
          <v-text-field
            v-model="passwordConfirm"
            :error-messages="errors"
            label="비밀번호 확인"
            placeholder="비밀번호를 다시한번 입력하세요."
            :type="showPassword ? 'text' : 'password'"
          ></v-text-field>
        </validation-provider>
        <v-btn
          class="mr-4"
          type="submit"
          :disabled="invalid"
          block
        >
          submit
        </v-btn>
      </form>
    </validation-observer>
  </v-col>
</template>

<script>
  import firebase from 'firebase/app'
  import { required, min } from 'vee-validate/dist/rules'
  import { extend, ValidationObserver, ValidationProvider } from 'vee-validate'


  // 유효성 검사 규칙 커스터마이징
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
    message: '비밀번호는 8글자 이상이어야 합니다.'
  })
  // 유효성 검사 규칙 커스터마이징 끝

  export default {
    name: 'ResetPassword',
    components: {
      ValidationProvider,
      ValidationObserver,
    },
    data: () => ({
      password: '',
      passwordConfirm: '',
      showPassword: false,
    }),
    methods: {
      submit () { // submit 버튼 클릭 시 발생 이벤트
        this.$refs.observer.validate()
        
        var params = this.$route.query

        var actionCode = params.oobCode
        var continueUrl = params.continueUrl
        var lang = params.lang
        var auth = firebase.app().auth()

        this.handleResetPassword(auth, actionCode, continueUrl, lang)
      },
      handleResetPassword(auth, actionCode, continueUrl, lang) { // 계정을 찾는 사용자가 이메일 인증 후 유효한 비밀번호를 제출하면 firebase에 적용하는 함수
        const self = this

        auth.verifyPasswordResetCode(actionCode)
        .then(email => {
          auth.confirmPasswordReset(actionCode, self.password)
          .then(resp => {
            alert("비밀번호 변경이 완료되었습니다.");
            self.$router.push({ name: 'Login' }) // 현재는 로그인 화면으로 전환되지만 사용자 UX 개선을 위해 login 토큰을 받아 바로 FeedMain으로 가게 끔 수정해야함
          }).catch(err => {
            alert("오류"); // TODO: 오류페이지로 변경
            console.log('Error', err.message);
          })
        }).catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
        })
      }
    },
  }
</script>

<style>

</style>