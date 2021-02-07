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

export default {
  methods: {
    helloJoin(){
      var para = document.location.href.split('/')[4].split('?')[1]
      const verificationURI = `https://pjt-auth-97ed8.firebaseapp.com/__/auth/action?${para}`
      window.location.href = verificationURI
    },
  },
  created() {
    
    this.helloJoin()
  }
}
</script>

<style>
.blue{
  background-color: blue;
}
</style>