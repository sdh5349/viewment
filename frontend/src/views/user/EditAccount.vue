<template>
    <v-row
    justify="center"
  >
    <v-col
      lg="4"
      md="4"
      sm="6"
    >
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

    </v-col>
  </v-row>
</template>

<script>

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