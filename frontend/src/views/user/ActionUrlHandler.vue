<template>
  <v-row
    v-if="mode === 'resetPassword'"
    justify="center"
  >
    <ResetPassword />
  </v-row>
   <v-row
    v-else-if="mode === 'verifyEmail'"
    justify="center"
  >
    <JoinComplete />
  </v-row>
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