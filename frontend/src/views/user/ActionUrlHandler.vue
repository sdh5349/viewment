<template>
  <div>
    <div v-if="mode === 'resetPassword'">
      <ResetPassword />
    </div>
    <div v-else-if="mode === 'verifyEmail'">
      <JoinComplete />
    </div>
  </div>
</template>
<script>
import firebase from 'firebase/app'
import ResetPassword from '@/components/user/find/ResetPassword'
import JoinComplete from '@/components/user/join/JoinComplete'

export default {
  name: 'ActionUrlHandler',
  components: {
    ResetPassword,
    JoinComplete
  },
  data() {
    return {
      mode: '',
    }
  },
  created() {
    this.mode = this.$route.query.mode
  }
}
</script>