<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <Alert
        v-if="alert.alerted"
        :message="alert.message"
      />
      <validation-observer
        ref="observer"
        v-slot="{ invalid }"
      >
        <form @submit.prevent="submit">
          <validation-provider
            rules="required|email"
            v-slot="{ errors }"
          >
            <v-text-field
              v-model="email"
              :error-messages="errors"
              label="이메일"
            ></v-text-field>
          </validation-provider>
          
          <validation-provider
            mode="aggressive"
            rules="required|min:8"
            v-slot="{ errors }"
          >
            <v-text-field
              v-model="password"
              :error-messages="errors"
              label="비밀번호"
              :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              :type="showPassword ? 'text' : 'password'"
              @click:append="showPassword = !showPassword"
            ></v-text-field>
          </validation-provider>

          <v-btn
            class="mr-4"
            type="submit"
            :disabled="invalid"
            @keypress.enter="submit"
            block
          >
            login
          </v-btn>
        </form>
      </validation-observer>
      
      <br>
      <div class="d-flex">
        <v-spacer></v-spacer>
        <v-btn
          color="primary"
          elevation="2"
          @click="onResetPassword"
          class="mr-3"
          small
        > 비밀번호 재설정 </v-btn>
        <v-btn
          color="primary"
          elevation="2"
          @click="onJoin"
          small
        > 회원가입 </v-btn>
        </div>
    </v-col>
  </v-row>
</template>

<script>
import firebase from 'firebase/app';
import { required, email, min } from 'vee-validate/dist/rules'
import { extend, ValidationObserver, ValidationProvider } from 'vee-validate'
import Alert from '@/components/common/Alert'

// 유효성 검사 규칙 커스터마이징
extend('email', {
  ...email,
  message: '이메일을 다시 확인해 주세요.',
})

extend('required', {
  ...required,
  message: '필수 입력 항목입니다.'
})

extend('min', {
  ...min,
  message: '비밀번호는 8글자 이상이어야 합니다.'
})

export default {
  components: {
    Alert,
    ValidationProvider,
    ValidationObserver,
  },
  data: () => {
    return {
      alert: {
        alerted: false,
        message: ''
      },
      email: '',
      password: '',
      showPassword: false,
    }
  },
  methods: {
    submit () {
      this.alert.alerted = false
      const self = this
      // 1시간 단위로 만료되는 토큰을 세션 종료 전(브라우저 창을 닫는 행위로)이나 직접 로그아웃을 할때 까지 유지
        firebase.auth().signInWithEmailAndPassword(self.email, self.password)
        .then(res => {
          
          const user = res.user
          const verifiedState = user.emailVerified
  
          if (verifiedState){
            user.getIdToken()
            .then(token => {
              sessionStorage.setItem('jwt', token)
              sessionStorage.setItem('uid', user.uid)
              self.$emit('login')
              self.$router.replace({ name: 'Curation' })
            })
            .catch(err => {
              this.alert.alerted = true
              this.alert.message = 'FireBase 오류'
            })}
          else{
            user.sendEmailVerification()
            .then(() => {
              this.alert.alerted = true
              this.alert.message = '이메일 인증이 안되어있습니다.</br>이메일 인증메일을 재발송 했습니다. 이메일을 확인해주세요.'
            })
            .catch((err) => {
              this.alert.alerted = true
              this.alert.message = '인증메일이 발송된지 얼마지나지 않았습니다.</br>이메일에서 인증메일을 확인해주세요.'
            })
          } 
        })
        .catch(err => {
          this.alert.alerted = true

          if (err.code === 'auth/user-not-found') {
            this.alert.message = '해당 ID는 존재하지 않습니다.'
          } else if (err.code === 'auth/wrong-password') {
            this.alert.message = '비밀번호가 틀렸습니다.'
          } else if (err.code === 'auth/too-many-requests') {
            this.alert.message = '비정상적인 로그인 시도가 있었습니다.</br>잠시 후 다시 시도해주세요.'
          }
        })  
    },
    onResetPassword() {
      this.$router.push({ name: 'ConfirmEmail' })
    },
    onJoin() {
      this.$router.push({ name: 'Join' })
    }
  },
  created() {
    const token = sessionStorage.getItem('jwt')

    if (token) {
      this.$router.push({name: 'Curation'})
    }
  },
}
</script>
