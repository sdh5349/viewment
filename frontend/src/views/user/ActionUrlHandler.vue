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
import ResetPassword from '@/components/user/ResetPassword'
import JoinComplete from '@/components/user/JoinComplete'

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