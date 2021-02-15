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
        :color="alert.color ? alert.color : 'error'"
      />
      <validation-observer
        ref="observer"
        v-slot="{ invalid }"
      >
        <form @submit.prevent="submit">
          <validation-provider
            mode="aggressive"
            v-slot="{ errors }"
            rules="required|email"
          >
            <v-text-field
              v-model="email"
              :error-messages="errors"
              label="E-mail"
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
  import { required, email } from 'vee-validate/dist/rules'
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
  // 유효성 검사 규칙 커스터마이징 끝

  export default {
    name: 'ConfirmEmail',
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
      email: '',
    }),
    methods: {
      submit () {
        this.alert.alerted = false
        this.$refs.observer.validate()

        var auth = firebase.auth();
        const self = this

        auth.sendPasswordResetEmail(this.email)
        .then(()=> {
          this.alert.message = '인증메일을 발송하였습니다.</br>인증 완료 후 로그인하시기 바랍니다.'
          this.alert.color = 'primary'
          this.alert.alerted = true
          setTimeout(() => this.$router.push({name:'Login'}), 2000)
        })
        .catch(err => {
          this.alert.message = err.message
          this.alert.alerted = true
        })
      },
    },
  }
</script>

<style>

</style>