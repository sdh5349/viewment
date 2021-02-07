<template>
  <div>
    <v-list-item-avatar
      v-if="profileImage"
      size="36"
      class="my-0"
    >
      <img
        :src="imageServerPrefix + profileImage.path"
      >
    </v-list-item-avatar>
    <v-list-item-avatar
      v-else
    >
      <v-icon
        class="mr-4" 
        left 
        large
      > 
      mdi-account-circle 
      </v-icon>
      {{ nickname }}
    </v-list-item-avatar>
  </div>
</template>

<script>
export default {
  name: 'UserProfileImage',
  props: {
    profileImage: Object,
    nickname: {
      type: String,
      default: '',
    }
  }
}
</script>

<style>

</style>