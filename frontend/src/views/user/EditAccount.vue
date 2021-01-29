<template>
    <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >

    <!-- 계정 설정 버튼 시작 -->
    <v-btn 
      class="my-1 justify-start v-btn-text" 
      text 
      x-large 
      block
      @click="onChangePasswordButton"
    >
      <v-icon
        class="mr-4"
        left
        large
      >
        mdi-account-edit
      </v-icon>
      비밀번호 변경
    </v-btn>
    <v-btn 
      class="my-1 justify-start v-btn-text" 
      text 
      x-large 
      block
      @click="onLogoutButton"
    >
      <v-icon
        class="mr-4"
        left
        large
      >
        mdi-logout
      </v-icon>
      로그아웃
    </v-btn>
    <v-btn 
      class="my-1 justify-start v-btn-text" 
      text 
      x-large 
      block
      @click="onAccountDelete"
    >
      <v-icon
        class="mr-4"
        left
        large
      >
        mdi-delete
      </v-icon>
      회원탈퇴
    </v-btn>
    <!-- 계정 설정 버튼 끝 -->

    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'EditAccount',
  props: {
    profileUserId: String,
  },
  computed: {
    getToken() {
      const token = sessionStorage.getItem('jwt')

      const config = {
        headers: {
          'X-Authorization-Firebase': token
        }
      }
      return config
    }
  },
  methods: {
    onChangePasswordButton() {
      this.$router.push({ name: 'ChangePassword' })
    },
    onLogoutButton() {
      sessionStorage.removeItem('jwt')
      sessionStorage.removeItem('uid')
      this.$emit('logout')
      this.$router.push({ name: 'Login' })
    },
    onAccountDelete() {
      // TODO: firebase 재인증 후 회원탈퇴
      if (confirm("정말 회원탈퇴 하시겠습니까?")) {
        // 서버 DB에서 삭제
        axios.delete(`http://i4b105.p.ssafy.io:8080/api/v1/accounts/${this.profileUserId}`, this.getToken)
        .then(() => {
          var user = firebase.auth().currentUser;
          console.log(user)
          const self = this

          // firebase DB에서 삭제
          user.delete()
          .then(() => {
            alert("그동안 이용해주셔서 감사합니다.");
            self.onLogoutButton()
          })
          .catch(err => {
            alert("오류"); // TODO: 오류페이지로 변경
            console.log('Error', err.message);
            // self.$router.push({ name: 'Error' })
          });
        })
        .catch(err => {
          alert("오류"); // TODO: 오류페이지로 변경
          console.log('Error', err.message);
          // self.$router.push({ name: 'Error' })
        })
      }
    }
  }
}
</script>

<style scoped>
/* 버튼 내에 들어갈 텍스트의 크기를 조절한다 */
  .v-btn-text {
    font-size: 1.2rem;
  }
</style>