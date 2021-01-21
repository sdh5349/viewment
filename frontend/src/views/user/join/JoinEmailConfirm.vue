<template>
  <v-main>
    <h1>회원가입-이메일 인증</h1>
    
    <p><input type="text" placeholder="이메일을 입력하세요" v-model="email"></p>

    <button @click='emailConfirm' class="blue">인증메일 보내기</button>
  </v-main>
</template>

<script>
import firebase from 'firebase/app'

export default {
  data () {
    return {
      email: '',
    }
  },
  methods: {
    emailConfirm() {
        const protocol = location.protocol;
        const hostName = location.hostname;
        const port = location.port;

        let url = protocol + '//' + hostName + (port ? ':' + port : '');
        // url += '/#/signupfinish';
        url += '/callback/email';

        // 이거는 임시로 해놓은 url
        url = `http://localhost:8080/user/join/${this.email}`

        console.log(url);
        const actionCodeSettings = {
          // URL you want to redirect back to. The domain (www.example.com) for
          // this URL must be whitelisted in the Firebase Console.
          url: 'http://localhost:8080/user/join',
          // This must be true for email link sign-in.
          handleCodeInApp: true,
        };


        console.log(actionCodeSettings)

        const _this = this;
        let self = this
        firebase.auth().sendSignInLinkToEmail(this.email, actionCodeSettings)
        .then(function() {
          window.localStorage.setItem('emailForSignIn', _this.email);
          alert("입력하신 전자우편으로 인증메일을 발송하였습니다. 전자우편을 통해서 로그인을 완료하시기 바랍니다.");
          _this.email = '';
        })
        .catch(function(error) {
          console.log(error)
          if(error && error.message) {
            alert(error.message);
          }
        })




      
      // const email= this.email
      // const password= 'dsadasdasd'
      // firebase.auth().createUserWithEmailAndPassword(email, password)
      // .then(userCredential => {
      //   // 체크 이메일 닉네임 제대로 맞는지? 유효한지 (저장x)
      //   // .then(이제 메일)
      //   var user = firebase.auth().currentUser
      //     user.sendEmailVerification().
      //     then(() => {
      //       console.log('성공')
      //     })
      //     .catch(error => {
      //       console.log(error)
      //     })
      // })
      // .catch(error => {
      //   console.log(error)
      // })
      
      // const currentUser = {
      //   email: email,
      // }





    }
  }
}

</script>

<style>
.blue {
  background-color: blue;
}

</style>