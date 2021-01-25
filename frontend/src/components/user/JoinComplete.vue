<template>
  <div>
    <h1>회원가입 완료</h1> 
    <h2>ViewMent에 오신걸 환영합니다.</h2>   
    <p>3초뒤 로그인 화면으로 이동합니다.</p>
  </div>
</template>

<script>
import axios from 'axios'
import firebase from 'firebase/app'

export default {
  methods: {
    helloJoin(){
      setTimeout(()=> {
        this.$router.push({name: 'Login'})
      }, 3000);
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