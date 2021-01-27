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

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'EditAccount',
  components: {

  },
  props: {
    profileUserId: String
  },
  data() {
    return {
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
      // back과 연동 시

      // axios.delete(`${SERVER_URL}/accounts/${this.profileUserId}`)
      // .then(() => {
      //   alert("그동안 이용해주셔서 감사합니다.");
      //   this.$router.push({ name: 'Login' })
      // })
      // .catch(err => {
      //   alert("오류"); // TODO: 오류페이지로 변경
      //   console.log('Error', err.message);
      //   // self.$router.push({ name: 'Error' })
      // })
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