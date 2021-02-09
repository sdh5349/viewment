<template>
  <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
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
      ValidationProvider,
      ValidationObserver,
    },
    data: () => ({
      email: '',
    }),
    methods: {
      submit () {
        this.$refs.observer.validate()

        var auth = firebase.auth();
        const self = this

        auth.sendPasswordResetEmail(this.email)
        .then(()=> {
          alert("입력하신 전자우편으로 인증메일을 발송하였습니다. 전자우편을 통해서 로그인을 완료하시기 바랍니다.");
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      },
    },
  }
</script>

<style>

</style>