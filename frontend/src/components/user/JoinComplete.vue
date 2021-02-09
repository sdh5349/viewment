<template>
    <v-col
    lg="4"
    md="4"
    sm="6"
  >
    <h1>ViewMent에 오신걸 환영합니다.</h1>   
  </v-col>
</template>

<script>
  import firebase from 'firebase/app'

  export default {
    name: 'JoinComplete',
    created() {
      var auth = firebase.app().auth()
      var params = this.$route.query
      var actionCode = params.oobCode

      this.handleVerifyEmail(auth, actionCode)
    },
    methods: { //
      handleVerifyEmail(auth, actionCode) { // 인증을 요청한 사용자의 emailVerifyied 필드의 값을 true로 체크해주는 함수
        const self = this

        auth.applyActionCode(actionCode)
        .then(() => {
          alert('인증이 완료되었습니다.\n해당 계정으로 접속가능합니다.');
          self.$router.push({ name: 'Login' })
        })
        .catch(err => {
          console.log(err)
        })
      }
    },
  }
</script>

<style>
.blue{
  background-color: blue;
}
</style>