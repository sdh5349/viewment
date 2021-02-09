<template>
  <div>
    <v-avatar
      :size="`${size}rem`"
    >
      <img
        v-if="this.profileImage"
        :src="prefix + profileImage.path"
      >
      <img
        v-else
        src="@/assets/images/account.png"
      >
    </v-avatar>
    {{nickname}}
  </div>
</template>

<script>

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: 'UserProfileImage',
  props: {
    profileImage: Object,
    nickname: {
      type: String,
      default: '',
    },
    size: {
      type: Number,
      default: 3,
    }
  },
  data() {
    return {
      prefix: `${SERVER_URL}/images/`,
      imagePath: null,
    }
  },
  watch: {
    profileImage() {
      this.imagePath = this.profileImage
    }
  },
  created() {
    this.imagePath = this.profileImage
  }
}
</script>

<style>

</style>