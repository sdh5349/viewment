<template>
  <v-app>
    <v-main>
      <v-app-bar>
        <v-spacer></v-spacer>
        <span v-if="login">
          <v-btn
            color="primary"
            elevation="2"
            @click="onChangePassword"
            class="mr-2"
            small
          > 비밀번호 변경 </v-btn>
          <v-btn
            color="primary"
            elevation="2"
            @click="onLogout"
            small
          > 로그아웃 </v-btn>
        </span>
      </v-app-bar>
      <v-container fluid>
        <router-view 
          @login="login=true"
          @logout="login=false"
        />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: 'App',
  components: {
  },
  data: () => ({
    login: false
  }),
  methods: {
    onLogout() {
      sessionStorage.removeItem('jwt')
      sessionStorage.removeItem('uid')
      this.login = false
      this.$router.push({ name: 'Login' })
    },
    onChangePassword() {
      this.$router.push({ name: 'ChangePassword' })
    }
  },
  created() {
    const token = sessionStorage.getItem('jwt')
    if (token) {
      this.login = true
    }
  }
};
</script>