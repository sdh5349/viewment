<template>
  <div>
    
    <h1>ViewMent에 오신걸 환영합니다.</h1>   
    
  </div>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'

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