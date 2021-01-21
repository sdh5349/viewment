<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
      <h1>비밀번호 변경</h1>
      <validation-observer
        ref="observer"
        v-slot="{ invalid }"
      >
        <form @submit.prevent="submit">
          <validation-provider
            v-slot="{ errors }"
            rules="required|currentPassword"
          >
            <v-text-field
              v-model="currentPassword"
              :error-messages="errors"
              label="현재 비밀번호"
              :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
              :type="showPassword ? 'text' : 'password'"
              @click:append="showPassword = !showPassword"
            ></v-text-field>
          </validation-provider>
          <validation-provider
            v-slot="{ errors }"
            rules="required|min:8"
            name="password"
          >
            <v-text-field
              v-model="password"
              :error-messages="errors"
              label="비밀번호"
              placeholder="비밀번호를 입력해주세요."
              :type="showPassword ? 'text' : 'password'"
            ></v-text-field>
          </validation-provider>
          <validation-provider
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
  </v-row>
</template>

<script>
  import firebase from 'firebase/app'
  import { required, min } from 'vee-validate/dist/rules'
  import { extend, ValidationObserver, ValidationProvider, setInteractionMode } from 'vee-validate'

  // https://logaretm.github.io/vee-validate/guide/interaction-and-ux.html#interaction-modes
  setInteractionMode('eager') // 유효성 검사의 시기

  // 유효성 검사 규칙 커스터마이징
  extend('currentPassword', { // 비밀번호 변경을 위해 로그인한 유저의 재인증을 위한 현재 비밀번호 검사
    validate: async value => { // promise 요청으로 유효성을 검사하기 때문에 promise 함수를 동기식으로 바꿔줘야 올바른 결과값을 얻는다
      const user = firebase.auth().currentUser
      var flag = false

      if (user) {
        // 현재 유저가 로그인상태이고 해당 유저의 이메일과 입력값으로 받은 현재 비밀번호가 일치하면        
        const credential = firebase.auth.EmailAuthProvider.credential(
            user.email, 
            value
        )
        
        await user.reauthenticateWithCredential(credential)
        .then(() => {
          flag = true
        })
        .catch(err => {
        })
      }

      return flag
    },
    message: '현재 비밀번호와 다릅니다.'
  });

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
    name: 'ChangePassword',
    components: {
      ValidationProvider,
      ValidationObserver,
    },
    data: () => ({
      currentPassword: '',
      password: '',
      passwordConfirm: '',
      showPassword: false,
    }),
    methods: {
      submit () { // submit 버튼 클릭 시 발생 이벤트
        this.$refs.observer.validate()

        var user = firebase.auth().currentUser;
        const self = this

        user.updatePassword(self.password)
        .then(() => {
          alert("비밀번호 변경이 완료되었습니다.");
          self.onLogout()
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
        })
      },
      onLogout() {
        sessionStorage.removeItem('jwt')
        this.$router.push({ name: 'Login' })
      },
    },
  }
</script>

<style>

</style>