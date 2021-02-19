<template>
  <v-col
    lg="4"
    md="4"
    sm="6"
  >
    <Alert
      v-if="alert.alerted"
      :message="alert.message"
      :color="alert.color ? alert.color : 'error'"
    />
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
  import Alert from '@/components/common/Alert'


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
      Alert,
      ValidationProvider,
      ValidationObserver,
    },
    data: () => ({
      alert: {
        alerted: false,
        message: '',
        color: '',
      },
      password: '',
      passwordConfirm: '',
      showPassword: false,
    }),
    methods: {
      submit () { // submit 버튼 클릭 시 발생 이벤트
        this.alert.alerted = false
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
          .then(res => {
            this.alert.message = '비밀번호 변경이 완료되었습니다.'
            this.alert.color = 'primary'
            this.alert.alerted = true
            setTimeout(() => self.$router.replace({ name: 'Login' }), 2000)
          }).catch(err => {
            this.alert.message = err.message
            this.alert.alerted = true
          })
        }).catch(err => {
          this.alert.message = err.message
          this.alert.alerted = true
        })
      }
    },
  }
</script>

<style>

</style>