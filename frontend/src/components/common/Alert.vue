<template>
  <v-snackbar
    v-if="isBottom"
    v-model="alertMessage"
    :color="alertColor"
    :timeout="alertTimeout"
    bottom
  >
    <span v-html="alertMessage"></span>
  </v-snackbar>
  <v-snackbar
    v-else
    v-model="alertMessage"
    :color="alertColor"
    :timeout="alertTimeout"
    top
  >
    <span v-html="alertMessage"></span>
  </v-snackbar>
</template>

<script>
export default {
  name: 'Alert',
  props: {
    color: {
      type: String,
      default : 'error'
    },
    message: {
      type: String,
    },
    timeout: {
      type: Number,
      default : 2000
    },
    isBottom: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      alertMessage: this.message,
      alertTimeout: this.timeout,
      alertColor: this.color,
    }
  }
}
</script>

<style>

</style>